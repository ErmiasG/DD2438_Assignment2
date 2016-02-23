using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public abstract class MotionModel : MonoBehaviour {

	public float maxSpeed;
	public float maxForce;
	public float maxSteeringAngle;
	public float carLength;
	public float mass;
	public List<Transform> wayPoints;
	public float roadRadius;
	public Vector3 start;
	public bool DrawLine = false;

	protected Vector3 location;
	protected Vector3 velocity;
	protected Vector3 acceleration;
	protected Vector3 forward;
	protected int targetWayPoint;
	protected float theta;
	protected float fixY;
	private bool startSimulation = true;

	public void setWayPoints(List<Transform> wP) {
		this.wayPoints = wP;
	}

	public void setStart (Vector3 start) {
		this.start = start;
	}

	public void setStartSimulation(bool start) {
		startSimulation = start;
	}

	// Use this for initialization
	void Start () {
		fixY = transform.position.y;//to avoid rounding error.
		transform.localScale += new Vector3 (0, 0, carLength);
		//transform.position = new Vector3 (start.x, fixY, start.z);
		forward = transform.forward;//set in applyRotation for car models
		location = new Vector3 (transform.position.x, 0, transform.position.z);
		velocity = transform.forward;
		acceleration = new Vector3 (0, 0, 0);
		targetWayPoint = 0;
	}
	
	// Update is called once per frame
	void Update () {
		if (!startSimulation) {
			return;
		}
		chooseTarget ();
		follow ();
	}

	//physics
	void FixedUpdate() {
		if (!startSimulation) {
			return;
		}
		location += (velocity * Time.deltaTime);
		transform.forward = forward;
		transform.position = new Vector3 (location.x, fixY, location.z);
	}

	//seek will set the appropriate values to acceleration or velocity to
	//steer the model to target.
	public abstract void seek (Vector3 target);

	protected void chooseTarget() {
		if (isTargetReached(targetWayPoint) && targetWayPoint < wayPoints.Count-1) {
			targetWayPoint++;
		}
		//Debug.DrawLine (location,wayPoints[targetWayPoint].position, Color.red);
	}

	protected void follow() { //set target point and call follow
		Vector3 predict = new Vector3 (velocity.x, velocity.y, velocity.z);
		predict = predict.normalized;
		predict *= 25;// should be calculated from speed
		Vector3 predictLoc = predict + location;
		Vector3 a = new Vector3(transform.position.x, 0, transform.position.z);
		Vector3 b = new Vector3(wayPoints[targetWayPoint].position.x, 0, wayPoints[targetWayPoint].position.z);
		Vector3 normalPoint = getNormalPoint (predictLoc, a, b);
		Vector3 dir = b - a;
		dir = dir.normalized;
		dir *= 50;// should be calculated from speed
		float distance = Vector3.Distance (normalPoint, predictLoc);
		if (distance > roadRadius) { //steer only if model drifts outside the road
			Vector3 target = normalPoint + dir;
			seek (target);
		} else {
			seek (wayPoints[targetWayPoint].position);
		}
	}

	//sets rotation angle theta
	protected void applyRotation (Vector3 target)
	{
		Vector3 targetDir = target - location;
		float radianAngle = 0f;
		float angle = Vector3.Angle (transform.forward, targetDir);//only returns degree 0 to 180
		if (angle > maxSteeringAngle) {
			radianAngle = maxSteeringAngle * Mathf.Deg2Rad;
		} else {
			radianAngle = angle * Mathf.Deg2Rad;
		}
		if (Vector3.Cross(transform.forward, targetDir).y < 0) {
			radianAngle = -radianAngle;
		}
		angle = velocity.magnitude * Mathf.Tan (radianAngle) / (transform.localScale.z);
		theta = angle * Time.deltaTime;
		forward = Quaternion.AngleAxis(theta* Mathf.Rad2Deg , Vector3.up) * forward ;
	}


	protected void applyForce(Vector3 force) 
	{
		if (mass <= 0) {
			mass = 1f;
		}
		acceleration += force / mass;
		float distance = distanceToFinish();
		float brakingDistance =  (maxSpeed*maxSpeed)/(2*maxForce);
		if (distance<brakingDistance) {
			maxSpeed -= maxForce * Time.deltaTime;
			if (maxSpeed < 1.5f) {
				maxSpeed = 0;
			}
		} 
	}

	protected float distanceToFinish() {
		float distance = 0;
		for (int i = targetWayPoint; i<wayPoints.Count; i++) {
			distance += Vector3.Distance (wayPoints[i].position, location);
		}
		return distance;
	}

	protected Vector3 getNormalPoint (Vector3 p, Vector3 a, Vector3 b)
	{
		Vector3 ap = p - a;
		Vector3 ab = b - a;
		ab = ab.normalized;
		ab *= Vector3.Dot (ap, ab);
		Vector3 normalPoint = a + ab;
		return normalPoint;
	}

	protected bool isTargetReached(int wayPointIndex){
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
				if (DrawLine)
				  Gizmos.DrawLine (wayPoints[i].position,wayPoints[i+1].position);
			}
			if (DrawLine)
			   Gizmos.DrawSphere (wayPoints[wayPoints.Count - 1].position, 0.5f);
		}
	}
}