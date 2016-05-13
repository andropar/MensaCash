package com.example.johan.nfcreaderforunicard;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by johan on 11.05.2016.
 */
public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String KEY_PREF_toggle = "toggleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_view);
        }
    }

    @Override
    public void onBackPressed(){
        Intent backToMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(backToMain);
        finish();
    }

    public void onSharedPreferenceChanged(SharedPreferences sPref, String key){
        if(key.equals("toggleKey")){
            boolean toggleState = sPref.getBoolean(key, true);
            PackageManager pkgMgr = getPackageManager();
            ComponentName comp = new ComponentName("com.example.johan.nfcreaderforunicard",
                    "com.example.johan.nfcreaderforunicard.ActivityAlias");
            if(toggleState){
                pkgMgr.setComponentEnabledSetting(comp, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }else{
                pkgMgr.setComponentEnabledSetting(comp, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }
    }


}
