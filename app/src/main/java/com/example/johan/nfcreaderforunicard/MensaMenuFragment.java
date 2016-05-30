package com.example.johan.nfcreaderforunicard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        List<String> data = new ArrayList<String>();

        @Override
        protected List<String> doInBackground(List<String>... params){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();

            SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String loc = sPref.getString("mensa_key", "106");

            String url = "http://www.studentenwerk-leipzig.de/mensa/menu?date="+
                    dateFormat.format(date)+loc;
            try{
                Document doc = Jsoup.connect(url).get();
                Elements menus = doc.getElementsByClass("menu");
                for(Element menu: menus){
                    if(menu.getElementsByClass("menu_price").hasText()){
                        Elements food = menu.getElementsByClass("col_name");
                        Elements price = menu.getElementsByClass("menu_price");
                        String text = "";
                        for(Element foods: food){
                            Elements e = foods.children();
                            for(Element singleFood: e){
                                text += singleFood.text()+"\n";
                            }
                        }
                        String[] prices = price.text().split("€");
                        text += prices[0] + "€" + prices[1] + "€" + prices[2] + "€";
                        data.add(text);
                    }
                }
                return data;
            }catch(Exception e){
                Log.v("MenuFragment", e.toString());

            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> data){
            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, data);
            setListAdapter(adapter);
        }
    }


}
