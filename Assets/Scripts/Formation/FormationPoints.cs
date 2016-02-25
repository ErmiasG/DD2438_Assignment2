using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class FormationPoints : MonoBehaviour
{
	public List<Vector2> formationPoints;
	public Transform vehicles;
	public int destance;
	public int moveDestance;
	public bool moving;
	private MotionModel[] mm;
	private Vector3[] destinations;
	private Vector3[] positions;
	private int[] assignment;
	private int fps = 0;
	private int steps = 0;
	private int rnd = 1;

	void OnDrawGizmos ()
	{
		Gizmos.DrawSphere (findCenterOfMass (), 1f);
	}
	/**
	 * Note for C# and Boo users: use Awake instead of the constructor for initialization, 
	 * as the serialized state of the component is undefined at construction time.
	 */ 
	void Awake ()
	{
		setFormationPoints ();
		List<Transform> wP;

		float dist = 0;
		float dt = Time.fixedDeltaTime;
		positions = new Vector3[vehicles.childCount];
		assignment = new int[vehicles.childCount];
		destinations = new Vector3[transform.childCount];
		mm = new MotionModel[vehicles.childCount];
		for (int i = 0; i < vehicles.childCount; i++) {
			positions [i] = vehicles.GetChild (i).position; //get all the positions of the vehicles.
			assignment [i] = i; // first assign every one coresponding destination.
			destinations [i] = transform.GetChild (i).position;
			mm[i] = vehicles.GetChild (i).gameObject.GetComponent<MotionModel> ();
			mm[i].setStartSimulation (false);//stop simulation until we calculate a path
		}
		// do assignment of destination to vehicles.
		findAssignment ();
		//set destination for the vehicles.
		for (int i = 0; i < vehicles.childCount; i++) {
			wP = new List<Transform> ();
			wP.Add (transform.GetChild (assignment [i]));
			dist = Vector3.Distance (mm[i].transform.position, transform.GetChild (assignment [i]).position);
			mm[i].setWayPoints (wP, dist / (dt * 1000));
			//mm.setStart ();//we can set initial positions here if we want.
			mm[i].setMovingFormation (moving);
			mm[i].setStartSimulation (true);
		}
//		for (int j = 0; j < 30; j++) {
//			Vector3 center = new Vector3 (moveDestance, 0, 0);
//			moveFormation (center);
//			for (int i = 0; i < vehicles.childCount; i++) {
//				dist = Vector3.Distance (mm[i].transform.position, transform.GetChild (assignment [i]).position);
//				mm[i].addWayPoint (transform.GetChild (assignment [i]), dist / (dt * 1000));
//			}
//		}
//		if (moving) {
//			for (int i = 0; i < vehicles.childCount; i++) {
//				mm[i].setStartSimulation (true);
//			}
//		}
	}

	void Update ()
	{
		if (!moving) {
			return;
		}
		if (moving && fps < 5) {
			fps++;
			return;
		}
		fps = 0;
		float dist = 0;
		float dt = Time.fixedDeltaTime;
		if (steps > 50) {
			rnd = Random.Range (1, 4);
			steps = 0;
		}
		//Debug.Log (rnd);
		steps++;
		Vector3 center;
		if (rnd == 1) { // move north
			center = new Vector3 (moveDestance, 0, 0);
		} else if (rnd == 2) { // move east
			center = new Vector3 (0, 0, moveDestance);
		} else if (rnd == 3) { // move west
			center = new Vector3 (0, 0, -moveDestance);
		} else { //move south
			center = new Vector3 (-moveDestance, 0, 0);
		}
		moveFormation (center);
		List<Transform> wP;
		for (int i = 0; i < vehicles.childCount; i++) {
			wP = new List<Transform> ();
			dist = Vector3.Distance (mm[i].transform.position, transform.GetChild (assignment [i]).position);
			wP.Add (transform.GetChild (assignment [i]));
			mm[i].setWayPoints (wP, dist / (dt * 1000));
		}

	}

	//tries to find a minimum distance assignment.
	void findAssignment ()
	{
		float distance = calculatDistance (assignment);
		int[] current;
		bool converged = false;
		int temp = 0;
		float d;
		bool breakInnerLoop;
		while (!converged) {
			converged = true;
			breakInnerLoop = false;
			for (int i = 0; i < assignment.Length && !breakInnerLoop; i++) {
				for (int j = 0; j < assignment.Length && !breakInnerLoop; j++) {
					current = (int[])assignment.Clone ();
					temp = current [i];
					current [i] = current [j];
					current [j] = temp;
					d = calculatDistance (current);
					if (d < distance) {
						converged = false;
						distance = d;
						assignment = current;
						breakInnerLoop = true;
					}
				}
			}
		}
		Debug.Log ("Done!");
	}

	float calculatDistance (int[] l)
	{
		float dist = 0;
		for (int i = 0; i < l.Length; i++) {
			dist += Vector3.Distance (positions [i], destinations [l [i]]);
		}
		return dist;
	}

	void setFormationPoints ()
	{
		if (formationPoints.Count == transform.childCount) {
			Vector3 center = findCenterOfMass ();
			Vector3 pos;
			for (int i = 0; i < transform.childCount; i++) {
				pos = new Vector3 (formationPoints [i].x * destance, 0.5f, formationPoints [i].y * destance);
				transform.GetChild (i).position = pos + center;
			}
		}
	}

	void moveFormation (Vector3 center)
	{
		Vector3 pos;
		for (int i = 0; i < transform.childCount; i++) {
			pos = new Vector3 (transform.GetChild (i).position.x, 0.5f, transform.GetChild (i).position.z);
			transform.GetChild (i).position = pos + center;
		}
	}
	
	Vector3 findCenterOfMass ()
	{
		int M = vehicles.childCount;
		Vector3 sum = new Vector3 ();
		for (int i = 0; i < vehicles.childCount; i++) {
			sum += vehicles.GetChild (i).position;
		}
		return sum / M;
	}


}