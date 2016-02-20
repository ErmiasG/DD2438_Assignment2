using UnityEngine;
using System.Collections;

public class CameraController : MonoBehaviour {
	public GameObject dynamicPoint;

	private Vector3 offset;

	void Start ()
	{
		offset = transform.position - dynamicPoint.transform.position;
	}

	void LateUpdate ()
	{
		transform.position = dynamicPoint.transform.position + offset;
	}
}