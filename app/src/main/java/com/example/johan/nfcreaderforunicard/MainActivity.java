package com.example.johan.nfcreaderforunicard;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public void onResume(){
        super.onResume();
        PackageManager pkgMgr = getPackageManager();
        ComponentName comp = new ComponentName("com.example.johan.nfcreaderforunicard",
                "com.example.johan.nfcreaderforunicard.ActivityAlias");
        pkgMgr.setComponentEnabledSetting(comp, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    @Override
    public void onStop(){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean toggleState = sPref.getBoolean("toggleKey", true);
        if(!toggleState) {
            PackageManager pkgMgr = getPackageManager();
            ComponentName comp = new ComponentName("com.example.johan.nfcreaderforunicard",
                    "com.example.johan.nfcreaderforunicard.ActivityAlias");
            pkgMgr.setComponentEnabledSetting(comp, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case(R.id.action_settings):
                Intent startSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(startSettings);
                return true;
            case(R.id.about):
                Intent startAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(startAbout);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
