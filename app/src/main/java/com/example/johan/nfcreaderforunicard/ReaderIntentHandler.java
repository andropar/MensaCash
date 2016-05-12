package com.example.johan.nfcreaderforunicard;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

/**
 * Created by johan on 11.05.2016.
 */
public class ReaderIntentHandler extends IntentService {

    // Storage-Bytes for InterCard
    private final byte[] selectAid = {(byte)90, (byte)95, (byte)-124, (byte)21};      //select application command
    private final byte[] creditPayload = {(byte)108, (byte)1};                        //select credit file

    private byte[] resultOk;
    private byte[] creditBytes;

    public ReaderIntentHandler(){
        super("ReaderIntentHandler");
    }

    @Override
    public void onHandleIntent(Intent intent){
        IsoDep isodep = IsoDep.get((Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
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
        String credit = formatCreditToText(creditBytes);
        Intent result = new Intent();
        result.putExtra("credit", credit);
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String toggleKey = getString(R.string.pref_toggle_key);
        Boolean toggleStatus = sPref.getBoolean(toggleKey, false);
        if(toggleStatus){
            result.setAction("com.example.johan.nfcreaderforunicard.TOGGLE_ON");
            startActivity(result);
        }else{
            result.setAction("com.example.johan.nfcreaderforunicard.TOGGLE_OFF");
            LocalBroadcastManager.getInstance(this).sendBroadcast(result);
        }
       }

    private String formatCreditToText(byte[] array) {
        double credit = (double)(((0xff & array[4]) << 24) + ((0xff & array[3]) << 16) + ((0xff & array[2]) << 8) + (0xff & array[1])) / 1000D;
        String creditStr = String.valueOf(credit);
        return creditStr;
    }
}
