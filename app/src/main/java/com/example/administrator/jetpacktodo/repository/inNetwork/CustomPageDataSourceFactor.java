package com.example.administrator.jetpacktodo.repository.inNetwork;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.example.administrator.jetpacktodo.model.Student;

public class CustomPageDataSourceFactor extends DataSource.Factory<Integer, Student> {

    final MutableLiveData<MyDataSource> sourceLiveData = new MutableLiveData<>();

    @Override
    public DataSource<Integer, Student> create() {
        final MyDataSource source = new MyDataSource();
        sourceLiveData.postValue(source);
        return source;
    }
}
