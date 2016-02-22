package T1Base.Parser;

public class POSTag {
	private int id;
	private String type;
	
	public POSTag(int id, String type){
		this.id=id;
		this.type=type;
	}
	@Override
	public boolean equals(Object o){
		if(o==null) return false;
		if(!(o instanceof POSTag)) return false;
		return ((POSTag)o).id==this.id;
	}
	@Override
	public String toString(){
		return this.type;
	}
	public float getID() {
		return id;
	}
}
