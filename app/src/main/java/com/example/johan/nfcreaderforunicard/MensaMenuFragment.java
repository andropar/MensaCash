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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by johan on 27.05.2016.
 */
public class MensaMenuFragment extends ListFragment {

    private static final String TAG = "MensaMenuFragment";

    List<String> data = new ArrayList<String>();
    List<String> priceList = new ArrayList<String>();

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

    public void updateMenuPositions(double credit){
        Log.v(TAG, "Combine lists");
        List<String> list = combineLists(data, priceList);
        Log.v(TAG, "Create new array adapter");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView text = (TextView) super.getView(position, convertView, parent);
                text.setBackgroundColor(Color.GREEN);
                return text;
            }
        };
        Log.v(TAG, "Set listadapter");
        setListAdapter(adapter);
    }

    private class MenuTask extends AsyncTask<List<String>, String, List<String>>{
        @Override
        protected List<String> doInBackground(List<String>... params){
            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if(netInfo != null && netInfo.isConnected()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();

                SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String loc = sPref.getString("mensa_setting", "106");

                String url = "http://www.studentenwerk-leipzig.de/mensa/menu?date=" +
                        dateFormat.format(date) + "&location=" + loc;
                Log.v(TAG, "URL:" + url);
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements menus = doc.getElementsByClass("menu");
                    for (Element menu : menus) {
                        if (menu.getElementsByClass("menu_price").hasText()) {
                            Elements food = menu.getElementsByClass("col_name");
                            Elements price = menu.getElementsByClass("menu_price");
                            String text = "";
                            for (Element foods : food) {
                                Elements e = foods.children();
                                for (Element singleFood : e) {
                                    text += singleFood.text() + "\n";
                                }
                            }
                            String[] prices = price.text().split("€");
                            prices[1] = prices[1].replace("/", "").trim();
                            prices[2] = prices[2].replace("/", "").trim();
                            int i = Integer.parseInt(sPref.getString("state_setting", "0"));
                            priceList.add(prices[i]);
                            data.add(text);
                        }
                    }
                    return data;
                } catch (Exception e) {
                    Log.v("MenuFragment", e.toString());

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> data){
            List<String> list = combineLists(data, priceList);
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
            setListAdapter(adapter);
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
