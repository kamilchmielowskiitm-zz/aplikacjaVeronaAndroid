package com.example.sebastian.token;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import javax.net.ssl.SSLSessionContext;

public class AutoLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String password = sharedPreferences.getString("password","");

        if((name.toString().equals("shuwax") && password.toString().equals("356413.nokia123")) ||
                (name.toString().equals("W.Mazur") && password.toString().equals("veron@123")) ||
                (name.toString().equals("piekarski") && password.toString().equals("wpiekarski")) ||
                (name.toString().equals("matysiak") && password.toString().equals("amatysiak.veron")))
        {
            finish();
            startActivity(new Intent(this,MenuActivity.class));
        }else
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
