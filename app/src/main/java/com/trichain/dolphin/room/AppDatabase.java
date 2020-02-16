package com.trichain.dolphin.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.trichain.dolphin.entities.PeopleTable;
import com.trichain.dolphin.entities.TeamTable;

@Database(entities = {PeopleTable.class, TeamTable.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TeamDao teamDao();

    public abstract PeopleDao peopleDao();

}
