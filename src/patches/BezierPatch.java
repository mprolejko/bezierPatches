package patches;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;

public class BezierPatch{
	
	private static final double[] B_POLY = {1.0,3.0,3.0,1.0};
	private static final double getDeriv(int l, double t){
		switch(l){
			case 0:return -3*(1-t)*(1-t);
			case 1:return 3*(1-t)*(1-t) -6*t*(1-t);
			case 2:return 6*t*(1-t) - 3*t*t;
			case 3:return 3*t*t;
		}
		return 0;
	}
	
	public static int nrQuads=4;
	private Point3d[][] controls;
	private Point3d[] surface;
	
	private Shape3D patch; 	public Shape3D getPatch(){return patch;}
	
//	public BezierPatch(){
//		controls = new Point3d[4][4];
//		for(int i=0;i<4;i++)
//			for(int j=0;j<4;j++)
//				controls[i][j] = new Point3d(i,i,0);
//		
//	}
	

	public BezierPatch(Point3d[][] po){
		controls = po;
		
		patch = new Shape3D(makeGeometry(),createAppearance());
		patch.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		patch.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		
	}

	
	private Point3d calculatePoint(double u, double v){
		double x=0,y=0,z=0;
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++){
				double ex = B_POLY[i]*Math.pow(u, i)*Math.pow(1-u, 3-i)*B_POLY[j]*Math.pow(v, j)*Math.pow(1-v, 3-j);
				x+= ex*controls[i][j].x;
				y+= ex*controls[i][j].y;
				z+= ex*controls[i][j].z;
			}
		return new Point3d(x,y,z);
	}
	private Point3d evalBezierCurve(Point3d[] controls, double u){
		double x=0,y=0,z=0;
		for(int i=0;i<4;i++){
			double ex = B_POLY[i]*Math.pow(u, i)*Math.pow(1-u, 3-i);
			x+= ex*controls[i].x;
			y+= ex*controls[i].y;
			z+= ex*controls[i].z;
		}
		return new Point3d(x,y,z);
	}
	private Vector3f calculateNormal(double u, double v){
		Vector3f normal = new Vector3f();
		Vector3f du =  dUBezier(controls, u, v);
		Vector3f dv =  dVBezier(controls, u, v);
		normal.cross(du, dv);

		return normal;
	}
	
	Vector3f dUBezier(Point3d[][] controlPoints, double u, double v)
	{
		Point3d[] P = new Point3d[4];
		Point3d[] vCurve = new Point3d[4];
		for (int i = 0; i < 4; ++i) {
			P[0] = controlPoints[i][0];
			P[1] = controlPoints[i][1];
			P[2] = controlPoints[i][2];
			P[3] = controlPoints[i][3];
			vCurve[i] = evalBezierCurve(P, v);
		}
		float x,y,z;
		
		Vector3f normal = new Vector3f();	
		normal.x = (float)(getDeriv(0,u)*vCurve[0].x + getDeriv(1,u)*vCurve[1].x + getDeriv(2,u)*vCurve[2].x +	getDeriv(3,u)*vCurve[3].x);
		normal.y = (float)(getDeriv(0,u)*vCurve[0].y + getDeriv(1,u)*vCurve[1].y + getDeriv(2,u)*vCurve[2].y +	getDeriv(3,u)*vCurve[3].y);
		normal.z = (float)(getDeriv(0,u)*vCurve[0].z + getDeriv(1,u)*vCurve[1].z + getDeriv(2,u)*vCurve[2].z +	getDeriv(3,u)*vCurve[3].z);

		return normal;
	}

	Vector3f dVBezier(Point3d[][] controlPoints, double u, double v)
	{
		Point3d[] P = new Point3d[4];
		Point3d[] uCurve = new Point3d[4];
		for (int i = 0; i < 4; ++i) {
			P[0] = controlPoints[0][i];
			P[1] = controlPoints[1][i];
			P[2] = controlPoints[2][i];
			P[3] = controlPoints[3][i];
			uCurve[i] = evalBezierCurve(P, u);
		}
		float x,y,z;
		
		Vector3f normal = new Vector3f();	
		normal.x = (float)(getDeriv(0,v)*uCurve[0].x + getDeriv(1,v)*uCurve[1].x + getDeriv(2,v)*uCurve[2].x +	getDeriv(3,v)*uCurve[3].x);
		normal.y = (float)(getDeriv(0,v)*uCurve[0].y + getDeriv(1,v)*uCurve[1].y + getDeriv(2,v)*uCurve[2].y +	getDeriv(3,v)*uCurve[3].y);
		normal.z = (float)(getDeriv(0,v)*uCurve[0].z + getDeriv(1,v)*uCurve[1].z + getDeriv(2,v)*uCurve[2].z +	getDeriv(3,v)*uCurve[3].z);

		return normal;
	}	
	
	
	
	
	private GeometryArray makeGeometry(){
		int VERTEX_COUNT = (nrQuads+1)*(nrQuads+1);
		double[][] u = new double[nrQuads+1][nrQuads+1],
				   v = new double[nrQuads+1][nrQuads+1];
		
		surface = new Point3d[VERTEX_COUNT];
		Vector3f[] normals = new Vector3f[VERTEX_COUNT];
		
		// inicjacja wspó³rzêdnych dla których obliczymy punkty powierzchni
		for(int i=0;i<=nrQuads;i++)for(int j=0;j<=nrQuads;j++){
			u[i][j] = i/(double)nrQuads;
			v[i][j] = j/(double)nrQuads;	
			surface[i*(nrQuads+1)+j] = calculatePoint(u[i][j],v[i][j]);
			normals[i*(nrQuads+1)+j] = calculateNormal(u[i][j],v[i][j]);
		}
		
		IndexedTriangleArray geom = new IndexedTriangleArray(
				VERTEX_COUNT,
				GeometryArray.COORDINATES| GeometryArray.NORMALS,
				VERTEX_COUNT*6);
		geom.setCapability(GeometryArray.ALLOW_COORDINATE_READ|GeometryArray.ALLOW_COORDINATE_WRITE);
		int nq = nrQuads+1;
		
		geom.setCoordinates(0, surface);
		geom.setNormals(0, normals);
		int[] coordinateIndices = new int[VERTEX_COUNT*6];
		int[] normalIndices 	= new int[VERTEX_COUNT*6];

		for(int i=0;i<nrQuads;i++)for(int j=0;j<nrQuads;j++){
			int baseindex = (i*nq+j)*6;
			coordinateIndices[baseindex+0] =     i*nq+j;		// |
			coordinateIndices[baseindex+1] =     i*nq+j+1;		//  => pierwsza linia trójk¹tów
			coordinateIndices[baseindex+2] = (i+1)*nq+j;		// |
			coordinateIndices[baseindex+3] =     i*nq+j+1;			// |
			coordinateIndices[baseindex+4] = (i+1)*nq+j+1;			//  => druga linia trójk¹tów
			coordinateIndices[baseindex+5] = (i+1)*nq+j;			// |
		}
		for(int i=0;i<normalIndices.length;i++){	// skopiowalismy numery indeksow dla normalnych
			normalIndices[i] = coordinateIndices[i];
		}
		geom.setCoordinateIndices(0, coordinateIndices);
		geom.setNormalIndices(0, normalIndices);
		
		GeometryInfo geometryInfo = new GeometryInfo(geom);
		//NormalGenerator ng = new NormalGenerator();
		//geometryInfo.recomputeIndices();
		//ng.setCreaseAngle(Math.PI);
		//ng.generateNormals(geometryInfo);
		Vector3f[] a = geometryInfo.getNormals();
		System.out.println(a.length);
		return geometryInfo.getGeometryArray();
	}
	
	private Appearance createAppearance () {
		
		Appearance appearance = new Appearance();
		Material material = new Material(); 
		material.setDiffuseColor(1.0f, 0.0f, 0.0f);   // red
		material.setSpecularColor(0.2f, 0.2f, 0.2f);  // reduce default values
		appearance.setMaterial(material);
		
//		Appearance appearance = new Appearance();
//		Color3f color = new Color3f(0.76f,0.33f,0.0f);
//		Color3f dark = new Color3f(0.01f, 0.01f, 0.01f);
//		Color3f light = new Color3f(0.76f, 0.39f, 0.14f);
//		
		ColoringAttributes colAttr = new ColoringAttributes();
//		colAttr.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
		colAttr.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
		appearance.setColoringAttributes(colAttr);
//		
//		Material mat = new Material(color, dark, color, light, 1f);
//		appearance.setMaterial(mat);
		makeDoubleSided(appearance);
		
		return appearance;
	}
	
	public static void makeDoubleSided( Appearance app )
	{
		PolygonAttributes pa = app.getPolygonAttributes( );
		if( pa == null )
		{
			pa = new PolygonAttributes( );
			app.setPolygonAttributes( pa );
		}
		pa.setCullFace( PolygonAttributes.CULL_NONE );
		pa.setBackFaceNormalFlip( true );
	}	
}
