package com.example.administrator.jetpacktodo.repository.inNetwork;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.databinding.ObservableBoolean;

import com.example.administrator.jetpacktodo.model.Student;

public class CustomPageDataSourceFactor extends DataSource.Factory<Integer, Student> {

    private ObservableBoolean refreshStatus;

    public CustomPageDataSourceFactor(ObservableBoolean refreshStatus) {
        this.refreshStatus = refreshStatus;
    }

    final MutableLiveData<MyDataSource> sourceLiveData = new MutableLiveData<>();

    @Override
    public DataSource<Integer, Student> create() {
        final MyDataSource source = new MyDataSource(refreshStatus);
        sourceLiveData.postValue(source);
        return source;
    }
}
