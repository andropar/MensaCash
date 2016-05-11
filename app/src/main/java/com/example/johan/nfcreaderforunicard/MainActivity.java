package com.example.johan.nfcreaderforunicard;

import android.content.ComponentName;
import android.content.Intent;
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
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case(R.id.action_settings):
                Intent startSettings = new Intent();
                startSettings.setComponent(new ComponentName("com.example.johan.nfcreaderforunicard", "com.example.johan.nfcreaderforunicard.SettingsActivity"));
                startActivity(startSettings);
                return true;
            case(R.id.about):
                Intent startAbout = new Intent();
                startAbout.setComponent(new ComponentName("com.example.johan.nfcreaderforunicard", "com.example.johan.nfcreaderforunicard.AboutActivity"));
                startActivity(startAbout);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}