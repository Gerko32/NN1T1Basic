package T1Base.Parser;

public class EntityType {
	private int id;
	private String type;
	
	public EntityType(int id, String type){
		this.id=id;
		this.type=type;
	}
	@Override
	public boolean equals(Object o){
		if(o==null) return false;
		if(!(o instanceof EntityType)) return false;
		return ((EntityType)o).id==this.id;
	}
	
	@Override
	public String toString(){
		return this.type;
	}
	public int getID() {
		return this.id;
	}
}
