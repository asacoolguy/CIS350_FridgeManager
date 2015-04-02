package cis350.group6.fridgemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.Parse;


public class MainActivity extends ActionBarActivity {
    public static final int LoginActivity_ID = 1;
    public static final int FridgeActivity_ID = 2;
    public static final int ShoppingListActivity_ID = 3;
    public static final int RecipeActivity_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "RPkBsLufAmYR4TCMTrcTX4Dgq0iSvaPRTvVMPxLB", "RZKk7RHWH2pNxCrDCp7bg73dOYWCMfOCwIXIDwtY");
        setContentView(R.layout.activity_main);
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

    public void onLoginButtonClick(View v){
        Intent i = new Intent(this,LoginActivity.class);

        startActivityForResult(i, LoginActivity_ID);
    }

    public void onFridgeButtonClick(View v){
        Intent i = new Intent(this,FridgeActivity.class);

        startActivityForResult(i, FridgeActivity_ID);
    }

    public void onShoppingButtonClick(View v){
        Intent i = new Intent(this,ShoppingListActivity.class);

        startActivityForResult(i, ShoppingListActivity_ID);
    }

    public void onRecipeButtonClick(View v){
        Intent i = new Intent(this,RecipeActivity.class);

        startActivityForResult(i, RecipeActivity_ID);
    }
}
