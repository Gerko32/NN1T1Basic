package T1Base.Parser;

import java.util.HashSet;
import java.util.Set;

public class POS {
	private EntityType entityType;
	private Set<POSTag> posTags;
	private String phrase;
	public POS(String line, SentencePropertyMapper mapper) {
		String[] columns=line.split("\\t");
		if(!columns[1].equals("O"))this.entityType=mapper.getEntity(columns[1]);
		this.posTags=new HashSet<POSTag>();
		String[] tags=columns[4].split("/");
		for(String t : tags){
			this.posTags.add(mapper.getPOSTag(t));
		}
		this.phrase=columns[5];
	}
	public EntityType getEntityType() {
		return this.entityType;
	}
	public Set<POSTag> getPOSTags() {
		return this.posTags;
	}
	public String getPhrase() {
		return this.phrase;
	}
	@Override
	public String toString(){
		return this.phrase.toString();
	}
}
