package cis350.group6.fridgemanager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class RecipeActivity extends ActionBarActivity {
    public static String debugTag = "btag";
    private static GridViewAdapter mAdapter = null;
    private ArrayList<String> listRecipeName;
    private ArrayList<Integer> listRecipeImg;
    private ArrayList<Recipe> recipeList;
    private static ArrayList<JSONObject> JSONrecipes = null;
    private GridView gridView;
    private Spinner cuisineFilterSpinner, courseFilterSpinner;
    private EditText searchEditText;
    private String filteringCuisine = "all";
    private String filteringCourse = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(JSONrecipes == null){
            JSONrecipes = new ArrayList<JSONObject>();
        }
        prepareList();

        if(mAdapter == null){
            // prepared arraylist and passed it to the Adapter class
            mAdapter = new GridViewAdapter(this,recipeList);
        }


        // Set custom adapter to gridview
        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(mAdapter);

        // Implement On Item click listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Toast.makeText(RecipeActivity.this, mAdapter.getRecipeName(position), Toast.LENGTH_SHORT).show();
//                ShoppingListActivity.addRecipe(mAdapter.getItem(position));
            }
        });

        // set the spinners
        cuisineFilterSpinner = (Spinner) findViewById(R.id.RecipeCuisineFilterSpinner);
        cuisineFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(id == 0){ filteringCuisine = "all cuisines"; }
                else if (id == 1){ filteringCuisine = "american"; }
                else if (id == 2){ filteringCuisine = "italian"; }
                else if (id == 3){ filteringCuisine = "chinese"; }
                else if (id == 4){ filteringCuisine = "japanese"; }
                else if (id == 5){ filteringCuisine = "thai"; }
                else if (id == 6){ filteringCuisine = "mexican"; }
                else if (id == 7){ filteringCuisine = "french"; }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });
        courseFilterSpinner = (Spinner) findViewById(R.id.RecipeCourseFilterSpinner);
        courseFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(id == 0){ filteringCourse = "all courses"; }
                else if (id == 1){ filteringCourse = "Main+Dishes"; }
                else if (id == 2){ filteringCourse = "Desserts"; }
                else if (id == 3){ filteringCourse = "Salads"; }
                else if (id == 4){ filteringCourse = "Breakfast+and+Brunch"; }
                else if (id == 5){ filteringCourse = "Soups"; }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        searchEditText = (EditText) findViewById(R.id.search_recipes_text);
//        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                boolean handled = false;
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//                    Toast.makeText(RecipeActivity.this, v.getText().toString(), Toast.LENGTH_SHORT).show();
//                    handled = true;
//                }
//                return handled;
//            }
//        });
    }

    public void prepareList()
    {
        JSONObject searchReturn;
        if(searchEditText == null || searchEditText.getText().toString().equals("")){
            searchReturn = searchRecipes("recipe",filteringCuisine, filteringCourse);
        }
        else{
            searchReturn = searchRecipes(searchEditText.getText().toString(),filteringCuisine, filteringCourse);
        }
        try {
            JSONArray returnedRecipes = searchReturn.getJSONArray("matches");
            int numRecipes = 10;
            recipeList = getRecipesFromJSON(returnedRecipes, numRecipes);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetRecipeTask extends AsyncTask<String, Void, JSONObject> {
        RecipeActivity caller;

        GetRecipeTask(RecipeActivity caller){
            this.caller = caller;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    return new JSONObject(data);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject result) {
            caller.onResponseReceived(result);
        }
    }

    public String getJSON(String address){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(address);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.d(debugTag, "Failed to get JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        Log.d(debugTag, builder.toString());
        return builder.toString();
    }


    public void onResponseReceived(JSONObject json){
        JSONrecipes.add(json);
    }


    private JSONObject searchRecipes(String query, String cuisine, String course){
        String executingURL = "http://api.yummly.com/v1/api/recipes?_app_id=01b9fa3b&_app_key=2eb9083c6cea006509068f9a5ee9bf97" +
                "&q=" + query;

        // change the search query if filtering by cuisine
        try{
            if (!cuisine.equals("all")){
                cuisine = "cuisine^cuisine-" + cuisine;
                executingURL = executingURL + "&allowedCuisine" + URLEncoder.encode("[]", "UTF-8") + "=" + URLEncoder.encode(cuisine, "UTF-8");
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        // change the search query if filtering by course
        try{
            //if (!course.equals("all")){
                course = "course^course-" + course;
                executingURL = executingURL + "&allowedCourse" + URLEncoder.encode("[]", "UTF-8") + "=" + URLEncoder.encode(course, "UTF-8");
            //}
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }



        try{
            JSONObject ans = new GetRecipeTask(this).execute(executingURL).get();
            return ans;
        }
        catch(ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getRecipe(String id){
        try{
            return new GetRecipeTask(this).execute("http://api.yummly.com/v1/api/recipe/" + id + "?_app_id=01b9fa3b&_app_key=2eb9083c6cea006509068f9a5ee9bf97").get();
        }
        catch(ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Recipe> getRecipesFromJSON(JSONArray jsonRecipes, int numRecipes){
        ArrayList<Recipe> parsedRecipes = new ArrayList<Recipe>();

        try {
            for (int i = 0; i < numRecipes; i++) {
                JSONObject jsonRecipe = jsonRecipes.getJSONObject(i);
                String recipeId = jsonRecipe.getString("id");
                JSONObject fullRecipeData = getRecipe(recipeId);

                parsedRecipes.add(new Recipe(fullRecipeData));
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return parsedRecipes;
    }

    // refreshes the gridview based on the new set of parameters
    public void refreshSearchResults(){
        if(JSONrecipes == null){
            JSONrecipes = new ArrayList<JSONObject>();
        }
        Toast.makeText(RecipeActivity.this, "searching...", Toast.LENGTH_SHORT).show();
        prepareList();

        // failed attempt that updating gridview. please fix
        if(mAdapter != null){
            mAdapter.setRecipes(recipeList);
            //mAdapter = new GridViewAdapter(this,recipeList);
            gridView.invalidateViews();
            gridView.setAdapter(mAdapter);
        }
    }

    public void onReturnButtonClick(View v){
        finish();
    }

    public void onSearchButtonClick(View v){
        refreshSearchResults();
    }
}
