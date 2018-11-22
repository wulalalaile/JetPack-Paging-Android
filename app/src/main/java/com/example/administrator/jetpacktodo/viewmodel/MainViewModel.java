package com.example.administrator.jetpacktodo.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.util.Log;

import com.example.administrator.jetpacktodo.model.Student;
import com.example.administrator.jetpacktodo.repository.BaseRepository;
import com.example.administrator.jetpacktodo.repository.Listing;
import com.example.administrator.jetpacktodo.repository.inNetwork.Repository;

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> subredditName;
    private LiveData<Listing<Student>> result;
    public LiveData<PagedList<Student>> listLiveData;


    public LiveData<Integer> refreshStatus;

    public MainViewModel(final BaseRepository repository) {
        subredditName = new MutableLiveData<>();

        result = Transformations.map(subredditName, new Function<String, Listing<Student>>() {
            @Override
            public Listing<Student> apply(String input) {
                return repository.getData();
            }
        });
        subredditName.setValue("");
        listLiveData = Transformations.switchMap(result, new Function<Listing<Student>, LiveData<PagedList<Student>>>() {
            @Override
            public LiveData<PagedList<Student>> apply(Listing<Student> input) {
                return input.getPagedList();
            }
        });
        refreshStatus = Transformations.switchMap(result, new Function<Listing<Student>, LiveData<Integer>>() {
            @Override
            public LiveData<Integer> apply(Listing<Student> input) {

                return input.getRefreshStatus();
            }
        });
    }

    public void refresh() {
        result.getValue().refresh();
    }

}
