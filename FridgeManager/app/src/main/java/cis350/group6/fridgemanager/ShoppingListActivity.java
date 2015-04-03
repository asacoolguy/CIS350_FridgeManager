package cis350.group6.fridgemanager;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    private List<String> shoppinglist = null;

    //array of options --> arrayadapter -> listview

    //listview is going to be a set of views::items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        add("hello", 3);
        add("there", 3);
        add("my", 3);
        add("name", 3);

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
            shoppinglist = new ArrayList<String>();
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Shoppinglist");
                ob = query.find();
                for (ParseObject shoppingsearch : ob) {
                    // Locate images in flag column
                    String shoppingitem = (shoppingsearch.getString("name"));
                    int quantity = shoppingsearch.getInt("quantity");
                    shoppinglist.add(shoppingitem);
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShoppingListActivity.this,
                    R.layout.shoppingitem,
                    shoppinglist);
            // Binds the Adapter to the ListView
            list.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
    private void registerClickCallback(){
        list = (ListView) findViewById(R.id.shoppinglist);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "you clicked #" + position + ", which is String: " + textView.getText().toString();
                Toast.makeText(ShoppingListActivity.this, message, Toast.LENGTH_LONG).show();
                remove(textView.getText().toString());
                return true;
            }
        });
    }
    public void remove(String name){
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shoppinglist");
            ob = query.find();
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
    public void add(String name, int quantity){
        ParseObject newitem = new ParseObject("Shoppinglist");
        newitem.put("name", name);
        newitem.put("quantity", quantity);
        newitem.saveInBackground();
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
}
