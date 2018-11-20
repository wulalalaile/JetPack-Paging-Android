package com.example.administrator.jetpacktodo.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.jetpacktodo.model.Student;

public class MyHandler {
    public void onClick(View view, Student student) {
        if (student != null && !TextUtils.isEmpty(student.getDescription())) {
            Toast.makeText(view.getContext(), student.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }
}
