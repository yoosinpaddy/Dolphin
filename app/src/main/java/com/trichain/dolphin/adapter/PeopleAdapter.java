package com.trichain.dolphin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trichain.dolphin.R;
import com.trichain.dolphin.entities.PeopleTable;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private List<PeopleTable> peopleTableList;
    private Context context;

    public PeopleAdapter(List<PeopleTable> peopleTableList, Context context) {
        this.peopleTableList = peopleTableList;
        this.context = context;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeopleViewHolder(LayoutInflater.from(context).inflate(R.layout.rec_player_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        PeopleTable player = peopleTableList.get(position);
        holder.inputLayout.setText(player.getName());
        if (player.getGender() == 0) {
            holder.male.setChecked(true);
            holder.female.setChecked(false);
        } else {
            holder.male.setChecked(false);
            holder.female.setChecked(true);

        }
        if (player.getIntrestedIn() == 0) {
            holder.boys.setChecked(true);
            holder.girls.setChecked(false);
        } else if(player.getIntrestedIn() == 1){
            holder.boys.setChecked(false);
            holder.girls.setChecked(true);

        }else {
            holder.boys.setChecked(true);
            holder.girls.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return peopleTableList.size();
    }

    class PeopleViewHolder extends RecyclerView.ViewHolder {
        TextView inputLayout;
        RadioButton male, female;
        CheckBox boys, girls;

        PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            inputLayout = itemView.findViewById(R.id.tvPlayerName);
            male = itemView.findViewById(R.id.male);
            female = itemView.findViewById(R.id.female);
            boys = itemView.findViewById(R.id.boys);
            girls = itemView.findViewById(R.id.girls);
        }
    }
}
