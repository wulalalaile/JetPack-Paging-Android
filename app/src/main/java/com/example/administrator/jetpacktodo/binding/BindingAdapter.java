package com.example.administrator.jetpacktodo.binding;

import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.jetpacktodo.adapter.RefreshStatus;
import com.example.administrator.jetpacktodo.viewmodel.MainViewModel;

public class BindingAdapter {

    @android.databinding.BindingAdapter(value = "adapter")
    public static void setAdapter(RecyclerView recyclerView
            , RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @android.databinding.BindingAdapter(value = "refresh")
    public static void refresh(RecyclerView recyclerView, boolean isRefresh) {
        if (isRefresh && recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @android.databinding.BindingAdapter(value = "setRefresh")
    public static void setSwipeRefreshLayoutOnRefreshListener(SwipeRefreshLayout view,
                                                              final MainViewModel viewModel) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
    }

    @android.databinding.BindingAdapter(value = "setRefreshIng")
    public static void setRefreshIng(SwipeRefreshLayout view, ObservableField<Boolean> data) {
        view.setRefreshing(data.get());
    }


}
