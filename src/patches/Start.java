package patches;


import java.awt.image.BufferedImage;
import javax.vecmath.*;

public class Start extends Thread {
	BufferedImage[] screens;

	private static final int years = 1;
	private static final int interval = 10;
	private static final boolean log = false;

	public Scene scene;
	public Model model;
	
	public static void main(String[] args){
		(new Start()).start();
	}
	
	public Start() {
		UIFrame frame = new UIFrame(600,600,"My scene");
		scene = new Scene(frame, true,false);
		//scene.makeOffscreanCanvas(1500, 2100);
		
		Point3d[][] anchors = new Point3d[4][4];
		for(int i=0;i<4;i++)for(int j=0;j<4;j++){
			anchors[i][j] = new Point3d(i,j,i*(3-i)*j*(6-j)/5.0);
		}
		
		model = new Model(anchors);
		scene.addHierarchy(model);

	}

	@Override
	public void run() {

//		screens = new BufferedImage[(365/interval +1)*(years +1)-1];
//		int i=0;
//
		while(true){
			model.update();
			scene.update();
//			screens[i++] = scene.getOffscreenCanvas();
			try {
				sleep(1000);// milisekundowa przerwa miêdzy klatkami
			} catch (InterruptedException e) {
			}
		}


	}


}
