using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class FormationPoints : MonoBehaviour
{
	public List<Vector2> formationPoints;
	public int destance;

	// Use this for initialization
	void OnDrawGizmos ()
	{
		if (formationPoints.Count == transform.childCount) {
			for (int i = 0; i < transform.childCount; i++) {
				transform.GetChild (i).position = new Vector3(formationPoints [i].x  * destance, 0.5f, formationPoints [i].y  * destance);
				Gizmos.DrawSphere (transform.GetChild (i).position, 0.5f);
			}
		}
	}

}