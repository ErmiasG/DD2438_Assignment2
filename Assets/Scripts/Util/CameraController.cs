using UnityEngine;
using System.Collections;

public class CameraController : MonoBehaviour {
	public GameObject gameObj0;
	public GameObject gameObj1;
	public GameObject gameObj2;
	public int objNumber;

	private Vector3 offset;
	private GameObject gameObj;

	void Start ()
	{
		if (objNumber == 1) {
			gameObj = gameObj1;
		} else if (objNumber == 2) {
			gameObj = gameObj2;
		} else {
			gameObj = gameObj0;
		}
		transform.position = new Vector3(gameObj.transform.position.x,15f,gameObj.transform.position.z);
		transform.Rotate (new Vector3(1,0,0) * 45);
		offset = transform.position - gameObj.transform.position;
	}

	void LateUpdate ()
	{
		transform.position = gameObj.transform.position + offset;
	}
}