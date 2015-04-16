package cis350.group6.fridgemanager;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


public class ShoppingListActivity extends ActionBarActivity {
    ListView list;
    ProgressDialog mProgressDialog;
    private List<shippingitem> shoppinglist = null;
    private EditText nametext, quantitytext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        nametext = (EditText)findViewById(R.id.namefield);
        quantitytext = (EditText)findViewById(R.id.quantity);

        new populateListView().execute();
        registerClickCallback();
    }
    private class populateListView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ShoppingListActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parse.com Custom ListView Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            shoppinglist = new ArrayList<shippingitem>();
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Shoppinglist");
                List<ParseObject> ob = query.find();
                for (ParseObject shoppingsearch : ob) {
                    // Locate images in flag column
                    String shoppingitem = (shoppingsearch.getString("name"));
                    int quantity = shoppingsearch.getInt("quantity");
                    shippingitem temp = new shippingitem();
                    temp.setname(shoppingitem);
                    temp.setquantity(quantity);
                    shoppinglist.add(temp);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            list = (ListView) findViewById(R.id.shoppinglist);
            // Pass the results into ListViewAdapter.java
            ListViewAdapter adapter = new ListViewAdapter(ShoppingListActivity.this,
                    shoppinglist);
            // Binds the Adapter to the ListView
            list.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
    private void registerClickCallback(){
        list = (ListView) findViewById(R.id.shoppinglist);
//        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
//                //ListviewItem textView = (shippingitem) viewClicked;
//                String message = "you clicked #" + position + ", we will remove all " + viewClicked.getText().toString();
//                Toast.makeText(ShoppingListActivity.this, message, Toast.LENGTH_LONG).show();
//                remove(textView.getText().toString());
//                return true;
//            }
//        });
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
//                TextView textView = (TextView) viewClicked;
//                String message = "you clicked #" + position + ", we will increment " + textView.getText().toString();
//                Toast.makeText(ShoppingListActivity.this, message, Toast.LENGTH_LONG).show();
//                increment(textView.getText().toString());
//            }
//        });
    }
    public static void remove(String name){
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shoppinglist");
            List<ParseObject>ob = query.find();
            int i =0;
            for (ParseObject shoppingsearch : ob) {
                // Locate images in flag column
                String shoppingitem = (shoppingsearch.getString("name"));
                if (shoppingitem.equals(name)){
                    shoppingsearch.deleteInBackground();
                }
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
    public void deletecheckeditems(){
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("fordeletion");
            List<ParseObject> ob = query.find();
            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Shoppinglist");
            List<ParseObject> ob2 = query.find();
            int i =0;
            for (ParseObject deletionsearch : ob) {
                String deletionitem = (deletionsearch.getString("name"));
                for (ParseObject shoppingsearch : ob2) {
                    // Locate images in flag column
                    String shoppingitem = (shoppingsearch.getString("name"));
                    if (shoppingitem.equals(deletionitem)){
                        remove(shoppingitem);
                        deletionsearch.deleteInBackground();
                    }
                }
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
    public static void increment(String name){
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shoppinglist");
            List<ParseObject> ob = query.find();
            int i =0;
            for (ParseObject shoppingsearch : ob) {
                // Locate images in flag column
                String shoppingitem = (shoppingsearch.getString("name"));
                if (shoppingitem.equals(name)){
                    shoppingsearch.increment("quantity");
                    shoppingsearch.saveInBackground();
                }
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
    public static void markfordeletion(String name){
        ParseObject newitem = new ParseObject("fordeletion");
        newitem.put("name", name);
        newitem.saveInBackground();
    }
    public static void unmarkfordeletion(String name){
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("fordeletion");
            List<ParseObject>ob = query.find();
            int i =0;
            for (ParseObject deletionsearch : ob) {
                // Locate images in flag column
                String deletionitem = (deletionsearch.getString("name"));
                if (deletionitem.equals(name)){
                    deletionsearch.deleteInBackground();
                }
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
    public static void decrement(String name){
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shoppinglist");
            List<ParseObject> ob = query.find();
            int i =0;
            for (ParseObject shoppingsearch : ob) {
                // Locate images in flag column
                String shoppingitem = (shoppingsearch.getString("name"));
                if (shoppingitem.equals(name)){
                    shoppingsearch.increment("quantity", -1);
                    shoppingsearch.saveInBackground();
                }
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
    public static void add(String name, int quantity){
        ParseObject newitem = new ParseObject("Shoppinglist");
        newitem.put("name", name);
        newitem.put("quantity", quantity);
        newitem.saveInBackground();
    }
    public void refresh(View v){
        List<shippingitem> shoppinglist = new ArrayList<shippingitem>();
        deletecheckeditems();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shoppinglist");
            List<ParseObject> ob = query.find();
            for (ParseObject shoppingsearch : ob) {
                // Locate images in flag column
                String shoppingitem = (shoppingsearch.getString("name"));
                int quantity = shoppingsearch.getInt("quantity");
                shippingitem temp = new shippingitem();
                temp.setname(shoppingitem);
                temp.setquantity(quantity);
                shoppinglist.add(temp);
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        ListView list = (ListView) findViewById(R.id.shoppinglist);
        // Pass the results into ListViewAdapter.java
        ListViewAdapter adapter = new ListViewAdapter(ShoppingListActivity.this,
                shoppinglist);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
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

    public void onReturnButtonClick(View v){
        finish();
    }
    public void onNewItemClick(View v){
        String name= "forgot to enter";
        try {name = nametext.getText().toString();}
        catch(Exception e){}
        int quantity;
        try {
            quantity = Integer.parseInt(quantitytext.getText().toString());
        }catch (Exception e){
            quantity=0;
        }
        add(name, quantity);
        nametext.setText("");
        quantitytext.setText("");
    }
    public static void addRecipe(Recipe list){
        String[] ingredients = list.getingredients();
        add(list.getName().trim() + ":", 0);
        for(int i=0; i<ingredients.length;i++){
            add("   " +ingredients[i], 1);
        }
        add("end of " + list.getName().trim(), 0);
    }
}
