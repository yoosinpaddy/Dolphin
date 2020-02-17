package com.trichain.dolphin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.trichain.dolphin.R;
import com.trichain.dolphin.entities.PeopleTable;
import com.trichain.dolphin.room.DatabaseClient;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPeople();
        RadioGroup radioGroup=findViewById(R.id.radioGroup);
        RadioButton a,b;
    }
    private void getPeople() {
        class GetPeople extends AsyncTask<Void, Void, List<PeopleTable>> {

            @Override
            protected List<PeopleTable> doInBackground(Void... voids) {
                List<PeopleTable> peopleTables = DatabaseClient
                        .getInstance(GameActivity.this)
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
                GameActivity.this.runOnUiThread(new Runnable() {
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
