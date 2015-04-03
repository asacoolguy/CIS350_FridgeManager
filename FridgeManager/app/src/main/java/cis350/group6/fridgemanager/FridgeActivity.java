package cis350.group6.fridgemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FridgeActivity extends ActionBarActivity {
    ArrayList<String> list;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillFridge();
        setContentView(R.layout.activity_fridge);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fridge, menu);
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

    public void addItemToFridge(Food f){
        DateTime expire = new DateTime(f.getExpireDate());
        DateTime now = new DateTime(new Date());
        int daysLeft = Days.daysBetween(now, expire).getDays();
        list.add(f.getName() + "    " + f.getQuantity() +  "    " + f.getUnits() + "    " + daysLeft + " days left");
    }

    public void fillFridge(){
        ParseQuery<Food> fridge = ParseQuery.getQuery(Food.class);
        fridge.findInBackground(new FindCallback<Food>() {
            @Override
            public void done(List<Food> results, ParseException e) {
                ListView listview = (ListView) findViewById(R.id.fridgeView);
                list = new ArrayList<String>();

                for (Food f : results) {
                    addItemToFridge(f);
                }

                Context context = getApplicationContext();
                adapter = new ArrayAdapter(context, R.layout.list_item_fridge, list);
                listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onAddItemClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogLayout);
        final Dialog dialog = builder.create();
        //store item on done button click
        /*
        editName = (EditText) dialogLayout.findViewById(R.id.editName);
        editAmount = (EditText) dialogLayout.findViewById(R.id.editAmount);
        editUnits = (EditText) dialogLayout.findViewById(R.id.editUnits);
        editExpiration = (EditText) dialogLayout.findViewById(R.id.editExpiration);*/
        Button doneButton = (Button) dialogLayout.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName = (EditText) dialogLayout.findViewById(R.id.editName);
                String name = String.valueOf(editName.getText());
                if(name.length() == 0){
                    Context context = getApplicationContext();
                    CharSequence text = "Must input name!";
                    Toast error = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    error.show();
                    return;
                }
                else{
                    EditText editAmount = (EditText) dialogLayout.findViewById(R.id.editAmount);
                    String amount = String.valueOf(editAmount.getText());
                    EditText editUnits = (EditText) dialogLayout.findViewById(R.id.editUnits);
                    String units = String.valueOf(editUnits.getText());
                    EditText editExpiration = (EditText) dialogLayout.findViewById(R.id.editExpiration);
                    String expiration = String.valueOf(editExpiration.getText());
                    Food newAddition = new Food();
                    newAddition.setName(name);
                    newAddition.setQuantity(Double.parseDouble(amount));
                    newAddition.setUnits(units);
                    SimpleDateFormat  format = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        newAddition.setExpireDate(format.parse(expiration));
                        addItemToFridge(newAddition);
                        adapter.notifyDataSetChanged();
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }
        });
        //close on cancel button click
        Button cancelButton = (Button) dialogLayout.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onReturnButtonClick(View v){
        finish();
    }
}
