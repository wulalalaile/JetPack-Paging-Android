package com.example.administrator.jetpacktodo.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.administrator.jetpacktodo.model.Student;

import java.io.Serializable;
import java.util.Objects;

public class DiffCallBack extends DiffUtil.ItemCallback<Student> {


    @Override
    public boolean areItemsTheSame(@NonNull Student student, @NonNull Student t1) {
        return student.getTitle().equals(t1.getTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Student student, @NonNull Student t1) {
        return student == t1;
    }


}
