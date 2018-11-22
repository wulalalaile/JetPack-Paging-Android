package com.example.administrator.jetpacktodo.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.jetpacktodo.adapter.CustomeAdapter;
import com.example.administrator.jetpacktodo.adapter.DiffCallBack;
import com.example.administrator.jetpacktodo.adapter.RefreshStatus;
import com.example.administrator.jetpacktodo.repository.BaseRepository;
import com.example.administrator.jetpacktodo.repository.ServiceLocator;
import com.example.administrator.jetpacktodo.viewmodel.MainViewModel;
import com.example.administrator.jetpacktodo.R;
import com.example.administrator.jetpacktodo.model.Student;
import com.example.administrator.jetpacktodo.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    public static final String INTENT_KEY = "intent_type";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding viewDataBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_main);


        final MainViewModel viewModel = ViewModelProviders
                .of(this, new ViewModelProvider.Factory() {
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                        String type = getIntent().getStringExtra(INTENT_KEY);
                        BaseRepository repository = ServiceLocator.getRepository(MainActivity.this, type);
                        return (T) new MainViewModel(repository);
                    }
                }).get(MainViewModel.class);
        final CustomeAdapter customeAdapter = new CustomeAdapter(new DiffCallBack());
        viewDataBinding.setAdapter(customeAdapter);

        viewDataBinding.setModel(viewModel);

        viewModel.listLiveData.observe(this, new Observer<PagedList<Student>>() {
            @Override
            public void onChanged(@Nullable PagedList<Student> students) {
                customeAdapter.submitList(students);
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        viewModel.refreshStatus.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                swipeRefreshLayout.setRefreshing(integer == RefreshStatus.RE_STATUS_LOADING);
            }
        });
    }


}
