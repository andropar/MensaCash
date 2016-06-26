package com.example.johan.nfcreaderforunicard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by johan on 27.05.2016.
 */
public class MensaMenuFragment extends ListFragment {

    Menu todaysMenu = new Menu();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.menufragment_view, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        new MenuTask().execute();
    }

    private class MenuTask extends AsyncTask<List<String>, String, List<String>>{
        @Override
        protected List<String> doInBackground(List<String>... params){
            if(isConnected()){
                try {
                    SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    int state = Integer.parseInt(sPref.getString("state_setting", "0"));

                    Document doc = getMenuDoc();
                    todaysMenu.updateMenu(doc, state);
                    return todaysMenu.getData();

                } catch (Exception e) {
                    Log.v("MenuFragment", e.toString());
                }
            }
            return null;
        }

        private boolean isConnected(){
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(netInfo != null && netInfo.isConnected()){
                return true;
            }
            else return false;
        }

        private Document getMenuDoc() throws IOException{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            int state = Integer.parseInt(sPref.getString("state_setting", "0"));
            String loc = sPref.getString("mensa_setting", "106");

            String url = "https://www.studentenwerk-leipzig.de/mensen-cafeterien/speiseplan?location="+loc+
                    "&date="+dateFormat.format(date)+"&criteria=&meal_type=all";

            return Jsoup.connect(url).get();
        }

        @Override
        protected void onPostExecute(List<String> data){
            List<String> list = combineLists(data, todaysMenu.getPriceList());
            if(list != null){
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
                setListAdapter(adapter);
            }
        }

    }

    private List<String> combineLists(List<String> a, List<String> b){
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < b.size(); i++){
            list.add(a.get(i) + b.get(i) + "€");
        }
        return list;
    }
}
