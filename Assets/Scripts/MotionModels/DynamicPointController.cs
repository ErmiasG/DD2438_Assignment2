using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class DynamicPointController :MotionModel {

	public override void seek(Vector3 target) 
	{
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
		velocity += acceleration;// in kinematic acceleration is always 0
		if (velocity.magnitude > maxSpeed) {
			velocity = velocity.normalized;
			velocity *= maxSpeed;
		}
		acceleration *= 0;
	}
}