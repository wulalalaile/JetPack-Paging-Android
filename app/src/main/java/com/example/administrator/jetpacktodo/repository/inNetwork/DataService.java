package com.example.administrator.jetpacktodo.repository.inNetwork;

import com.example.administrator.jetpacktodo.model.Student;
import com.pingan.haofang.network.response.HttpResponseBody;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataService {

    @GET("v2/everything")
    Call<HttpResponseBody<List<Student>>> getData(
            @Query("q") String q,
            @Query("apiKey") String apiKey,
            @Query("page") long page,
            @Query("pageSize") int pageSize

    );
    @GET("v2/everything")
    Observable<HttpResponseBody<List<Student>>> getDataAsyn(
            @Query("q") String q,
            @Query("apiKey") String apiKey,
            @Query("page") long page,
            @Query("pageSize") int pageSize

    );
}
