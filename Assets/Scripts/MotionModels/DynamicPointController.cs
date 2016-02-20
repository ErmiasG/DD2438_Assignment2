using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class DynamicPointController :MotionModel {

	public override void seek(Vector3 target) 
	{
		Vector3 finish = wayPoints [wayPoints.Count - 1].position;
		float d = (finish - location).magnitude;
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

	void applyForce(Vector3 force) 
	{
		acceleration += force;
	}

}