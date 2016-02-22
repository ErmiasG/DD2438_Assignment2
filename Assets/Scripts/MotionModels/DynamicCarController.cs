using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class DynamicCarController : MotionModel
{
	public override void seek (Vector3 target)
	{
		Vector3 desired = target - location;
		if (desired.magnitude == 0) {
			return;
		}
		Vector3 steer = desired - velocity;
		steer = steer.normalized;
		steer *= maxForce;
		applyForce(steer);
		velocity += acceleration * Time.fixedDeltaTime;
		velocity = velocity.normalized;
		velocity *= maxSpeed;
		applyRotation (target);
		//velocity = new Vector3 (Mathf.Sin (theta), 0, Mathf.Cos (theta)) * velocity.magnitude;
		velocity = Quaternion.AngleAxis(theta * Mathf.Rad2Deg , Vector3.up)* velocity;
		acceleration *= 0;
	}	
}