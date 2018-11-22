package com.example.administrator.jetpacktodo.repository.inDb;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.administrator.jetpacktodo.model.Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    void insert(Student user);

    @Insert
    void insertList(List<Student> users);

    @Query("DELETE FROM student_table ")
    void deleteAll();

    @Query("SELECT* from student_table ORDER BY id ASC ")
    List<Student> getAllUsers();

    @Query("SELECT*from student_table ORDER BY id ASC")
    LiveData<List<Student>> getAllLiveDataUsers();

    @Query("SELECT MAX(id) from student_table")
    int getNextIndex();

    @Query("SELECT*FROM student_table ORDER BY id ASC")
    DataSource.Factory<Integer, Student> getDataSourceFactory();
}
