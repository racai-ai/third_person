package ThirdPerson;

import java.io.IOException;
import java.util.HashMap;

import ro.racai.base.Sentence;
import ro.racai.base.Token;
import ro.racai.conllup.CONLLUPReader;
import ro.racai.conllup.CONLLUPWriter;
import rolex.RoLex;

public class ThirdPersonProcessor {
	
	public HashMap<String,String> replacePron;
	public HashMap<String,String> replaceVerb;
	
	private RoLex rolex;
	
	public ThirdPersonProcessor(RoLex rolex) {
		this.rolex=rolex;
		
		replacePron=new HashMap<>();
		replacePron.put("eu", "el");
		replacePron.put("noi", "ei");
		replacePron.put("îmi", "îi");
		replacePron.put("mea", "sa");
		replacePron.put("mele", "sale");

		replaceVerb=new HashMap<>();
		replaceVerb.put("s_am", "a");
		replaceVerb.put("p_am", "au");

	}
	
	public String getTokenKey(Token t, String key1, String key2) {
		String s=t.getByKey(key1);
		if(s==null)s=t.getByKey(key2);
		return s;
	}

	public String matchCase(String f1, String f2) {
		if(f1.toLowerCase().contentEquals(f1))return f2.toLowerCase();
		if(f1.toUpperCase().contentEquals(f1))return f2.toUpperCase();
		String first=f1.substring(0, 1);
		if(first.toUpperCase().contentEquals(first))
			return f2.substring(0,1).toUpperCase()+f2.substring(1).toLowerCase();
		return f2;
	}
	
	public void processDocument(CONLLUPReader in, CONLLUPWriter out) throws IOException {
		for(Sentence s=in.readSentence();s!=null;s=in.readSentence()) {
			String last_pron="s";
			for(Token t:s.getTokens()) {
				String form=getTokenKey(t,"FORM","1");
				String lemma=getTokenKey(t,"LEMMA","2");
				String upos=getTokenKey(t,"UPOS","3");
				String xpos=getTokenKey(t,"XPOS","4");
				
				String kname="FORM";if(t.getByKey(kname)==null) kname="1";
				
				// Maybe set also XPOS ? => currently not
				
				if(upos.contentEquals("PRON")) {
					if(xpos.length()>4)last_pron=xpos.substring(4,5);
					if(replacePron.containsKey(form.toLowerCase())) {
						t.setByKey(kname, matchCase(form,replacePron.get(form.toLowerCase())));
					}else if(xpos.contains("1")) {
						t.setByKey(kname, matchCase(form, rolex.transform(form.toLowerCase(), xpos, xpos.replace('1', '3'))));
					}
				}else if(upos.contentEquals("VERB") || upos.contentEquals("AUX") || xpos.startsWith("V")) {
					// ROLEX
					String searchKey=last_pron+"_"+form.toLowerCase();
					if(replaceVerb.containsKey(searchKey)) {
						t.setByKey(kname, matchCase(form,replaceVerb.get(searchKey)));
					}else if(xpos.contains("1")) {
						t.setByKey(kname, matchCase(form, rolex.transform(form.toLowerCase(), xpos, xpos.replace('1', '3'))));
					}
				}
				
				
			}
			out.writeSentence(s);
		}
	}
	
}
