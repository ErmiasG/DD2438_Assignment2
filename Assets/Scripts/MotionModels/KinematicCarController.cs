using UnityEngine;
using System.Collections;

public class KinematicCarController : MotionModel
{
	public override void seek(Vector3 target) 
	{
		if (targetWayPoint == wayPoints.Count-1 && isTargetReached()) {
			maxSpeed = 0;
		}
		Vector3 desired = target - location;
		if (desired.magnitude == 0) {
			return;
		}
		velocity += desired - velocity;
		velocity = velocity.normalized;
		velocity *= maxSpeed;
		applyRotation (target);
		//velocity = new Vector3 (Mathf.Sin (theta), 0, Mathf.Cos (theta)) * velocity.magnitude;
		velocity = Quaternion.AngleAxis(theta* Mathf.Rad2Deg , Vector3.up)* velocity;
	}
}