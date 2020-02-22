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

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.PeopleViewHolder> {

    private List<PeopleTable> peopleTableList;
    private Context context;

    public ResultsAdapter(List<PeopleTable> peopleTableList, Context context) {
        this.peopleTableList = peopleTableList;
        this.context = context;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeopleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_player_results, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        PeopleTable player = peopleTableList.get(position);
        holder.tvPlayerNameResults.setText(player.getName());
        holder.tvPlayerPointsResults.setText(String.valueOf(player.getScore()));
    }

    @Override
    public int getItemCount() {
        return peopleTableList.size();
    }

    class PeopleViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlayerNameResults,tvPlayerPointsResults;
        PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayerNameResults = itemView.findViewById(R.id.tvPlayerNameResults);
            tvPlayerPointsResults = itemView.findViewById(R.id.tvPlayerPointsResults);
        }
    }
}
