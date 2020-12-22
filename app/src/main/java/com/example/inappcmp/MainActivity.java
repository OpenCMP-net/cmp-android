package com.example.inappcmp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_open).setOnClickListener(v -> openNewActivity());
    }

    private void openNewActivity() {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }
}