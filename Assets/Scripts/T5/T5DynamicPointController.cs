using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class T5DynamicPointController :T5MotionModel {


    void OnTriggerEnter(Collider other)
    {
        if (other.name.Contains("Wall(Clone)"))
        {
            collisionAvoidance += (location - other.GetComponent<Collider>().bounds.ClosestPoint(location));
            collisionAvoidance.y = 0;
        }

    }

    public override void seek(Vector3 target, Vector3 sum) 
	{
		if (targetWayPoint == wayPoints.Count-1 && isTargetReached()) {
			maxSpeed = 0;
		}
		Vector3 desired = target - location;
		Vector3 steer;
		if (desired.magnitude == 0) {
			return;
		}
		steer = desired - velocity;
        steer += collisionAvoidance;
        steer += sum;

		steer = steer.normalized;
		steer *= maxForce;
        applyForce(steer);

      /*  if (sum.magnitude > 0)
        {
            velocity += acceleration;
            velocity = velocity.normalized;
            velocity *= -(maxSpeed*0.005F);

        }
        else {*/
            velocity += acceleration;
        if(velocity.magnitude > maxSpeed) {
            velocity = velocity.normalized;

            velocity *= maxSpeed;
        }
    
        //}
		applyRotation (target, new Vector3());
		velocity = Quaternion.Euler (new Vector3(0,theta* Mathf.Rad2Deg,0))* velocity;
		acceleration *= 0;
        
    }
}