package com.example.administrator.jetpacktodo.repository.inDb;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.jetpacktodo.adapter.RefreshStatus;
import com.example.administrator.jetpacktodo.model.Student;
import com.example.administrator.jetpacktodo.repository.ApiKey;
import com.example.administrator.jetpacktodo.repository.PagingRequestHelper;
import com.example.administrator.jetpacktodo.repository.inNetwork.DataService;
import com.pingan.haofang.network.response.HttpResponseBody;
import com.pingan.haofang.network.server.AppServerRetrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageBoundaryCallBack extends PagedList.BoundaryCallback<Student> {


    private PagingRequestHelper helper;
    private Executor executor;
    private int pageSize;
    private Listener listener;

    public MutableLiveData<Integer> refreshStatus;

    public PageBoundaryCallBack(Executor executor, int pageSize, Listener listener) {
        this.executor = executor;
        helper = new PagingRequestHelper(executor);
        this.pageSize = pageSize;
        this.listener = listener;
        refreshStatus = new MutableLiveData<>();
    }

    @Override
    public void onZeroItemsLoaded() {
        refreshStatus.postValue(RefreshStatus.RE_STATUS_LOADING);
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL, new PagingRequestHelper.Request() {
            @Override
            public void run(final Callback callback) {
                getData(callback, 1);
            }
        });


    }

    @Override
    public void onItemAtEndLoaded(@NonNull final Student itemAtEnd) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER, new PagingRequestHelper.Request() {
            @Override
            public void run(Callback callback) {
                getData(callback, getPageId(itemAtEnd.getId()));
            }
        });
    }

    private int getPageId(int id) {
        int remainder = id % pageSize;
        int page = id / pageSize;
        return remainder > 0 ? page : page + 1;
    }


    private void getData(final PagingRequestHelper.Request.Callback callback, int page) {
        AppServerRetrofit.getInstance()
                .create(DataService.class)
                .getDataAsyn(ApiKey.QUERY, ApiKey.API_KEY, page, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new io.reactivex.Observer<HttpResponseBody<List<Student>>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final HttpResponseBody<List<Student>> listHttpResponseBody) {
                        if (listHttpResponseBody != null && listHttpResponseBody.articles != null) {

                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    if (listener != null) {
                                        listener.handleResponse(listHttpResponseBody.articles);
                                    }
                                    callback.recordSuccess();
                                }
                            });


                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.recordFailure(e);
                    }

                    @Override
                    public void onComplete() {
                        refreshStatus.postValue(RefreshStatus.RE_STATUS_LOADED);
                    }
                });
    }

    public interface Listener {
        void handleResponse(List<Student> list);
    }
}
