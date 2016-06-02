package com.example.johan.nfcreaderforunicard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Storage-Bytes for InterCard
    private final byte[] selectAid = {(byte)90, (byte)95, (byte)-124, (byte)21};      //select application command
    private final byte[] creditPayload = {(byte)108, (byte)1};                        //select credit file

    private byte[] resultOk;
    private byte[] creditBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
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
            float credit = (float)formatCreditToDouble(creditBytes);
            MensaMenuFragment m_frag = (MensaMenuFragment)getSupportFragmentManager().findFragmentById(R.id.MensaMenu);
            m_frag.updateMenuPositions(credit);
            TextView label = (TextView)findViewById(R.id.main_text);
            label.setText("Guthaben: "+round(credit, 2)+"€");
        }
    }

    private double formatCreditToDouble(byte[] array) {
        double credit = (double)(((0xff & array[4]) << 24) + ((0xff & array[3]) << 16) + ((0xff & array[2]) << 8) + (0xff & array[1])) / 1000D;
        return credit;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onStop(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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

    @Override
    public void onBackPressed(){
        finish();
    }
}
