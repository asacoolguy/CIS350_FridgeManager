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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class FridgeActivity extends ActionBarActivity {
    ArrayList<String> list;
    ArrayList<Food> items;
    ArrayAdapter adapter;
    Context context;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillFridge();
        setContentView(R.layout.activity_fridge);
        context = getApplicationContext();
        selected = -1; //nothing selected
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
        f.saveInBackground();
        DateTime expire = new DateTime(f.getExpireDate());
        DateTime now = new DateTime(new Date());
        int daysLeft = Days.daysBetween(now, expire).getDays();
        list.add(f.getName() + "    " + f.getQuantity() +  "    " + f.getUnits() + "     days left: " + daysLeft);
        items.add(f);
    }

    public void fillFridge(){
        ParseQuery<Food> fridge = ParseQuery.getQuery(Food.class);
        fridge.orderByAscending("expiresAt");
        fridge.findInBackground(new FindCallback<Food>() {
            @Override
            public void done(List<Food> results, ParseException e) {
                ListView listview = (ListView) findViewById(R.id.fridgeView);
                list = new ArrayList<String>();
                items = new ArrayList<Food>();

                // adds list of foods to result
                for (Food f : results) {
                    addItemToFridge(f);
                }

                Context context = getApplicationContext();
                adapter = new ArrayAdapter(context, R.layout.list_item_fridge, list);
                listview.setAdapter(adapter);
                listview.setSelector(R.drawable.selection_indicator);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                        selected = pos;
                        Log.d("hi", Integer.toString(pos));
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onRemoveItemClick(View v){
        if(selected >= 0){
            Food toRemove = items.get(selected);
            toRemove.deleteInBackground();
            list.remove(selected);
            adapter.notifyDataSetChanged();
        }
    }
    public void onPlusItemClick(View v){
        if(selected >= 0){
            Food toInc = items.get(selected);
            Log.d("hi", toInc.getName());
            int currentAmount = toInc.getQuantity().intValue();
            toInc.setQuantity(currentAmount+1);
            toInc.saveInBackground();
            DateTime expire = new DateTime(toInc.getExpireDate());
            DateTime now = new DateTime(new Date());
            int daysLeft = Days.daysBetween(now, expire).getDays();
            list.set(selected,toInc.getName() + "    " + toInc.getQuantity() +  "    " + toInc.getUnits() + "    " + daysLeft + " days left");
            adapter.notifyDataSetChanged();
        }
    }
    public void onMinusItemClick(View v){
        if(selected >= 0){
            Food toDec = items.get(selected);
            int currentAmount = toDec.getQuantity().intValue();
            toDec.setQuantity(currentAmount-1);
            toDec.saveInBackground();
            DateTime expire = new DateTime(toDec.getExpireDate());
            DateTime now = new DateTime(new Date());
            int daysLeft = Days.daysBetween(now, expire).getDays();
            list.set(selected,toDec.getName() + "    " + toDec.getQuantity() +  "    " + toDec.getUnits() + "    " + daysLeft + " days left");
            adapter.notifyDataSetChanged();
        }
    }

    public void onAddItemClick(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogLayout);
        final Dialog dialog = builder.create();

        //store item on done button click
        Button doneButton = (Button) dialogLayout.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName = (EditText) dialogLayout.findViewById(R.id.editName);
                String name = String.valueOf(editName.getText());
                EditText editAmount = (EditText) dialogLayout.findViewById(R.id.editAmount);
                String amount = String.valueOf(editAmount.getText());
                EditText editUnits = (EditText) dialogLayout.findViewById(R.id.editUnits);
                String units = String.valueOf(editUnits.getText());
                EditText editExpiration = (EditText) dialogLayout.findViewById(R.id.editExpiration);
                String expiration = String.valueOf(editExpiration.getText());
                CheckBox favoriteCheck = (CheckBox) dialogLayout.findViewById(R.id.favoriteCheckBox);
                Boolean favorite = favoriteCheck.isChecked();
                Food newAddition = new Food();
                // append * to name if the food is a favorite
                if (favorite) name = name + "*";
                newAddition.setName(name);
                newAddition.setQuantity(Integer.parseInt(amount));
                newAddition.setUnits(units);
                newAddition.setFavorite(favorite);
                if(name.length() == 0 || amount.length() == 0 || expiration.length() == 0){
                    CharSequence text = "Please fill all inputs!";
                    Toast error = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    error.show();
                    return;
                }
                else{
                    SimpleDateFormat  format = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        newAddition.setExpireDate(format.parse(expiration));
                        addItemToFridge(newAddition);
                        adapter.notifyDataSetChanged();
                    } catch (java.text.ParseException e) {
                        CharSequence text = "Invalid date format!";
                        Toast error = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                        error.show();
                        return;
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
