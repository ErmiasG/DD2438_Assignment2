using UnityEngine;
using System.Collections;

public class BuildObstacle : MonoBehaviour {

	public GameObject wallPrefab;
	public GameObject customerPrefab;
	public GameObject startPrefab;
	public GameObject endPrefab;
	public TextAsset textFile;

	GameObject wall;
//	GameObject customer;
//	GameObject start;
//	GameObject end;

	// Use this for initialization
	void Awake () {
		string content = textFile.text;
		string[] tokens = content.Split ('\n');
		Vector3[] corrdinats = new Vector3[24];
		Vector3[] customersP = new Vector3[8];
		Vector3[] startP = new Vector3[5];
		Vector3[] endP = new Vector3[5];
		float[] button = new float[24];
		float x;
		float z;
		for (int i = 0; i < 24; i++) {
			x = float.Parse(tokens [i].Trim());
			z = float.Parse(tokens [i + 24].Trim());
			corrdinats[i] = new Vector3(x, 0 , z);
			button[i] = float.Parse(tokens [i + 48].Trim());
		}
		string[] objects;
		for (int i = 24*3 , j = 0; i < tokens.Length -1 ; i++,j++) {
			objects = System.Text.RegularExpressions.Regex.Split( tokens [i].Trim(), @"\s{2,}");
			x = float.Parse(objects [0].Trim());
			z = float.Parse(objects [1].Trim());
			if (j < startP.Length) {
				startP[j] = new Vector3(x, 0, z);
			} else if (j < startP.Length*2) {
				endP[j - startP.Length] = new Vector3(x, 0, z);
			} else {
				customersP[j - startP.Length*2] = new Vector3(x, 2, z);
			}

		}
		//Debug.Log (content);
		int firstEdge = 0;
		for (int i = 0; i < corrdinats.Length; i++) {
			float distance = 0;
			Vector3 a = new Vector3();
			if (button[i] == 1) {
				distance = Vector3.Distance(corrdinats[i] ,corrdinats[i+1]);
				a = corrdinats[i+1];
			} else if (button[i] == 3) {
				distance = Vector3.Distance(corrdinats[i] ,corrdinats[firstEdge]);
				a = corrdinats[firstEdge];
				firstEdge = i + 1;
			}
			wall = (GameObject) Instantiate (wallPrefab, Vector3.zero, Quaternion.identity);
			wall.transform.position = corrdinats[i];
			wall.transform.LookAt(a);
			wall.transform.position = corrdinats[i] + distance/2 * wall.transform.forward;
			wall.transform.localScale = new Vector3(wall.transform.localScale.x, wall.transform.localScale.y, distance);

		}
		for (int i = 0; i < startP.Length; i++) {
			Instantiate (startPrefab, startP[i], Quaternion.identity);
			Instantiate (endPrefab, endP[i], Quaternion.identity);
		}
		for (int i = 0; i < customersP.Length; i++) {
			Instantiate (customerPrefab, customersP[i], Quaternion.identity);
		}

	}

}