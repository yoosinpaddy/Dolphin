package com.trichain.dolphin.room;

import android.content.Context;

import androidx.room.Room;

import static com.trichain.dolphin.constants.Constant.dbName;


public class DatabaseClient {

    private Context context;

    private static DatabaseClient databaseClient;

    //DB object
    private AppDatabase appDatabase;

    private DatabaseClient(Context context){
        this.context=context;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, dbName).build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (databaseClient == null) {
            databaseClient = new DatabaseClient(context);
        }
        return databaseClient;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
