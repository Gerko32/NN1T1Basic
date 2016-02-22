package T1Base.Parser;

import java.util.ArrayList;
import java.util.List;

import T1Base.Application.ESentenceInstance;

public class Sentence {
	private List<POS> parts;
	public Sentence(List<String> linesInFile, int startSentence, int endSentence, SentencePropertyMapper mapper) {
		this.parts=new ArrayList<POS>();
		for(int i=startSentence; i<endSentence;i++){
			this.parts.add(new POS(linesInFile.get(i), mapper));
		}
	}
	
	public List<ESentenceInstance> getEInstances(){
		List<ESentenceInstance> result=new ArrayList<ESentenceInstance>();
		for(int i=0;i<parts.size();i++){
			if(this.parts.get(i).getEntityType()!=null){
				result.add(new ESentenceInstance(this, i));
			}
		}
		return result;
	}

	public POS getPart(int index) {
		return this.parts.get(index);
	}

	public int size() {
		return this.parts.size();
	}
}
