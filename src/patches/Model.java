package patches;


import java.util.HashMap;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Sphere;



public class Model extends TransformGroup{
	
	private HashMap<Integer,Element> elements = new HashMap<>();
	
	
	private BezierPatch shape;
	private Appearance appearance;
	
	public Model(Point3d[][] anchors){
		super();
		
		
		shape = new BezierPatch(anchors);
		appearance = createAppearance();
		//shape.setAppearance(appearance);
		addChild(shape.getPatch());
		
		
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++){
				Sphere sphere = new Sphere(0.05f,appearance);
				TransformGroup tg = new TransformGroup();
				Transform3D transform = new Transform3D();
				Vector3d vector = new Vector3d( anchors[i][j].x, anchors[i][j].y, anchors[i][j].z);
				transform.setTranslation(vector);
				tg.setTransform(transform);
				tg.addChild(sphere);
				addChild(tg);
			}
		
//		for(Point3d p : shape.surface){
//			Sphere sphere = new Sphere(0.01f);
//			TransformGroup tg = new TransformGroup();
//			Transform3D transform = new Transform3D();
//			Vector3d vector = new Vector3d( p.x, p.y, p.z);
//			transform.setTranslation(vector);
//			tg.setTransform(transform);
//			tg.addChild(sphere);
//			addChild(tg);
//		}
		
		//addChild(new BezierPatch(anchors));

		setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setCapability(Group.ALLOW_CHILDREN_EXTEND);
		setCapability(ALLOW_CHILDREN_WRITE);
		
	}

	public void transform(Transform3D t3d){
		   setTransform(t3d);
	}

	private Appearance createAppearance () {
		Appearance appearance = new Appearance();
		Color3f color = new Color3f(0.56f,0.53f,0.0f);
		//Color3f dark = new Color3f(0.5f, 0.22f, 0f);
		Color3f dark = new Color3f(0.1f, 0.1f, 0.1f);
		Color3f light = new Color3f(0.56f, 0.59f, 0.14f);
		
		
		Material mat = new Material(color, dark, color, light, 1f);
		appearance.setMaterial(mat);
		makeDoubleSided(appearance);
		
		return appearance;
	}
	
	public void makeDoubleSided( Appearance app )
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
	
	
//	public void bezierSurface(){
//		Appearance app = new Appearance(); 
//		BufferedImage bi = new BufferedImage(10,10,10);
//		TextureLoader loader=new TextureLoader(bi); 
//		ImageComponent2D image=loader.getImage(); 
//		Texture2D texture=new Texture2D(Texture.BASE_LEVEL,Texture.RGBA,image.getWidth(),image.getHeight()); 
//		texture.setImage(0, image); 
//		texture.setEnable(true); 
//		//set The magnification filter function and The minification filter function 
//		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR); 
//		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR); 
//		app.setTexture(texture); 
//
//	}


	
	
	
	public void update(){
		
		if(elements != null){
			for(Element e: elements.values()){
				if(e!= null){
					e.update();
				}
			}
		}
		
	}

	
}
