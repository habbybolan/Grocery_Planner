package com.habbybolan.groceryplanner.di;

import android.app.Application;

import com.habbybolan.groceryplanner.di.component.AppComponent;
import com.habbybolan.groceryplanner.di.component.DaggerAppComponent;
import com.habbybolan.groceryplanner.di.module.AppModule;
import com.habbybolan.groceryplanner.di.module.RoomModule;
import com.habbybolan.groceryplanner.di.module.UserModule;

public class GroceryApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .userModule(new UserModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
