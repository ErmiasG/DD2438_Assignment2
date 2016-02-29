using UnityEngine;
using UnityEditor;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.IO;

[InitializeOnLoad]
public class Pathcreator : MonoBehaviour
{


    // Use this for initialization
    void Start ()
    {
       

    Transform waypoint = GameObject.Find("Way_point").transform;

        List<Transform> path;

        string line;

        StreamReader theReader;

        for (int i = 1; i <= 5; i++)
        {
            path = new List<Transform>();
            theReader = new StreamReader("Assets/Scripts/VRP/"+i+".txt", Encoding.Default);

            using (theReader)
            {
                do
                {
                    line = theReader.ReadLine();

                    if (line != null)
                    {

                        string[] entries = line.Split(' ');
                        if (entries.Length > 0)
                        {
                            Vector3 pos = new Vector3(float.Parse(entries[0]), 0, float.Parse(entries[1]));
                            Transform clone = Instantiate(waypoint, pos, transform.rotation) as Transform;
                            path.Add(clone);
                        }

                    }
                }
                while (line != null);
                theReader.Close();
            }

         //  ath.Reverse();
            GameObject.Find("DynamicPoint "+i).GetComponent<DynamicPoint1Controller>().setWayPoints(path);

        }
     }

    // Update is called once per frame
    void Update () {
	
	}
}
