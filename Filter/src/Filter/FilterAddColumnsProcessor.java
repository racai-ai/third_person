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
	String[] columnValues;
	
	public FilterAddColumnsProcessor(String[] newColumns, String value, String[] columnValues) {
		this.newColumns=newColumns;
		this.value=value;
		this.columnValues=columnValues;
	}
	
	public void processDocument(CONLLUPReader in, CONLLUPWriter out) throws IOException {
		for(Sentence s=in.readSentence();s!=null;s=in.readSentence()) {
			for(Token t:s.getTokens()) {
				for(int i=0;i<newColumns.length;i++) {
					String c=newColumns[i];
					String v=value;
					if(i<columnValues.length && columnValues[i].length()>0) {
						v=columnValues[i];
						if(v.startsWith("$"))v=t.getByKey(v.substring(1));
					}
					t.setByKey(c, v);
				}
				
			}
			out.writeSentence(s);
		}
	}
	
}
