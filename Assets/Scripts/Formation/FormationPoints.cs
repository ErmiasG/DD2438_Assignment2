using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class FormationPoints : MonoBehaviour
{
	public List<Vector2> formationPoints;
	public Transform vehicles;
	public int destance;

	private MotionModel mm;
	private Vector3[] destinations;
	private Vector3[] positions;
	private int[] assignment;

	void OnDrawGizmos()
	{
		Gizmos.DrawSphere (findCenterOfMass(), 1f);
	}
	/**
	 * Note for C# and Boo users: use Awake instead of the constructor for initialization, 
	 * as the serialized state of the component is undefined at construction time.
	 */ 
	void Awake ()
	{
		Draw ();
		List<Transform> wP;
		positions = new Vector3[vehicles.childCount];
		assignment = new int[vehicles.childCount];
		destinations = new Vector3[transform.childCount];
		for (int i = 0; i < vehicles.childCount; i++) {
			positions [i] = vehicles.GetChild (i).position; //get all the positions of the vehicles.
			assignment [i] = i; // first assign every one coresponding destination.
			destinations [i] = transform.GetChild (i).position;
			mm = vehicles.GetChild (i).gameObject.GetComponent<MotionModel> ();
			mm.setStartSimulation (false);//stop simulation until we calculate a path
		}
		// do assignment of destination to vehicles.
		findAssignment ();
		//set destination for the vehicles.
		for (int i = 0; i < vehicles.childCount; i++) {
			wP = new List<Transform> (1);
			mm = vehicles.GetChild (i).gameObject.GetComponent<MotionModel> ();
			wP.Add (transform.GetChild (assignment [i]));
			mm.setWayPoints (wP);
			//mm.setStart ();//we can set initial positions here if we want.
			mm.setStartSimulation (true);
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
		bool breakInnerLoop ;
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

	void Draw()
	{
		if (formationPoints.Count == transform.childCount) {
			Vector3 center = findCenterOfMass();
			Vector3 pos;
			for (int i = 0; i < transform.childCount; i++) {
				pos = new Vector3 (formationPoints [i].x * destance, 0.5f, formationPoints [i].y * destance);
				transform.GetChild (i).position = pos + center;
			}
		}
	}
	
	Vector3 findCenterOfMass() {
		int M = vehicles.childCount;
		Vector3 sum = new Vector3();
		for (int i = 0; i < vehicles.childCount; i++) {
			sum += vehicles.GetChild (i).position;
		}
		return sum / M;
	}


}