package cis350.group6.fridgemanager;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Recipe {
    private String name;
    //    private Integer img;
    private String[] ingredients;
    private int numServings;
    private String totalTime;
    private Drawable smallImg;
    private Drawable largeImg;

    public Recipe(JSONObject json){
        try {
            name = json.getString("name");

            JSONArray arr = json.getJSONArray("ingredientLines");
            List<String> list = new ArrayList<String>();
            for(int i = 0; i < arr.length(); i++){
                list.add(arr.getString(i));
            }
            ingredients = list.toArray(new String[list.size()]);

            if(json.has("numberOfServings")){numServings = json.getInt("numberOfServings");}

            if(json.has("totalTime")){totalTime = json.getString("totalTime");}

            if(json.has("images")){
                JSONArray imgArray = json.getJSONArray("images");
                int length = imgArray.getJSONObject(0).length();
                if(imgArray.getJSONObject(0).has("hostedLargeUrl")){
                    String largeUrl = imgArray.getJSONObject(0).getString("hostedLargeUrl");
                    largeImg = new GetDrawableTask(this).execute(largeUrl).get();
                }
                if(imgArray.getJSONObject(0).has("hostedSmallUrl")){
                    String smallUrl = imgArray.getJSONObject(0).getString("hostedSmallUrl");
                    smallImg = new GetDrawableTask(this).execute(smallUrl).get();
                }
            }

        }
        catch(JSONException e){
            e.printStackTrace();
        }
        catch(ExecutionException e){
            e.printStackTrace();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

    }
    public String getName(){
        return name;
    }

    public Drawable getSmallImg(){
        return smallImg;
    }

    public Drawable getLargeImg(){
        return largeImg;
    }

    public String[] getIngredients(){
        return ingredients;
    }
    public String[] getingredients(){
        return ingredients;
    }
    public static Drawable drawableFromUrl(String url){
        Drawable drawable = null;

        try
        {
            URL drawURL = new URL(url);
            InputStream inputStream = drawURL.openStream();
            drawable = Drawable.createFromStream(inputStream, null);
            inputStream.close();
        }
        catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return drawable;
    }

    private class GetDrawableTask extends AsyncTask<String, Void, Drawable> {
        Recipe caller;

        GetDrawableTask(Recipe caller){
            this.caller = caller;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Drawable doInBackground(String... urls) {
            try {
                URL drawURL = new URL(urls[0]);
                InputStream inputStream = drawURL.openStream();
                Drawable img = Drawable.createFromStream(inputStream, null);
                inputStream.close();

                return img;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject result) {
        }
    }

}
