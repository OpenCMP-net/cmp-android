package com.example.inappcmp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.opencmp.inapplib.OpenCmp;
import com.opencmp.inapplib.OpenCmpContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.PopupWindow;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    OpenCmp openCmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        popupTest();

        OpenCmpContext cmpContext = new OpenCmpContext();
        cmpContext.domain="traffective.com";
        cmpContext.context = getApplicationContext();
//        cmpContext.view = findViewById(R.id.fab);
        cmpContext.activity = MainActivity.this;
        cmpContext.settingsButton = findViewById(R.id.opencmp_settings_button);
        cmpContext.clearButton = findViewById(R.id.opencmp_clear_button);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
            openCmp = OpenCmp.setup(cmpContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void popupTest() {
//        View parent = (View) findViewById(R.id.fab);
//
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        PopupWindow checkInPopup = new PopupWindow(getApplicationContext());
////                new PopupWindow(inflater.inflate(com.opencmp.inapplib.R.layout.opencmp_popup, null, false));
//        checkInPopup.setOutsideTouchable(true);
//        checkInPopup.setHeight(100);
//        checkInPopup.setWidth(200);
//        checkInPopup.setContentView(inflater.inflate(com.opencmp.inapplib.R.layout.opencmp_popup, null, false));
//
//
//        Activity activity = MainActivity.this;
////        activity.getWindow().getDecorView().getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
////            @Override
////            public void onWindowFocusChanged(boolean hasFocus) {
////                if(hasFocus){
////
////                }
////            }
////        });
//        checkInPopup.showAtLocation(parent, Gravity.CENTER_HORIZONTAL, 0, 0);
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus == false) {
            return;
        }
        try {
            openCmp.setWindowHasFocus(hasFocus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}