package com.trichain.dolphin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.trichain.dolphin.entities.PeopleTable;
import com.trichain.dolphin.room.DatabaseClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPeople();
    }
    private void getPeople() {
        class GetPeople extends AsyncTask<Void, Void, List<PeopleTable>> {

            @Override
            protected List<PeopleTable> doInBackground(Void... voids) {
                List<PeopleTable> peopleTables = DatabaseClient
                        .getInstance(MainActivity.this)
                        .getAppDatabase()
                        .peopleDao()
                        .getAllofEventPeople();
                return peopleTables;
            }

            @Override
            protected void onPostExecute(List<PeopleTable> peopleTables) {
                super.onPostExecute(peopleTables);
                for (int i = 0; i < peopleTables.size(); i++) {

                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ((TextView) findViewById(R.id.names)).setText(people);
                    }
                });
//                RecyclerView recyclerView = root.findViewById(R.id.mainrecyclerview);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                HolidayAdapter holidayAdapter = new HolidayAdapter(holidays, getActivity());
//                recyclerView.setAdapter(holidayAdapter);
//
//                if (holidays.size() == 0) {
//                    showNoHolidaysLayout();
//                } else {
//                    hideNoHolidaysLayout();
//                }

            }
        }

        GetPeople gh = new GetPeople();
        gh.execute();
    }
}
