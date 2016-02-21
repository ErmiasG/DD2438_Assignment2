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
//		desired = desired.normalized;
//	    desired *= maxSpeed;
		Vector3 steer = desired - velocity;
		steer = steer.normalized;
		steer *= maxForce;
		applyForce(steer);
		//Debug.Log (string.Format("Max velocity point==> {0}", maxSpeed));
		velocity += acceleration;
		if (velocity.magnitude > maxSpeed) {
			velocity = velocity.normalized;
			velocity *= maxSpeed;
		}
		acceleration *= 0;
	}
}