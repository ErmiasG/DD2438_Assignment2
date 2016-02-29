using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

public abstract class MotionModel : MonoBehaviour {

	public float maxSpeed;
	public float maxForce;
	public float maxSteeringAngle;
	public float carLength;
	public float mass;
	//public float formationDeadZone;
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
	protected bool movingFormation = false;
	protected MotionModel leader;
	private Vector3 target;
	private MotionModel[] followers;
	private int id;
	private int formationDistance;
	private Vector3[] formationPositions;
	private List<Vector3> formationPoints;
	private float NormalSpeed;
	private float MaximumSpeed;
	private float MinimumSpeed;
	private float speedBallisticZone;
	private float speedControlledZone;


	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public Vector3 getLocation() {
		return this.location;
	}

	public Vector3 getForward() {
		return this.forward;
	}

	public void setWayPoints(List<Transform> wP) {
		this.wayPoints = wP;
	}

	public void setMovingFormation(bool b) {
		movingFormation = b;
	}

	public void setStartSimulation(bool start) {
		startSimulation = start;
	}

	public void setLeader(MotionModel leader) {
		this.leader = leader;
	}

	public MotionModel getLeader() {
		return this.leader;
	}

	public MotionModel[] getFollowers() {
		return this.followers;
	}

	public void setFollowers(MotionModel[] followers) {
		this.followers = followers;
	}

	public void setFormationDist(int dist) {
		this.formationDistance = dist;
	}

	public void setFormationPoints (List<Vector3> formationPoints) {
		this.formationPoints = formationPoints;
		assignPosition ();
	}

	public void setSpeedBallisticZone(float bz) {
		this.speedBallisticZone = bz;
	}
	public void setSpeedControlledZone(float cz) {
		this.speedControlledZone = cz;
	}
	public Vector3 getTarget() {
		if (leader != null) {
			return leader.getTarget ();
		} else {
			//return this.target;
			return wayPoints[targetWayPoint].position;
		}
	}

	public Vector3 getFormationPosition(int id) {
		if (leader == null) {
			return formationPositions [id];
		} else {
			throw new Exception(String.Format("I am not a leader! Car {0} is the leader",this.leader.getId())); 
		}
	}

	private void assignPosition() {
		formationPositions = new Vector3[formationPoints.Count];
		formationPositions [this.id] = Vector3.zero;

		Vector3 middel = formationPoints [formationPoints.Count / 2];
		for (int i = 0, j = 0; i < formationPoints.Count; i++) {
			if (j == formationPoints.Count / 2) {
				j++; i--;
				continue;
			}
			if (i != this.id) {
				formationPositions [i] = formationPoints[j] - middel;
				formationPositions [i] = Quaternion.Euler (transform.rotation.eulerAngles) * formationPositions [i];
				j++;
			}
		}
	}


	// Use this for initialization
	void Start () {
		NormalSpeed = maxSpeed;
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
		if (!movingFormation || leader == null) { // if not moving formation or have no leader follow path
			chooseTarget ();
			follow ();
			if (leader == null) {
				rotateFormation ();
			}
		} else if (movingFormation && leader != null) {
			followLeader ();
		}

	}

	//physics
	void FixedUpdate() {
		if (!startSimulation) {
			return;
		}
		location += (velocity * Time.deltaTime);//fixed does not work here
		transform.forward = forward;
		transform.position = new Vector3 (location.x, fixY, location.z);
	}

	//seek will set the appropriate values to acceleration or velocity to
	//steer the model to target.
	public abstract void seek (Vector3 target);

	protected void chooseTarget() {
		if ( targetWayPoint < wayPoints.Count-1 && isTargetReached()) {
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
			target = normalPoint + dir;
		} else {
			target = wayPoints[targetWayPoint].position;
		}
		seek (target);
	}

	protected void followLeader () {
		//Debug.DrawRay (location, leader.getFormationPosition(this.id), Color.green);
		Vector3 target = getTarget() +  leader.getFormationPosition(this.id);
		adjustSpeed (leader.getLocation() + leader.getFormationPosition(this.id));
		seek (target);
	}

	void adjustSpeed(Vector3 target) {
		Vector3 desired = target - location;
		float distance = Vector3.Distance (target, location);
		float angle = Vector3.Angle (transform.forward, desired);
		if (angle > 90) {//slow-down
			//Debug.DrawRay (location, desired, Color.yellow);
			if (distance >= speedBallisticZone) {
				maxSpeed = NormalSpeed*0.4f;
			} else if (distance < speedBallisticZone && distance >= speedControlledZone) {
				maxSpeed = NormalSpeed* 0.4f *  (speedBallisticZone - speedControlledZone)/distance;
			} else {
				maxSpeed = NormalSpeed;
			}
		} else {//speed-up
			if (distance >= speedBallisticZone) {
				maxSpeed = NormalSpeed*1.4f;
			} else if (distance < speedBallisticZone && distance >= speedControlledZone) {
				maxSpeed = NormalSpeed*1.4f * distance / (speedBallisticZone - speedControlledZone);
			} else {
				maxSpeed = NormalSpeed;
			}
			//Debug.DrawRay (location, desired, Color.green);
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
		theta = angle * Time.deltaTime;//fixed does not work here
		//forward = Quaternion.AngleAxis(theta* Mathf.Rad2Deg , Vector3.up) * forward ;
		forward = Quaternion.Euler (new Vector3(0,theta* Mathf.Rad2Deg,0)) * forward ; //more efficent
	}

	void rotateFormation () {
		for (int i = 0; i < formationPositions.Length; i++) {
			formationPositions [i] = Quaternion.Euler (new Vector3(0,theta* Mathf.Rad2Deg,0)) * formationPositions [i];
			Debug.DrawRay (location, formationPositions[i], Color.red);
		}
	}

	protected void applyForce(Vector3 force) 
	{
		if (mass <= 0) {
			mass = 1f;
		}
		acceleration += force / mass;
		if (!movingFormation) {
			float distance = distanceToFinish();
			float brakingDistance =  (maxSpeed*maxSpeed)/(2*maxForce);
			if (distance<brakingDistance) {
				maxSpeed -= maxForce * Time.deltaTime;
				if (maxSpeed < 1.5f) {
					maxSpeed = 0;
				}
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

	protected bool isTargetReached(){
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