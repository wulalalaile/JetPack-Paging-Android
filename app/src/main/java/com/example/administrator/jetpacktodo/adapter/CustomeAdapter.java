package com.example.administrator.jetpacktodo.adapter;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.administrator.jetpacktodo.BR;
import com.example.administrator.jetpacktodo.R;
import com.example.administrator.jetpacktodo.model.Student;
import com.example.administrator.jetpacktodo.databinding.LayoutItemBinding;

import java.util.List;

public class CustomeAdapter extends PagedListAdapter<Student, CustomeAdapter.ViewHolder> {


    public CustomeAdapter(DiffCallBack diffCallBack) {
        super(diffCallBack);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutItemBinding viewDataBinding = DataBindingUtil.
                inflate(LayoutInflater.from(viewGroup.getContext())
                        , R.layout.layout_item, viewGroup, false);

        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, List<Object> payloads) {
        Log.d("zzr","onBind");
        if(!payloads.isEmpty()){
            Log.d("zzr","onBindViwHolder");
        }else {
            onBindViewHolder(viewHolder,i);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.viewDataBinding.setVariable(BR.student, getItem(i));
        viewHolder.viewDataBinding.setClickHandler(new MyHandler());
        viewHolder.viewDataBinding.executePendingBindings();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LayoutItemBinding viewDataBinding;

        public ViewHolder(LayoutItemBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.viewDataBinding = viewDataBinding;
        }
    }

}
