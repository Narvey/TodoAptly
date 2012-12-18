/*
 * Chris Card
 * Nathan Harvey
 * 11/6/12
 * This class allows for preferences to be selected 
 */
package csci422.CandN.to_dolist;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity {

	private SharedPreferences prefs = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
	}

	@Override 
	public void onResume()
	{
		super.onResume();

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(onChange);
	}
	
	@Override 
	public void onPause()
	{
		super.onPause();
		prefs.unregisterOnSharedPreferenceChangeListener(onChange);
	}

	OnSharedPreferenceChangeListener onChange = new OnSharedPreferenceChangeListener()
	{
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
		{
			if("syncWcal".equals(key))
			{
				boolean cal = prefs.getBoolean(key, false);
				if (cal && !FileSync.getInstance().isSyncCal()) 
				{
					FileSync.getInstance().toggleCalSync();
				}
				else if (!cal && FileSync.getInstance().isSyncCal()) 
				{
					FileSync.getInstance().toggleCalSync();
				}				
			}
			else if("syncWfile".equals(key)){
				boolean cal = prefs.getBoolean(key, false);
				if (cal && !FileSync.getInstance().isSyncFile()) 
				{
					FileSync.getInstance().toggleSaveFile();
				}
				else if (!cal && FileSync.getInstance().isSyncFile()) 
				{
					FileSync.getInstance().toggleSaveFile();
				}
			}
		}
	};
}
