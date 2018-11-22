package com.example.administrator.jetpacktodo.repository;

import com.example.administrator.jetpacktodo.model.Student;

public abstract class BaseRepository {
    public final static String TYPE_DB = "db";
    public final static String TYPE_NET = "net";

    public abstract Listing<Student> getData();

}
