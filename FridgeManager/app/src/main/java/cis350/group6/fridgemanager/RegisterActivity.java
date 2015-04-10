package cis350.group6.fridgemanager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.widget.*;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.util.List;

/**
 * Created by David on 4/10/2015.
 */
public class RegisterActivity extends ActionBarActivity {

    private EditText passwordText, emailText, confirmPasswordText, firstName, lastName;
    private HTMLRequester htmlRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.firstName);
        passwordText = (EditText)findViewById(R.id.loginPassword);
        confirmPasswordText = (EditText)findViewById(R.id.confirmPassword);
        emailText = (EditText)findViewById(R.id.loginEmail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    /**
     * Login button event handler. Makes the login request.
     * @param v
     */
    public void onLoginButtonClick(View v) {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        //String s = htmlRequester.doInBackground("http://api.reddit.com/r/all/search/?q=angular&after=t3_2xy59d&limit=10");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("Email", email);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + userList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        boolean isValidated = validateLoginCredentials(email, password);
        if (isValidated) {
            Intent i = new Intent(this,FridgeActivity.class);
            startActivityForResult(i, Constants.FridgeActivity_ID);
            Toast.makeText(getApplicationContext(), "Welcome Back!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Incorrect login ID or password", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validateLoginCredentials(String email, String password) {
        if (email.equals("test") && password.equals("1234")) {
            return true;
        } else {
            return false;
        }
    }

    public void debug(String msg) {
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onReturnButtonClick(View v){
        finish();
    }

}
