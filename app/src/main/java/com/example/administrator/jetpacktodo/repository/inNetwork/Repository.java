package com.example.administrator.jetpacktodo.repository.inNetwork;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.administrator.jetpacktodo.model.Student;
import com.example.administrator.jetpacktodo.repository.Listing;
import com.example.administrator.jetpacktodo.repository.inNetwork.CustomPageDataSourceFactor;
import com.example.administrator.jetpacktodo.repository.inNetwork.MyDataSource;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository {
    public Listing<Student> getData() {
        PagedList.Config mConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setEnablePlaceholders(false)
                .build();

        final CustomPageDataSourceFactor sourceFactor = new CustomPageDataSourceFactor();
        Executor executor = Executors.newFixedThreadPool(5);
        ;
        final LiveData<PagedList<Student>> mPagedList =
                new LivePagedListBuilder(sourceFactor, mConfig)
                        .setFetchExecutor(executor)
                        .build();

        final LiveData<Integer> refreshState = Transformations
                .switchMap(sourceFactor.sourceLiveData, new Function<MyDataSource, LiveData<Integer>>() {
                    @Override
                    public LiveData<Integer> apply(MyDataSource input) {
                        return input.refreshStatus;
                    }
                });
        return new Listing<Student>() {
            @Override
            public void refresh() {
                if(sourceFactor.sourceLiveData.getValue()!=null){
                    sourceFactor.sourceLiveData.getValue().invalidate();
                }
            }

            @Override
            public LiveData<PagedList<Student>> getPagedList() {
                return mPagedList;
            }

            @Override
            public LiveData<Integer> getRefreshStatus() {
                return refreshState;
            }


        };
    }
}
