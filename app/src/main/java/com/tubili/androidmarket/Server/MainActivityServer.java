package com.tubili.androidmarket.Server;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tubili.androidmarket.R;

public class MainActivityServer extends AppCompatActivity implements View.OnClickListener {
    Button btnSignIn;
    TextView textSlogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_server);

        btnSignIn = findViewById(R.id.btnSignIn);

        textSlogan = findViewById(R.id.txtSlogan);

        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSignIn)
        {
            Intent intent = new Intent(MainActivityServer.this, SignInServer.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
