package rolex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class RoLex {

	private HashMap<String,String> formpos_lemma;
	private HashMap<String,String> lemmapos_form;
	
	public RoLex() {
		formpos_lemma=new HashMap<>();
		lemmapos_form=new HashMap<>();
	}
	
	public void load(Path fpath) throws IOException {
		BufferedReader in=new BufferedReader(new InputStreamReader(Files.newInputStream(fpath, StandardOpenOption.READ),Charset.forName("UTF-8")));
		for(String line=in.readLine();line!=null;line=in.readLine()) {
			String[] data=line.split("\t",-1);
			if(data.length!=6)continue;
			
			String form=data[0];
			String lemma=data[1]; if(lemma.contentEquals("="))lemma=form;
			String pos=data[2];
			
			if(pos.startsWith("V") || pos.startsWith("P")) {
				formpos_lemma.put(form+"_"+pos, lemma);
				lemmapos_form.put(lemma+"_"+pos, form);
			}
		}
		in.close();
	}
	
	public String getLemmaByFormAndPos(String form, String pos) {
		String k=form+"_"+pos;
		if(formpos_lemma.containsKey(k))return formpos_lemma.get(k);
		return null;
	}
	
	public String getFormByLemmaAndPos(String lemma, String pos) {
		String k=lemma+"_"+pos;
		if(lemmapos_form.containsKey(k))return lemmapos_form.get(k);
		return null;
	}
	
	public String transform(String form, String posFrom, String posTo) {
		String lemma=getLemmaByFormAndPos(form, posFrom);
		if(lemma==null)return form;
		
		String dst=getFormByLemmaAndPos(lemma, posTo);
		if(dst==null)return form;
		return dst;
	}
}
