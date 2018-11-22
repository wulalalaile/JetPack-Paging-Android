package com.example.administrator.jetpacktodo.repository.inDb;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;

import com.example.administrator.jetpacktodo.adapter.RefreshStatus;
import com.example.administrator.jetpacktodo.model.Student;
import com.example.administrator.jetpacktodo.repository.ApiKey;
import com.example.administrator.jetpacktodo.repository.BaseRepository;
import com.example.administrator.jetpacktodo.repository.Listing;
import com.example.administrator.jetpacktodo.repository.inNetwork.DataService;
import com.pingan.haofang.network.response.HttpResponseBody;
import com.pingan.haofang.network.server.AppServerRetrofit;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class DbRespository extends BaseRepository implements PageBoundaryCallBack.Listener {
    private AppDatabase database;
    private StudentDao studentDao;
    private Context context;
    private int pageSize;
    private Executor executor;

    public DbRespository(Context context, int pageSize) {
        this.context = context.getApplicationContext();
        database = AppDatabase.getDataBase(context);
        studentDao = database.studentDao();
        this.pageSize = pageSize;
        executor = Executors.newSingleThreadExecutor();

    }

    public Listing<Student> getData() {
        final PageBoundaryCallBack callBack = new PageBoundaryCallBack(executor, pageSize, this);
        DataSource.Factory<Integer, Student> factory = database.studentDao().getDataSourceFactory();
        PagedList.Config mConfig = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setPrefetchDistance(pageSize)
                .setEnablePlaceholders(true)
                .build();
        final LiveData<PagedList<Student>> listLiveData = new LivePagedListBuilder(factory, mConfig)
                .setBoundaryCallback(callBack)
                .build();

        final MutableLiveData<String> trigger = new MutableLiveData<>();

        final LiveData<Integer> refreshStatus = Transformations
                .switchMap(trigger, new Function<String, LiveData<Integer>>() {
                    @Override
                    public LiveData<Integer> apply(String input) {
                        return refresh();
                    }
                });
        return new Listing<Student>() {
            @Override
            public void refresh() {
                trigger.setValue("");
            }

            @Override
            public LiveData<PagedList<Student>> getPagedList() {
                return listLiveData;
            }

            @Override
            public LiveData<Integer> getRefreshStatus() {
                return refreshStatus;
            }
        };
    }

    @Override
    public void handleResponse(final List<Student> list) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                studentDao.insertList(list);
            }
        });
    }

    private LiveData<Integer> refresh() {
        final MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(RefreshStatus.RE_STATUS_LOADING);
        AppServerRetrofit.getInstance()
                .create(DataService.class)
                .getDataAsyn(ApiKey.QUERY, ApiKey.API_KEY, 1, pageSize)
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
                                    database.runInTransaction(new Runnable() {
                                        @Override
                                        public void run() {
                                            studentDao.deleteAll();
                                            handleResponse(listHttpResponseBody.articles);
                                        }
                                    });
                                }
                            });


                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mutableLiveData.postValue(RefreshStatus.RE_STATUS_LOADED);
                    }
                });
        return mutableLiveData;
    }

}
