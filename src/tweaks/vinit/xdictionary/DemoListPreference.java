package tweaks.vinit.xdictionary;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListAdapter;

public class DemoListPreference extends ListPreference {

	public DemoListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	protected void onPrepareDialogBuilder(Builder builder) {
		ListAdapter listAdapter = new ModeArrayAdapter(getContext(), android.R.layout.select_dialog_singlechoice, getEntries());
		builder.setAdapter(listAdapter, this);
		super.onPrepareDialogBuilder(builder);
	}
}
