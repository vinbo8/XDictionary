package tweaks.vinit.xdictionary;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.os.Environment;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class DictSearch {

	static POS[] POS_ARR = { POS.NOUN, POS.VERB, POS.ADJECTIVE, POS.ADVERB };
	StringBuffer m;
	IDictionary dict;
	boolean created;
	
	public DictSearch() {
		created = true;
		m = new StringBuffer(5000);
		buildDict();
	}

	private void buildDict() {
		StringBuffer fpath = new StringBuffer(400);
		fpath.append(Environment.getExternalStorageDirectory().toString()
				+ "/xdict");
		
		File f = new File(fpath.toString());

		dict = new Dictionary(f);
		try {
			dict.open();
		} catch (IOException e) {
			e.printStackTrace();
			created = false;
		}
	}
	
	private String shorten(String s) {
		if(s.length() == 4) 
			return s.substring(0, 1);
		else
			return s.substring(0, 3);
	}
	
	public boolean exists() {
		return created;
	}

	public String getAllGlosses(String search_word) {

		int i = 1;
		for (POS p : POS_ARR) {
			IIndexWord idxWord = dict.getIndexWord(search_word, p);
			if (idxWord == null)
				continue;
			List<IWordID> wordIDs = idxWord.getWordIDs();
			for (IWordID wordID : wordIDs) {
				IWord iword = dict.getWord(wordID);
				m.append(String.format(Locale.getDefault(), "%d. (%s) %s\n",
						i, shorten(iword.getPOS().toString()), iword.getSynset()
						.getGloss()));
				++i;
			}
		}
		return m.toString();
	}

	public String getTopGlosses(String search_word) {

		int i = 1;
		for (POS p : POS_ARR) {
			IIndexWord idxWord = dict.getIndexWord(search_word, p);
			if (idxWord == null)
				continue;
			List<IWordID> wordIDs = idxWord.getWordIDs();
			IWordID wordID = wordIDs.get(0);
			IWord iword = dict.getWord(wordID);
			m.append(String.format(Locale.getDefault(), "%d. (%s) %s\n",
					i, shorten(iword.getPOS().toString()), iword.getSynset()
					.getGloss()));
			++i;
		}
		return m.toString();
	}

}
