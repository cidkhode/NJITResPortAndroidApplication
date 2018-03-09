package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText ucidLogin;
    EditText passwordLogin;
    TextView tempText;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempText = (TextView) findViewById(R.id.textView5);

        if (readToken().equals("ERROR!") || readToken().equals("")){
            tempText.setText("no token found. please login");
        }
        else {
            Intent intent = new Intent(MainActivity.this, StudentProfile.class);
            tempText.setText("token found: " + readToken());
            intent.putExtra("Source", "from MainActivity");
            startActivity(intent);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ucidLogin = (EditText) findViewById(R.id.editText);
        passwordLogin = (EditText) findViewById(R.id.editText2);

    }
    public void writeToken(String token)
    {
        File path = getApplicationContext().getFilesDir();
        File tokenFile = new File(path, "token.txt");
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(tokenFile, false);
            writer.write(token.getBytes());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTokenFromJSON(String response)  {
        JSONObject responseJSON = null;
        try {
            responseJSON = new JSONObject(response);
            String data;
            if(responseJSON.length() ==4)
            {
                data = responseJSON.getString("data");
                JSONObject dataJSON = new JSONObject(data);
                String token = dataJSON.getString("token");
                return token;
            }
            else
                return "ERROR!";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "ERROR!";
    }

    public String readToken()
    {
        File path = getApplicationContext().getFilesDir();
        File tokenFile = new File(path, "token.txt");

        int size = (int) tokenFile.length();
        byte[] data = new byte[size];
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(tokenFile);
            reader.read(data);
            reader.close();
            String token = new String(data);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR!";
    }

    public void clearToken() {
        File path = getApplicationContext().getFilesDir();
        File tokenFile = new File(path, "token.txt");
        PrintWriter clearer = null;
        try {
            clearer = new PrintWriter(tokenFile);
            clearer.write("");
            clearer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    String post(String user, String pass)  {
        String temp = "test";
        RequestBody formBody = new FormBody.Builder()
                .add("user", user)
                .add("pass", pass)
                .build();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/login")
                    .post(formBody)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            return (getTokenFromJSON(response.body().string()));
        } catch (IOException exception) {
        }
        return temp;
    }

    public String findRole(String token)
    {
        String body = token.split("\\.")[1];
        byte[] payload = Base64.decode(body, Base64.URL_SAFE);

        try {

            String json = new String(payload, "UTF-8");
            JSONObject jObj = new JSONObject(json);
            String role = jObj.getString("role");
            return role;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "ERROR!";
    }


    void login(View view){
        MainActivity example = new MainActivity();
        String user = ucidLogin.getText().toString();
        String pass = passwordLogin.getText().toString();
        String lol = example.post(user,pass);
        if(tempText.getText().equals("token found. press login to clear the token.")) {
            clearToken();
            tempText.setText("token cleared");
        }
        else {
            tempText.setText(findRole(lol));
            if (findRole(lol).equals("student")) {
                writeToken(lol);
                tempText.setText("token found for a student " + lol);
                Intent intent = new Intent(MainActivity.this, StudentProfile.class);
                intent.putExtra("Source", "from MainActivity");
                startActivity(intent);
            } else {
                tempText.setText("def");
            }
        }
    }

    public void goToStudentProfile(View view)
    {
        Intent intent = new Intent(MainActivity.this, StudentProfile.class);
        startActivity(intent);
    }

    public void goToFacultyProfile(View view)
    {
        Intent intent = new Intent(MainActivity.this, FacultyProfile.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    public void goToContactUs(View view)
    {
        Intent intent = new Intent(MainActivity.this, ContactUs.class);
        startActivity(intent);
    }

}