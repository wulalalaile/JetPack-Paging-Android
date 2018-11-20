package com.example.administrator.jetpacktodo.repository;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.databinding.ObservableBoolean;

import com.example.administrator.jetpacktodo.adapter.RefreshStatus;


public interface Listing<T> {

    void refresh();

    LiveData<PagedList<T>> getPagedList();

    LiveData<Integer> getRefreshStatus();

}
