using UnityEngine;
using System.Collections;

public class CameraController : MonoBehaviour {
	public GameObject gameObj;

	private Vector3 offset;

	void Start ()
	{
		offset = transform.position - gameObj.transform.position;
	}

	void LateUpdate ()
	{
		transform.position = gameObj.transform.position + offset;
	}
}