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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.util.List;

/**
 * Created by David on 4/10/2015.
 */
public class RegisterActivity extends ActionBarActivity {

    private EditText usernameText, passwordText, emailText, confirmPasswordText,
            firstNameText, lastNameText;
    private HTMLRequester htmlRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = (EditText) findViewById(R.id.Username);
//        firstNameText = (EditText) findViewById(R.id.FirstName);
//        lastNameText = (EditText) findViewById(R.id.FirstName);
        passwordText = (EditText)findViewById(R.id.RegisterPassword);
        confirmPasswordText = (EditText)findViewById(R.id.ConfirmPassword);
        emailText = (EditText)findViewById(R.id.LoginEmail);
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
     * Register button event handler. Makes the login request.
     * @param v
     */
    public void onRegisterButtonClick(View v) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();
//        String firstName = firstNameText.getText().toString();
//        String lastName = lastNameText.getText().toString();
        String username = usernameText.getText().toString();

//        boolean isValidated = validateRegistration(firstName, lastName, email,
//                password, confirmPassword);
        boolean isValidated = validateRegistration(email, password, confirmPassword);
        if (isValidated) {
            addUserToDatabase(username, email, password);
        }
    }

    /**
     * Verifies if a new user can be created.
     *
     * @param email
     * @param password
     * @return
     */
    private boolean validateRegistration(String email, String password, String confirmPassword) {
        //Passwords do not match
        if (!password.equals(confirmPassword)) {
            makeToast("Passwords do not match");
            return false;
        }
        if (!(email.length() > 0 && password.length() > 0 && confirmPassword.length() > 0)) {
            makeToast("Some fields are missing information");
            return false;
        }

        return true;
    }

    private void addUserToDatabase(String username, String email, String password) {
        ParseUser parseUser = new ParseUser();
        parseUser.setEmail(username);
        parseUser.setUsername(email);
        parseUser.setPassword(password);
        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Show a simple Toast message upon successful registration
                    Toast.makeText(getApplicationContext(),
                            "Successfully Signed up, please log in.",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.d("regis", e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Sign up Error", Toast.LENGTH_LONG)
                            .show();

                }
            }
        });
    }

    public void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onReturnButtonClick(View v){
        finish();
    }

}
