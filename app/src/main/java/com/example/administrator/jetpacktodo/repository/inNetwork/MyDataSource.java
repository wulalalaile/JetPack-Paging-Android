package com.example.administrator.jetpacktodo.repository.inNetwork;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.jetpacktodo.adapter.RefreshStatus;
import com.example.administrator.jetpacktodo.model.Student;
import com.example.administrator.jetpacktodo.repository.inNetwork.DataService;
import com.pingan.haofang.network.response.HttpResponseBody;
import com.pingan.haofang.network.server.AppServerRetrofit;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class MyDataSource extends PageKeyedDataSource<Integer, Student> {
    String QUERY = "movies";
    String API_KEY = "079dac74a5f94ebdb990ecf61c8854b7";

    public MutableLiveData<Integer> refreshStatus;


    public MyDataSource() {
        this.refreshStatus = new MutableLiveData<Integer>();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Student> callback) {
        refreshStatus.postValue(RefreshStatus.RE_STATUS_LOADING);
        DataService dataService = AppServerRetrofit.getInstance()
                .create(DataService.class);
        Call<HttpResponseBody<List<Student>>> call
                = dataService.getData(QUERY, API_KEY, 1, params.requestedLoadSize);

        try {
            HttpResponseBody<List<Student>> listHttpResponseBody = call.execute().body();
            if (listHttpResponseBody != null && listHttpResponseBody.articles != null) {
                callback.onResult(listHttpResponseBody.articles, 0, 1);
            }
            refreshStatus.postValue(RefreshStatus.RE_STATUS_LOADED);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Student> callback) {
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Student> callback) {
        AppServerRetrofit.getInstance()
                .create(DataService.class)
                .getDataAsyn(QUERY, API_KEY, params.key, params.requestedLoadSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new io.reactivex.Observer<HttpResponseBody<List<Student>>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResponseBody<List<Student>> listHttpResponseBody) {
                        if (listHttpResponseBody != null && listHttpResponseBody.articles != null) {
                            callback.onResult(listHttpResponseBody.articles, params.key + 1);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
