package com.example.administrator.jetpacktodo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.jetpacktodo.R;
import com.example.administrator.jetpacktodo.repository.BaseRepository;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }


    public void onNetClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.INTENT_KEY, BaseRepository.TYPE_NET);
        startActivity(intent);

    }

    public void onDbClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.INTENT_KEY, BaseRepository.TYPE_DB);
        startActivity(intent);
    }
}
