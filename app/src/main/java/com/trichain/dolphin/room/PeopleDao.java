package com.trichain.dolphin.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.trichain.dolphin.entities.PeopleTable;

import java.util.List;

@Dao
public interface PeopleDao {
    @Query("SELECT * FROM PeopleTable")
    List<PeopleTable> getAllpeople();

    @Query("SELECT COUNT(id) FROM PeopleTable WHERE 1")
    int getNumberofEventPeople();

    @Query("SELECT * FROM PeopleTable WHERE 1")
    List<PeopleTable> getAllofEventPeople();

    @Insert
    void insert(PeopleTable peopleTable);

    @Delete
    void delete(PeopleTable peopleTable);

    @Update
    void update(PeopleTable peopleTable);
}
