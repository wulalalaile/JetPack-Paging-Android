package com.example.administrator.jetpacktodo.repository;

import android.content.Context;

import com.example.administrator.jetpacktodo.repository.inDb.DbRespository;
import com.example.administrator.jetpacktodo.repository.inNetwork.Repository;

public class ServiceLocator {
    private static final int PAGE_SIZE = 20;

    public static BaseRepository getRepository(Context context, String type) {
        switch (type) {
            case BaseRepository.TYPE_DB:
                return new DbRespository(context, PAGE_SIZE);
            case BaseRepository.TYPE_NET:
                return new Repository(PAGE_SIZE);

        }
        return null;
    }
}
