package cis350.group6.fridgemanager;

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


public class ShoppingListActivity extends ActionBarActivity {

    //array of options --> arrayadapter -> listview

    //listview is going to be a set of views::items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        populateListView();
        registerClickCallback();
    }
    private void populateListView(){
        // create the list
        String[] myitems = {"blue", "green", "purple", "red"};
        //build the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.shoppingitem, myitems);
        //configure the list view
        ListView list = (ListView) findViewById(R.id.shoppinglist);
        list.setAdapter(adapter);
    }
    private void registerClickCallback(){
        ListView list = (ListView) findViewById(R.id.shoppinglist);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "you clicked #" + position + ", which is String: " + textView.getText().toString();
                Toast.makeText(ShoppingListActivity.this, message, Toast.LENGTH_LONG).show();
                return true;
            }
        });
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
