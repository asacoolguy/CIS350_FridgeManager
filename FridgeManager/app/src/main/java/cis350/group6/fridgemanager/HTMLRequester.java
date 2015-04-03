package cis350.group6.fridgemanager;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * Make HTML requests for querying a database
 *
 * Created by root on 4/2/15.
 */
public class HTMLRequester extends AsyncTask<String, String, String> {
    private static HTMLRequester instance = null;

    private HttpClient httpclient;

    protected HTMLRequester() {
        httpclient = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(String... URL) {
        return "Test";
    }

    public String HTMLRequest(String URL) throws IOException{
        HttpResponse response = httpclient.execute(new HttpGet(URL));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();
            return responseString;
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }

    public static HTMLRequester getInstance() {
        if (instance == null) {
            instance = new HTMLRequester();
        }
        return instance;
    }
}
