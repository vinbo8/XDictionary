package tweaks.vinit.xdictionary;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {

	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		PreferenceManager prefMgr = getPreferenceManager();
		
		prefMgr.setSharedPreferencesName("my_prefs");
		prefMgr.setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
		addPreferencesFromResource(R.layout.settings_list);
		
	
	}

}
