using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public abstract class MotionModel : MonoBehaviour {

	public float maxSpeed;
	public float maxForce;
	public float maxSteeringAngle;
	public float carLength;
	public List<Transform> wayPoints;
	public float roadRadius;
	public Vector3 start;

	protected Vector3 location;
	protected Vector3 velocity;
	protected Vector3 acceleration;
	protected int targetWayPoint;
	protected float theta;
	protected float fixY;

	// Use this for initialization
	void Start () {
		fixY = transform.position.y;//to avoid rounding error.
		transform.position = new Vector3 (start.x, fixY, start.z);
		location = new Vector3 (transform.position.x, 0, transform.position.z);
		velocity = transform.forward;
		acceleration = new Vector3 (0, 0, 0);
		targetWayPoint = 0;
	}
	
	// Update is called once per frame
	void Update () {
		chooseTarget ();
		follow ();
		velocity += acceleration;// in kinematic acceleration is always 0
		velocity = velocity.normalized;
		velocity *= maxSpeed;
		Debug.Log (string.Format("******* velocity ==> {0}", velocity));
		location += (velocity * Time.deltaTime);
		transform.position = new Vector3 (location.x, fixY, location.z);
		acceleration *= 0;
	}

	//seek will set the appropriate values to acceleration or velocity to
	//steer the model to target.
	public abstract void seek (Vector3 target);

	void chooseTarget() {
		if (isTargetReached(targetWayPoint) && targetWayPoint < wayPoints.Count-1) {
			targetWayPoint++;
		}
	}

	void follow() { //set target point and call follow
		Vector3 predict = new Vector3 (velocity.x, velocity.y, velocity.z);
		predict = predict.normalized;
		predict *= 25;// should be calculated from speed
		Vector3 predictLoc = predict + location;
		Vector3 a = new Vector3(transform.position.x, 0, transform.position.z);
		Vector3 b = new Vector3(wayPoints[targetWayPoint].position.x, 0, wayPoints[targetWayPoint].position.z);
		Vector3 normalPoint = getNormalPoint (predictLoc, a, b);

		Vector3 dir = b - a;
		dir = dir.normalized;
		dir *= 10;
		//Debug.Log (string.Format("normal point ==> {0}, predict Location {1}", normalPoint, predictLoc));
		float distance = Vector3.Distance (normalPoint, predictLoc);
		Debug.Log (string.Format("--------------- distance ==> {0}", distance));
		if (distance > roadRadius) { //steer only if model drifts outside the road
			Vector3 target = normalPoint + dir;
			seek (target);
			Debug.Log (string.Format("target to seek ==> {0}", target));
		}
	}

	Vector3 getNormalPoint (Vector3 p, Vector3 a, Vector3 b)
	{
		Vector3 ap = p - a;
		Vector3 ab = b - a;
		ab = ab.normalized;
		ab *= Vector3.Dot (ap, ab);
		Vector3 normalPoint = a + ab;
		return normalPoint;
	}

	bool isTargetReached(int wayPointIndex){
		Vector3 wayP = new Vector3 (wayPoints[targetWayPoint].position.x, 0, wayPoints[targetWayPoint].position.z);
		return (wayP - location).magnitude < roadRadius;
	}

	void OnDrawGizmos()
	{
		if (wayPoints.Count > 0) {
			for (int i = 0; i < wayPoints.Count - 1; i++) {
				if (wayPoints[i] != null) {
					Gizmos.DrawSphere (wayPoints[i].position, 0.5f);
				}
				Gizmos.DrawLine (wayPoints[i].position,wayPoints[i+1].position);
			}
			Gizmos.DrawSphere (wayPoints[wayPoints.Count - 1].position, 0.5f);
		}
	}
}
