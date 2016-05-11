package com.example.johan.nfcreaderforunicard;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

/**
 * Created by johan on 11.05.2016.
 */
public class ReaderService extends IntentService {

    // Storage-Bytes for InterCard
    private final byte[] selectAid = {(byte)90, (byte)95, (byte)-124, (byte)21};      //select application command
    private final byte[] creditPayload = {(byte)108, (byte)1};                        //select credit file

    private byte[] resultOk;
    private byte[] creditBytes;

    public ReaderService(){
        super("ReaderService");
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
        Intent result = new Intent("data");
        result.putExtra("result", credit);
        LocalBroadcastManager.getInstance(this).sendBroadcast(result);
       }

    private String formatCreditToText(byte[] array) {
        double credit = (double)(((0xff & array[4]) << 24) + ((0xff & array[3]) << 16) + ((0xff & array[2]) << 8) + (0xff & array[1])) / 1000D;
        String creditStr = String.valueOf(credit);
        String formattedCredit;
        if(credit < 1.90) formattedCredit = "Arme Sau!:\n" + creditStr + "€";
        else if(credit >= 1.90 && credit < 2.45) formattedCredit = "Nudeltime!:\n" + creditStr + "€";
        else if(credit >= 2.45 && credit < 3.30) formattedCredit = "Freie Wahl!:\n" + creditStr + "€";
        else formattedCredit = "Reicher Sack!:\n" + creditStr + "€";
        return formattedCredit;
    }
}
