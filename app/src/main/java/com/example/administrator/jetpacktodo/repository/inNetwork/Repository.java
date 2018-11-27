package com.example.administrator.jetpacktodo.repository.inNetwork;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.databinding.ObservableBoolean;

import com.example.administrator.jetpacktodo.model.Student;
import com.example.administrator.jetpacktodo.repository.BaseRepository;
import com.example.administrator.jetpacktodo.repository.Listing;
import com.example.administrator.jetpacktodo.repository.inNetwork.CustomPageDataSourceFactor;
import com.example.administrator.jetpacktodo.repository.inNetwork.MyDataSource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository extends BaseRepository {
    private int pageSize;

    public Repository(int pageSize) {
        this.pageSize = pageSize;
    }

    public Listing<Student> getData(ObservableBoolean refreshStatus) {
        PagedList.Config mConfig = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 3)
                .setEnablePlaceholders(false)
                .build();

        final CustomPageDataSourceFactor sourceFactor = new CustomPageDataSourceFactor(refreshStatus);
        Executor executor = Executors.newFixedThreadPool(5);
        ;
        final LiveData<PagedList<Student>> mPagedList =
                new LivePagedListBuilder(sourceFactor, mConfig)
                        .setFetchExecutor(executor)
                        .build();

        return new Listing<Student>() {
            @Override
            public void refresh() {
                if (sourceFactor.sourceLiveData.getValue() != null) {
                    sourceFactor.sourceLiveData.getValue().invalidate();
                }
            }

            @Override
            public LiveData<PagedList<Student>> getPagedList() {
                return mPagedList;
            }



        };
    }
}
