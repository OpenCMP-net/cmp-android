package com.example.inappcmp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        OpenCmpContext cmpContext = new OpenCmpContext();
//        cmpContext.domain="traffective.com";
//        cmpContext.context = getApplicationContext();
//        cmpContext.activity = MainActivity.this;
//        cmpContext.settingsButton = findViewById(R.id.opencmp_settings_button);
//        cmpContext.clearButton = findViewById(R.id.opencmp_clear_button);
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                WebView.setWebContentsDebuggingEnabled(true);
//            }
//            openCmp = OpenCmp.setup(cmpContext);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        if (hasFocus == false) {
//            return;
//        }
//        try {
//            openCmp.setWindowHasFocus(hasFocus);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}