package tweaks.vinit.xdictionary;

import android.content.Context;
import android.widget.Toast;

public class ToastDisp {

	String search_word, meaning_text;
	Context context;
	DictSearch dict;

	public ToastDisp(String search_word, Context context) {
		this.search_word = search_word;
		this.context = context;
		dict = new DictSearch();
	}	

	public void show()
	{
		if(!dict.exists())
		{
			Toast.makeText(context, "Dictionary files not found!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		meaning_text = dict.getTopGlosses(search_word);
		if(meaning_text.equals(""))
			Toast.makeText(context, "No definition found", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(context, meaning_text, Toast.LENGTH_LONG).show();
	}
}
