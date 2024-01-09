package com.techrev.videocall;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        if (it != null && it.hasExtra("API_BASE_URL")) {
            Constants.API_BASE_URL = it.getStringExtra("API_BASE_URL");
        }
        Intent intent = new Intent(this , PermissionActivity.class);
        intent.putExtras(it);
        startActivity(intent);
    }
}
