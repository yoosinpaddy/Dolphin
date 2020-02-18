package com.trichain.dolphin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trichain.dolphin.R;
import com.trichain.dolphin.adapter.PeopleAdapter;
import com.trichain.dolphin.entities.PeopleTable;
import com.trichain.dolphin.room.DatabaseClient;
import com.trichain.dolphin.utils.RecyclerItemClickListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddPlayer;
    Boolean new_gender_male, new_like_male;
    boolean isShowingDialog = false;
    String TAG = "MainActivity";
    String mName = "";
    List<PeopleTable> peopleTables;
    private PeopleAdapter peopleAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddPlayer = findViewById(R.id.btnAddplayer);
        btnAddPlayer.setOnClickListener(this);




        getPeople();

        findViewById(R.id.floatingBtnPlayGame).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GameActivity.class)));
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddPlayer) {
            showAddPlayerDialog();
        }
    }

    private void showAddPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.rec_player_add_entry, null);

        final RadioButton like_boy = dialogView.findViewById(R.id.like_boy);
        final RadioButton like_girl = dialogView.findViewById(R.id.like_girl);
        final RadioButton gender_male = dialogView.findViewById(R.id.gender_male);
        final RadioButton gender_female = dialogView.findViewById(R.id.gender_female);
        like_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getResources().getDrawable(R.drawable.ic_check_box);
                Drawable img2 = getResources().getDrawable(R.drawable.ic_check_box_outline);
                like_boy.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
                like_girl.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img2);
                new_like_male = true;
            }
        });
        like_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getResources().getDrawable(R.drawable.ic_check_box);
                Drawable img2 = getResources().getDrawable(R.drawable.ic_check_box_outline);
                like_boy.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img2);
                like_girl.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
                new_like_male = false;
            }
        });
        gender_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getResources().getDrawable(R.drawable.ic_radio_button_checked);
                Drawable img2 = getResources().getDrawable(R.drawable.ic_radio_button_unchecked);
                gender_male.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
                gender_female.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img2);
                new_gender_male = true;
            }
        });
        gender_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getResources().getDrawable(R.drawable.ic_radio_button_checked);
                Drawable img2 = getResources().getDrawable(R.drawable.ic_radio_button_unchecked);
                gender_male.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img2);
                gender_female.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
                new_gender_male = false;
            }
        });
        builder.setView(dialogView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (new_like_male == null || new_gender_male == null) {
                    Log.e(TAG, "onClick: something is nul");
                } else {
                    int a, b;
                    if (new_like_male) {
                        a = 0;
                    } else {
                        a = 1;
                    }
                    if (new_gender_male) {
                        b = 0;
                    } else {
                        b = 1;
                    }
                    if (((EditText) dialogView.findViewById(R.id.tvPlayerName)).getText().toString().contentEquals("")) {
                        Log.e(TAG, "onClick: empty text");
                    } else {
                        String name = ((EditText) dialogView.findViewById(R.id.tvPlayerName)).getText().toString();
                        mName = name;
                        PeopleTable peopleTable = new PeopleTable();
                        peopleTable.setGender(b);
                        peopleTable.setIntrestedIn(a);
                        peopleTable.setName(name);
                        addPeople(peopleTable);
                        //TODO: Add new player and update recyclerView adapter
                    }
                }

            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void addPeople(final PeopleTable peopleTable) {
        class AddPeople extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(MainActivity.this)
                        .getAppDatabase()
                        .peopleDao()
                        .insert(peopleTable);
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getPeople();
                    }
                });

            }
        }

        AddPeople gh = new AddPeople();
        gh.execute();
    }

    private void deletePeople(final PeopleTable peopleTable) {
        class DelPeople extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(MainActivity.this)
                        .getAppDatabase()
                        .peopleDao()
                        .delete(peopleTable);
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getPeople();
                    }
                });

            }
        }

        DelPeople gh = new DelPeople();
        gh.execute();
    }

    private void getPeople() {

        class GetPeople extends AsyncTask<Void, Void, List<PeopleTable>> {

            @Override
            protected List<PeopleTable> doInBackground(Void... voids) {
                peopleTables = DatabaseClient
                        .getInstance(MainActivity.this)
                        .getAppDatabase()
                        .peopleDao()
                        .getAllofEventPeople();
                return peopleTables;
            }

            @Override
            protected void onPostExecute(final List<PeopleTable> peopleTables) {
                super.onPostExecute(peopleTables);
                //Init recyclerView and adapter
                peopleAdapter = new PeopleAdapter(peopleTables, MainActivity.this);
                peopleAdapter.notifyDataSetChanged();
                recyclerView = findViewById(R.id.recyclerViewPlayersEntry);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(peopleAdapter);
                RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener(MainActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.e(TAG, "onLongItemClick: " + position);
                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        Log.e(TAG, "onLongItemClick: " + position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Do You want to delete " + mName + " ?");
                        builder.setTitle("Caution");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PeopleTable pp = peopleTables.get(position+1);
                                deletePeople(pp);
                                isShowingDialog = false;
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isShowingDialog = false;
                            }
                        });

                        builder.setOnDismissListener(dialog -> {
                            isShowingDialog = false;
                            Log.d(TAG, "onLongItemClick: onDismiss " + isShowingDialog);
                        });

                        if (!isShowingDialog) {
                            builder.show();
                            isShowingDialog = true;
                        }

                    }
                });
                recyclerView.addOnItemTouchListener(recyclerItemClickListener);
            }
        }

        GetPeople gh = new GetPeople();
        gh.execute();
    }

}
