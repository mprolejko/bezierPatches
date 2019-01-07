package patches;

import java.util.HashMap;

public class Core {
	private HashMap<Integer,Grain> points = new HashMap<>();
	
	public Core(){
	}
	public Core(Grain[] po){
		for(Grain g : po)
			points.put(g.getId(), g);
	}
	public Core(Grain value){
		points.put(value.getId(), value);
	}
	public Core(Grain value1,Grain value2){
		points.put(value1.getId(), value1);
		points.put(value2.getId(), value2);
	}
	
}
