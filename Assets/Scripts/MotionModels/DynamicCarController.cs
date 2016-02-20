using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class DynamicCarController : MotionModel
{

	// TODO: make car model now it is just point model
	public override void seek(Vector3 target) {
		Vector3 desired = target - location;
		if (desired.magnitude == 0) {
			return;
		}
		desired = desired.normalized;
		desired *= maxSpeed;
		Vector3 steer = desired - velocity;
		steer = steer.normalized;
		steer *= maxForce;
		applyForce(steer);
	}

	void applyRotation (Vector3 target)
	{
		Vector3 targetDir = target - location;
		float angle = Vector3.Angle (targetDir, transform.forward - location);
		if (angle > maxSteeringAngle) {
			angle = maxSteeringAngle;
		}
		theta = angle;
	}

	void applyForce (Vector3 force)
	{
		acceleration += force;
	}
}