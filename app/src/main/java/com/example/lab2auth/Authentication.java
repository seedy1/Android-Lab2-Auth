package com.example.lab2auth;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Authentication extends AppCompatActivity {

    EditText login, password;
    String txtLogin, txtPassword;
    TextView result;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);


    }


    /*
     *
     new Thread() {
        public void run() {
          new URL("http://www.stackoverflow.com").getContent();
        }
      }.start();
     */

    public void auth(View view) {

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        result = (TextView)findViewById(R.id.result);

        String txtLogin = login.getText().toString();
//        Byte txtLogin = login.;
        String txtPass = password.getText().toString();
        new Thread(){
            @Override
            public void run() {

                URL url = null;
                try {
                    url = new URL("https://httpbin.org/basic-auth/bob/sympa");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    // adding auth headers
                    String userAndPassword = txtLogin+":"+txtPass; // text values from text fields
                    Log.i("USR", txtLogin);
                    Log.i("PWD", txtPass);
                    String basicAuth = "Basic "+ Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);
//                    String basicAuth = "Basic "+ Base64.encodeToString("bob:sympa".getBytes(), Base64.NO_WRAP);
                    urlConnection.setRequestProperty("Authorization", basicAuth);


                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        String s = readStream(in);
                        Log.i("JFL", s);

                        jsonObject = new JSONObject(s);
                        boolean res = jsonObject.getBoolean("authenticated");
//                        String usr = jsonObject.getString("user");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                result.setText(""+res);
                            }
                        });

//                        result.setText("My result here");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }.start();

    }

    private String readStream(InputStream in) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = in.read();
            while(i != -1) {
                bo.write(i);
                i = in.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

// method to get url info in String format
        private String readStream1(InputStream in) throws IOException {
                StringBuilder sb = new StringBuilder();
                BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
                for (String line = r.readLine(); line != null; line =r.readLine()){
                    sb.append(line);
                }
                in.close();
                return sb.toString();
        }
}