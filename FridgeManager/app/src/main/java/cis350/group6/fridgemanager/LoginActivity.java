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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    private EditText passwordText, usernameText;
    private HTMLRequester htmlRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordText = (EditText)findViewById(R.id.LoginPassword);
        usernameText = (EditText)findViewById(R.id.LoginUsername);
        htmlRequester = HTMLRequester.getInstance();
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

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
//        query.whereEqualTo("Email", email);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(final List<ParseObject> userList, ParseException e) {
//                if (e == null) {
//                    Log.d("score", "Retrieved " + userList.size() + " scores");
//                    if (userList.size() > 0) {
//
//                    }
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//
//            }
//        });
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // If user exist and authenticated, send user to Welcome.class
                    Intent intent = new Intent(
                            LoginActivity.this,
                            FridgeActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),
                            "Successfully Logged in",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "No such user exist, please signup",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    private boolean validateLoginCredentials(String email, String password) {
//        if (email.equals("test") && password.equals("1234")) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onRegisterButtonClick(View v) {
        Log.d("Test", "onRegisterButtonClick");
        Intent i = new Intent(this,RegisterActivity.class);
        startActivityForResult(i, Constants.RegisterActivity_ID);
    }
    public void onReturnButtonClick(View v){
        finish();
    }


}
