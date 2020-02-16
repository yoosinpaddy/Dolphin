package com.trichain.dolphin.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.trichain.dolphin.entities.TeamTable;

import java.util.List;

@Dao
public interface TeamDao {
    @Query("SELECT * FROM TeamTable")
    List<TeamTable> getAllHolidays();

    @Query("SELECT * FROM PeopleTable WHERE 1 limit 1")
    TeamTable getHolidayIdofplace();

    @Insert
    void insert(TeamTable teamTable);

    @Delete
    void delete(TeamTable teamTable);

    @Update
    void update(TeamTable teamTable);
}
