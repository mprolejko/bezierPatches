package patches;

import javax.vecmath.Point3d;

public class Grain {
	private Point3d point;
	private int id;					public int getId(){return id;}
	private double strength;
	private static int idNr = 0;
	
	public Grain(Point3d p, double s){
		point = p;
		strength = s;
		id = ++idNr;
	}
	
}
