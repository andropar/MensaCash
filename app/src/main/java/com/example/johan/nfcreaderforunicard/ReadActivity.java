package com.example.johan.nfcreaderforunicard;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;

/**
 * Created by johan on 10.05.2016.
 */
public class ReadActivity extends AppCompatActivity {

    // Storage-Bytes for InterCard
    private final byte[] selectAid = {(byte)90, (byte)95, (byte)-124, (byte)21};      //select application command
    private final byte[] creditPayload = {(byte)108, (byte)1};                        //select credit file

    private byte[] resultOk;
    private byte[] creditBytes;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())){
            IsoDep isodep = IsoDep.get((Tag)getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG));
            if(isodep != null){
                try{
                    isodep.connect();
                    resultOk = isodep.transceive(selectAid);
                    if(resultOk[0] == 0){
                        creditBytes = isodep.transceive(creditPayload);
                    }
                }catch (IOException e){
                }
            }
        }
        float credit = (float)formatCredit(creditBytes);
        TextView label = (TextView)findViewById(R.id.read_text);
        label.setText("Dein Guthaben: "+String.valueOf(credit)+"€");
    }

    private double formatCredit(byte[] array) {
        double credit = (double)(((0xff & array[4]) << 24) + ((0xff & array[3]) << 16) + ((0xff & array[2]) << 8) + (0xff & array[1])) / 1000D;
        return credit;
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
                Intent startSettings = new Intent(ReadActivity.this, SettingsActivity.class);
                startActivity(startSettings);
                return true;
            case(R.id.about):
                Intent startAbout = new Intent(ReadActivity.this, AboutActivity.class);
                startActivity(startAbout);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed(){
        Intent backToMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(backToMain);
        finish();
    }
}
