package patches;

import javax.vecmath.Point3d;

public class Element {
	private static int idNr=0;
	private Core core;
	private int index;
	
	public Element(){
		core = new Core();
		index = ++idNr;
	}
	public Element(Point3d p, double s){
		this();
		core = new Core( new Grain(p,s));
	}
	public Element(Point3d p1, Point3d p2, double s){
		this();
		core = new Core( new Grain(p1,s), new Grain(p2,s));
	}
	public Element(Point3d p1, Point3d p2, double s1, double s2){
		this();
		core = new Core( new Grain(p1,s1), new Grain(p2,s2));
	}
	public Element(Point3d[] p, double s){
		this();
		Grain[] g = new Grain[p.length];
		for(int i=0;i<g.length;i++){
			g[i] = new Grain(p[i],s);
		}
		core = new Core(g);
	}
	public Element(Point3d[] p, double[] s)throws IllegalDimensionException{
		this();
		if(p.length != s.length) throw new IllegalDimensionException();
		
		Grain[] g = new Grain[p.length];
		for(int i=0;i<g.length;i++){
			g[i] = new Grain(p[i],s[i]);
		}
		core = new Core(g);
	}
	
	
	


	
	
	
	public void update(){}
}
