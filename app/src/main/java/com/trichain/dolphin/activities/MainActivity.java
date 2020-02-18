package com.trichain.dolphin.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trichain.dolphin.R;
import com.trichain.dolphin.entities.PeopleTable;
import com.trichain.dolphin.model.Player;
import com.trichain.dolphin.room.DatabaseClient;
import com.trichain.dolphin.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddPlayer;
    Boolean new_gender_male,new_like_male;
    String TAG="MainActivity";
    List<PeopleTable> peopleTables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddPlayer = findViewById(R.id.btnAddplayer);
        btnAddPlayer.setOnClickListener(this);

        getPeople();


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

        final RadioButton  like_boy=dialogView.findViewById(R.id.like_boy);
        final RadioButton  like_girl=dialogView.findViewById(R.id.like_girl);
        final RadioButton  gender_male=dialogView.findViewById(R.id.gender_male);
        final RadioButton  gender_female=dialogView.findViewById(R.id.gender_female);
        like_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getResources().getDrawable( R.drawable.ic_check_box );
                Drawable img2 = getResources().getDrawable( R.drawable.ic_check_box_outline );
                like_boy.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img);
                like_girl.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img2);
                new_like_male=true;
            }
        });
        like_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getResources().getDrawable( R.drawable.ic_check_box );
                Drawable img2 = getResources().getDrawable( R.drawable.ic_check_box_outline );
                like_boy.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img2);
                like_girl.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img);
                new_like_male=false;
            }
        });
        gender_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getResources().getDrawable( R.drawable.ic_radio_button_checked );
                Drawable img2 = getResources().getDrawable( R.drawable.ic_radio_button_unchecked );
                gender_male.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img);
                gender_female.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img2);
                new_gender_male=true;
            }
        });
        gender_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img = getResources().getDrawable( R.drawable.ic_radio_button_checked );
                Drawable img2 = getResources().getDrawable( R.drawable.ic_radio_button_unchecked );
                gender_male.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img2);
                gender_female.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img);
                new_gender_male=false;
            }
        });
        builder.setView(dialogView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (new_like_male==null||new_gender_male==null){
                    Log.e(TAG, "onClick: something is nul" );
                }else{
                    int a,b;
                    if (new_like_male){
                        a=0;
                    }else {
                        a=1;
                    }
                    if (new_gender_male){
                        b=0;
                    }else{
                        b=1;
                    }
                    if (((EditText)dialogView.findViewById(R.id.tvPlayerName)).getText().toString().contentEquals("")){
                        Log.e(TAG, "onClick: empty text" );
                    }else {
                        String name=((EditText)dialogView.findViewById(R.id.tvPlayerName)).getText().toString();
                        PeopleTable peopleTable=new PeopleTable();
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
                for (int i = 0; i < peopleTables.size(); i++) {

                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlayersEntry);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
                            @NonNull
                            @Override
                            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.rec_player_entry, parent, false);
                                return new MyViewHolder(view);
                            }

                            @Override
                            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//                                Player player = players.get(position);
                                PeopleTable player=peopleTables.get(position);
                                holder.inputLayout.setText(player.getName());
                                if (player.getGender()==0){
                                    holder.male.setChecked(true);
                                    holder.female.setChecked(false);
                                }else {
                                    holder.male.setChecked(false);
                                    holder.female.setChecked(true);

                                }
                                if (player.getIntrestedIn()==0){
                                    holder.boys.setChecked(true);
                                    holder.girls.setChecked(false);
                                }else {
                                    holder.boys.setChecked(false);
                                    holder.girls.setChecked(true);
                                }
                            }

                            @Override
                            public int getItemCount() {
                                return peopleTables.size();
                            }
                        });
                        RecyclerItemClickListener recyclerItemClickListener=new RecyclerItemClickListener(MainActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Log.e(TAG, "onLongItemClick: "+position );
                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {
                                Log.e(TAG, "onLongItemClick: "+position );
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Do You want to delete?");
                                builder.setTitle("Caution");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PeopleTable pp=peopleTables.get(position);
                                        deletePeople(pp);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        });

                        recyclerView.addOnItemTouchListener(recyclerItemClickListener);
                    }
                });

            }
        }

        GetPeople gh = new GetPeople();
        gh.execute();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView inputLayout;
        RadioButton male,female;
        CheckBox boys,girls;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            inputLayout = itemView.findViewById(R.id.tvPlayerName);
            male = itemView.findViewById(R.id.male);
            female = itemView.findViewById(R.id.female);
            boys = itemView.findViewById(R.id.boys);
            girls = itemView.findViewById(R.id.girls);
        }
    }
}
