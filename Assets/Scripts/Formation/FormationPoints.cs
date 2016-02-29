using UnityEngine;
using System.Collections;
using System.Collections.Generic;
public class FormationPoints : MonoBehaviour
{
	public List<Vector2> formationPoints;
	public Transform vehicles;
	public Transform points;
	public bool useVehicle;
	public Transform wayPoints;
	public Transform centerOfMass;
	public int destance;
	public bool movingFormation;
	public bool GlobalKnowledge;
	public float speedBallisticZone;
	public float speedControlledZone;
	public float speedDeadZone;
	private MotionModel[] mm;
	private Vector3[] destinations;
	private Vector3[] positions;
	private int[] assignment;
	private List<Vector3> formationPointsV3;
	public float visiblityRadius;

	int fps;
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
		Transform models;
		positions = new Vector3[vehicles.childCount];
		assignment = new int[vehicles.childCount];
		destinations = new Vector3[transform.childCount];

		if (useVehicle) {
			models = vehicles;
			for (int i = 0; i < points.childCount; i++) {
				points.GetChild(i).gameObject.GetComponent<MotionModel> ().setStartSimulation(false);
			}
		} else {
			models = points;
			for (int i = 0; i < vehicles.childCount; i++) {
				vehicles.GetChild(i).gameObject.GetComponent<MotionModel> ().setStartSimulation(false);
			}
		}
		mm = new MotionModel[models.childCount];
		for (int i = 0; i < models.childCount; i++) {
			positions [i] = models.GetChild (i).position; //get all the positions of the vehicles.
			assignment [i] = i; // first assign every one coresponding destination.
			destinations [i] = transform.GetChild (i).position;
			mm[i] = models.GetChild (i).gameObject.GetComponent<MotionModel> ();
			mm[i].setStartSimulation (false);//stop simulation until we calculate a path
		}
		// do assignment of destination to vehicles.
		findAssignment ();
		if (!movingFormation) {
			float dt = Time.fixedDeltaTime;
			for (int i = 0; i < vehicles.childCount; i++) {
				wP = new List<Transform> ();
				wP.Add (transform.GetChild (assignment [i]));
				mm [i].setWayPoints (wP);
				//mm.setStart ();//we can set initial positions here if we want.
				mm [i].setMovingFormation (movingFormation);
				mm [i].setStartSimulation (true);
			}

		} else if (movingFormation && GlobalKnowledge) {
			wP = new List<Transform> (wayPoints.childCount);
			foreach (Transform child in wayPoints) {
				wP.Add (child);
			}
			mm [0].setId (0);
			mm [0].setWayPoints (wP);
			mm [0].setFormationDist (destance);
			mm [0].setFormationPoints (formationPointsV3);
			mm [0].setSpeedBallisticZone (speedBallisticZone);
			mm [0].setSpeedControlledZone (speedControlledZone);
			mm [0].setFollowers (mm);
			mm [0].setMovingFormation (movingFormation);
			mm [0].setStartSimulation (true);
			for (int i = 1; i < models.childCount; i++) {
				mm [i].setId (i);
				mm [i].setLeader (mm [0]);
				mm [i].setSpeedBallisticZone (speedBallisticZone);
				mm [i].setSpeedControlledZone (speedControlledZone);
				mm [i].setMovingFormation (movingFormation);
				mm [i].setStartSimulation (true);
			}
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
		formationPointsV3 = new List<Vector3> (transform.childCount);
		if (formationPoints.Count == transform.childCount) {
			Vector3 center = findCenterOfMass ();
			Vector3 pos;
			for (int i = 0; i < transform.childCount; i++) {
				pos = new Vector3 (formationPoints [i].x * destance, 0.5f, formationPoints [i].y * destance);
				transform.GetChild (i).position = pos + center;
				formationPointsV3.Add( pos);
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