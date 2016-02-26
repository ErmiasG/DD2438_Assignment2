using UnityEngine;
using System.Collections;
using System.Collections.Generic;
public class FormationPoints : MonoBehaviour
{
	public List<Vector2> formationPoints;
	public Transform vehicles;
	public Transform centerOfMass;
	public int destance;
	public int moveDestance;
	public bool moving;
	private MotionModel[] mm;
	private Vector3[] destinations;
	private Vector3[] positions;
	private int[] assignment;
	int fps;
	float dist = 0;
	Vector3 center;

	void OnDrawGizmos ()
	{
		centerOfMass.position = findCenterOfMass ();
	}
	/**
	 * Note for C# and Boo users: use Awake instead of the constructor for initialization, 
	 * as the serialized state of the component is undefined at construction time.
	 */ 
	void Awake ()
	{
		setFormationPoints ();
		List<Transform> wP;

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
		float dt = Time.fixedDeltaTime;
		for (int i = 0; i < vehicles.childCount; i++) {
			wP = new List<Transform> ();
			dist = Vector3.Distance (mm[i].transform.position, transform.GetChild (assignment [i]).position);
			wP.Add (transform.GetChild (assignment [i]));
			mm [i].setWayPoints (wP, dist / (dt * 1000));
			mm [i].setMovingFormation (moving);
			mm [i].setStartSimulation (true);
		}
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
		float dt = Time.fixedDeltaTime;
		center = new Vector3 (0, 0, moveDestance);
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