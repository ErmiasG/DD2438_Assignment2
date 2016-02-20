using UnityEngine;
using System.Collections;

public class KinematicCarController : MotionModel
{

	// TODO: make car model now it is just point model
	public override void seek(Vector3 target) 
	{
		Vector3 desired = target - location;
		applyVelocity (desired);
	}

	void applyVelocity(Vector3 v) 
	{
		velocity = v;
	}
}

