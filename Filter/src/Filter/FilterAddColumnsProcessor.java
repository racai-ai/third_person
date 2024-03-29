package Filter;

import java.io.IOException;
import java.util.HashMap;

import ro.racai.base.Sentence;
import ro.racai.base.Token;
import ro.racai.conllup.CONLLUPReader;
import ro.racai.conllup.CONLLUPWriter;

public class FilterAddColumnsProcessor {
	String[] newColumns;
	String value;
	
	public FilterAddColumnsProcessor(String[] newColumns, String value) {
		this.newColumns=newColumns;
		this.value=value;
	}
	
	public void processDocument(CONLLUPReader in, CONLLUPWriter out) throws IOException {
		for(Sentence s=in.readSentence();s!=null;s=in.readSentence()) {
			for(Token t:s.getTokens()) {
				for(String c:newColumns)t.setByKey(c, value);
				
			}
			out.writeSentence(s);
		}
	}
	
}
