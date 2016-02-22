package T1Base.Parser;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ERRCorporaParser {
	private SentencePropertyMapper mapper;
	
	public ERRCorporaParser(SentencePropertyMapper mapper){
		this.mapper=mapper;
	}
	
	public List<Sentence> parse(String filePath) throws IOException{
		return parse(Files.readAllLines(FileSystems.getDefault().getPath(".", filePath)), this.mapper);
	}
	public List<Sentence> parse(List<String> linesInFile, SentencePropertyMapper mapper){
		List<Sentence> sentences=new ArrayList<Sentence>();
		int i=0;
		int tableEnd, relationEnd;
		while(i<linesInFile.size()){
			tableEnd=this.findIndexOfFirstEmptyLine(linesInFile, i);
			relationEnd=this.findIndexOfFirstEmptyLine(linesInFile, tableEnd+1);
			sentences.add(new Sentence(linesInFile, i, tableEnd, mapper));
			i=relationEnd+1;
		}
		return sentences;
	}
	
	private int findIndexOfFirstEmptyLine(List<String> linesInFile, int startIndex){
		int index=startIndex;
		while(!linesInFile.get(index).equals("")){
			index++;
		}
		return index;
	}
}
