using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class DynamicPointController :MotionModel {

	public override void seek(Vector3 target) 
	{
		if (targetWayPoint == wayPoints.Count-1 && isTargetReached() && !movingFormation) {
			maxSpeed = 0;
		}
		Vector3 desired = target - location;
		Vector3 steer;
		if (desired.magnitude == 0) {
			return;
		}
		steer = desired - velocity;
		steer = steer.normalized;
		steer *= maxForce;
		applyForce(steer);
		velocity += acceleration;
		velocity = velocity.normalized;
		velocity *= maxSpeed;
		applyRotation (target);
		velocity = Quaternion.Euler (new Vector3(0,theta* Mathf.Rad2Deg,0))* velocity;
		acceleration *= 0;
        Debug.Log(velocity.magnitude);
	}
}