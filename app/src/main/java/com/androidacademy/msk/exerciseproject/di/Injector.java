package com.androidacademy.msk.exerciseproject.di;

import com.androidacademy.msk.exerciseproject.di.component.AppComponent;
import com.androidacademy.msk.exerciseproject.di.component.DaggerAppComponent;
import com.androidacademy.msk.exerciseproject.di.component.DbAndNetworkComponent;
import com.androidacademy.msk.exerciseproject.di.component.DbComponent;
import com.androidacademy.msk.exerciseproject.di.component.NewsItemComponent;
import com.androidacademy.msk.exerciseproject.di.module.AppModule;
import com.androidacademy.msk.exerciseproject.di.module.DbModule;
import com.androidacademy.msk.exerciseproject.di.module.NetworkModule;
import com.androidacademy.msk.exerciseproject.di.module.NewsItemIdModule;

import android.content.Context;


public class Injector {

    private static Injector instance;

    private static Context context;

    private final AppComponent appComponent;

    private final DbComponent dbComponent;

    private final DbAndNetworkComponent dbAndNetworkComponent;

    public static void init(Context appContext) {
        context = appContext;
    }

    private Injector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
        dbComponent = appComponent.plusDbComponent(new DbModule());

        dbAndNetworkComponent = dbComponent.plusNetworkComponent(new NetworkModule());
    }

    public static Injector getInstance() {
        if (context == null) {
            throw new RuntimeException("Injector isn't initialed. You mast call " +
                    "Injector.init() method before getInstance().");
        }
        if (instance == null) {
            instance = new Injector();
        }
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public DbAndNetworkComponent getDbAndNetworkComponent() {
        return dbAndNetworkComponent;
    }

    public NewsItemComponent getNewsItemComponent(int id) {
        return dbComponent.plusNewsItemComponent(new NewsItemIdModule(id));
    }

}
