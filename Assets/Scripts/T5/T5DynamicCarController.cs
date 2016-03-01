using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class T5DynamicCarController : T5MotionModel
{

    
    public override void seek (Vector3 target, Vector3 sum)
	{
		if (targetWayPoint == wayPoints.Count-1 && isTargetReached() ) {
			maxSpeed = 0;
		}
		Vector3 desired = target - location;
		Vector3 steer;
		if (desired.magnitude == 0) {
			return;
		}
		steer = desired - velocity;
        steer += (sum * 1F);
        if (steer.magnitude > maxForce)
        {
            steer = steer.normalized;
            steer *= maxForce;

        }
		
		applyForce(steer);

		velocity += acceleration * Time.deltaTime;
        if (velocity.magnitude > maxSpeed)
        {
            velocity = velocity.normalized;
            velocity *= maxSpeed;
        }
           
        

		applyRotation (target, sum);
		//velocity = new Vector3 (Mathf.Sin (theta), 0, Mathf.Cos (theta)) * velocity.magnitude;
		velocity = Quaternion.Euler (new Vector3(0,theta* Mathf.Rad2Deg,0))* velocity;
		acceleration *= 0;
	}			
}