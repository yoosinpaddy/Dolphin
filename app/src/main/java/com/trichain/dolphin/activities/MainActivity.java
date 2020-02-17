package com.trichain.dolphin.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trichain.dolphin.R;
import com.trichain.dolphin.model.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Player> players = new ArrayList<>();
    Button btnAddPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddPlayer = findViewById(R.id.btnAddplayer);
        btnAddPlayer.setOnClickListener(this);

        players.addAll(Arrays.asList(
                new Player("John Doe", 1, 0),
                new Player("Jane Mary", 2, 1),
                new Player("Haley Bernette", 0, 1),
                new Player("Larry King", 1, 0)
        ));

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlayersEntry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.rec_player_entry, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                Player player = players.get(position);
                holder.inputLayout.setText(player.getName());
            }

            @Override
            public int getItemCount() {
                return players.size();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddPlayer) {
            showAddPlayerDialog();
        }
    }

    private void showAddPlayerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.rec_player_add_entry, null);
        builder.setView(dialogView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Add new player and update recyclerView adapter
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView inputLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            inputLayout = itemView.findViewById(R.id.tvPlayerName);
        }
    }
}
