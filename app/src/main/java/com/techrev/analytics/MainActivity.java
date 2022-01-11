package com.techrev.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btn_apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_apply = findViewById(R.id.applyAnalytics);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG , "Response from techrev analytics: "+Analytics.Companion.authenticateUser("5EEC00B5520C0D87706115E7695E9BA664B117E1C93B3BD6301BA874B8FC6F8F"));
            }
        });
    }
}