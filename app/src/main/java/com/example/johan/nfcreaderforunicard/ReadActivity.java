package com.example.johan.nfcreaderforunicard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.app.IntentService;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import java.io.IOException;

import android.os.Bundle;
/**
 * Created by johan on 10.05.2016.
 */
public class ReadActivity extends AppCompatActivity {

    // commands which needs to be send to the nfc tag
    private final byte[] selectAid = {(byte)90, (byte)95, (byte)-124, (byte)21};      //select application command
    private final byte[] creditPayload = {(byte)108, (byte)1};                        //select credit file

    // this are the responses of the nfc tag
    private byte[] resultOk;
    private byte[] creditBytes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_view);
        TextView label = (TextView) findViewById(R.id.text);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadCastReceiver, new IntentFilter("data"));
    }

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String credit = intent.getStringExtra("result");
        }
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
