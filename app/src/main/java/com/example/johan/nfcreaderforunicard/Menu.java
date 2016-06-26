package com.example.johan.nfcreaderforunicard;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johannes on 26.06.2016.
 */
public class Menu {
    private List<String> data;
    private List<String> priceList;

    public Menu(){
        this.data = new ArrayList<String>();
        this.priceList = new ArrayList<String>();
    }

    public void updateMenu(Document doc, int state) {
        Elements meals = doc.getElementsByClass("meals__summary");
        for (Element meal : meals) {
            if (meal.getElementsByClass("meals__price").hasText()) {
                Elements food = meal.getElementsByClass("meals__name");
                Elements price = meal.getElementsByClass("meals__price");
                String text = food.text() + "\n";
                String[] prices = price.text().split("€");
                prices[0] = prices[0].replace("Preise: ", "").trim();
                prices[1] = prices[1].replace("/", "").trim();
                prices[2] = prices[2].replace("/", "").trim();
                priceList.add(prices[state]);
                data.add(text);
            }
        }
    }

    public List<String> getData(){
        return this.data;
    }

    public List<String> getPriceList(){
        return this.priceList;
    }
}
