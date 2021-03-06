package com.research.njit.njitresportandroidapplication;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText ucidLogin;
    EditText passwordLogin;
    OkHttpClient client = new OkHttpClient();
    OkHttpConnection conn = new OkHttpConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = conn.getNewHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ucidLogin = (EditText) findViewById(R.id.ucid_login);
        passwordLogin = (EditText) findViewById(R.id.editText2);

        if (readToken().equals("ERROR!") || readToken().equals("")) {
        }
        else {
            if (findRole(readToken()).equals("student")) {
                writeToken(readToken());
                Intent intent = new Intent(MainActivity.this, StudentProfile.class);
                intent.putExtra("Source", "from MainActivity");
                intent.putExtra("Token", readToken());
                finish();
                startActivity(intent);
            }
            else if (findRole(readToken()).equals("faculty")) {
                writeToken(readToken());
                Intent intent = new Intent(MainActivity.this, FacultyProfile.class);
                intent.putExtra("Source", "from MainActivity");
                intent.putExtra("Token", readToken());
                finish();
                startActivity(intent);
            }
        }
    }

    public void writeToken(String token) {
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
            if(responseJSON.length()==4)
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

    public String readToken() {
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

    public String post(String user, String pass)  {
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
            exception.printStackTrace();
            System.err.println("ERROR! UNABLE TO CONNECT TO API");
            exception.printStackTrace();
            System.err.println("STACK TRACE DONE");
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

    public void login(View view){
        String user = ucidLogin.getText().toString();
        String pass = passwordLogin.getText().toString();
        String tokenString = post(user,pass);
        System.out.println("--------------------------------------------------LOGIN--------------------------" + tokenString);
        if (!tokenString.equals("ERROR!")) {
            if (findRole(tokenString).equals("student")) {
                writeToken(tokenString);
                Intent intent = new Intent(MainActivity.this, StudentProfile.class);
                intent.putExtra("Source", "from MainActivity");
                intent.putExtra("Token", tokenString);
                finish();
                startActivity(intent);
            }
            else if (findRole(tokenString).equals("faculty")){
                writeToken(tokenString);
                Intent intent = new Intent(MainActivity.this, FacultyProfile.class);
                intent.putExtra("Source", "from MainActivity");
                finish();
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please enter valid credentials!", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please enter valid credentials!", Toast.LENGTH_LONG).show();
        }
    }
}