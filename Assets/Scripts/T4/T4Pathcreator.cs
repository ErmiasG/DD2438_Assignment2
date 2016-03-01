using UnityEngine;
using UnityEditor;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System;

[InitializeOnLoad]
public class T4Pathcreator : MonoBehaviour
{
    List<Vector3> pointLocations = new List<Vector3>();
    List<Vector3> carLocations = new List<Vector3>();
    private List<GameObject> walls;


    // Use this for initialization
    void Start ()
    {
     }

    // Update is called once per frame
    void Update() {
        UpdatePoints();
        //UpdateCars();
    }

    void UpdatePoints() {
        UpdatePointLocations();

        for (int i = 1; i <= 12; i++)
        {
            List<Vector3> forces = new List<Vector3>();
            Vector3 sum = new Vector3();
            
             for (int j=i; j<12; j++)
            {
                if (i-1 == j)
                    continue;
                if ((pointLocations[i-1] - pointLocations[j]).magnitude < 5)
                {
          
                    forces.Add((pointLocations[i - 1] - pointLocations[j]) * -1);
                    sum += (pointLocations[i - 1] - pointLocations[j]) * -(1-(pointLocations[i - 1] - pointLocations[j]).magnitude);
         
                }
            }
            
            GameObject.Find("DynamicPoint " + i).GetComponent<T5DynamicPointController>().manualUpdate(sum);
        }
    }

    private void UpdatePointLocations()
    {
        pointLocations.Clear();
        for (int i = 1; i <= 12; i++)
        {
            if (GameObject.Find("DynamicPoint " + i).GetComponent<T5DynamicPointController>().getVelocity().magnitude > 0)
            {
                pointLocations.Add(GameObject.Find("DynamicPoint " + i).GetComponent<T5DynamicPointController>().getLocation());
            }
            else {
                pointLocations.Add(new Vector3());
            }
        }
    }

    void UpdateCars()
    {
        carLocations.Clear();

        for (int i = 1; i <= 5; i++)
        {

            if (GameObject.Find("DynamicCar " + i).GetComponent<T5DynamicCarController>().getVelocity().magnitude > 0)
            {
                carLocations.Add(GameObject.Find("DynamicCar " + i).GetComponent<T5DynamicCarController>().getLocation());
            }
            else {
                carLocations.Add(new Vector3());
            }

        }
        for (int i = 1; i <= 5; i++)
        {
            List<Vector3> forces = new List<Vector3>();
            Vector3 sum = new Vector3();

            for (int j = i; j < 5; j++)
            //    for (int j = 0; j < 5; j++)
            {
                if (i - 1 == j)
                    continue;
                if ((carLocations[i - 1] - carLocations[j]).magnitude < 15)
                {

                    forces.Add((carLocations[i - 1] - carLocations[j]) * -1);
                    sum += (carLocations[i - 1] - carLocations[j]) * -(1 - (carLocations[i - 1] - carLocations[j]).magnitude);

                }
            }

            GameObject.Find("DynamicCar " + i).GetComponent<T5DynamicCarController>().manualUpdate(sum);
        }
    }
}
