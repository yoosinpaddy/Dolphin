package com.trichain.dolphin.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.trichain.dolphin.R;
import com.trichain.dolphin.entities.PeopleTable;
import com.trichain.dolphin.entities.QuestionTable;
import com.trichain.dolphin.room.DatabaseClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    List<PeopleTable> peopleTables;
    List<QuestionTable> questionTables;
    ArrayList<Integer> playerlist=new ArrayList<Integer>();
    ArrayList<String> questionList=new ArrayList<String>();
    ArrayList<Integer> peopleAlreadyPlayed=new ArrayList<Integer>();
    ArrayList<Integer> peopleplayingNow=new ArrayList<Integer>();
    String jsonQuestions;
    String TAG ="GameActivity";
    int currentQuize=1;
    int currentPlayer=1;

    int intLevel=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getPeople();
        jsonQuestions=loadJSONFromAsset();
//        showAddPlayerDialog("jj");
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton a, b;
    }

    private void showAddPlayerDialog(String gameQuestion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_challenge_options, null);
        TextView tvChallenge=dialogView.findViewById(R.id.tvChallenge);
        tvChallenge.setText(gameQuestion);
        Button accept = dialogView.findViewById(R.id.btnAccept);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        accept.setOnClickListener(v -> dialog.dismiss());


    }
    private void getNextQuestion(){
//        Collections.shuffle(questionList);
        if (currentQuize>questionList.size()){
            currentQuize=1;
        }
        QuestionTable prematureQuize=questionTables.get(currentQuize-1);
        String matureQuize=getMatureQuize(prematureQuize);
    }

    private String getMatureQuize(QuestionTable prematureQuize) {
        String finalQuize="";
        String preQuize=prematureQuize.getQuestions();
        int players=prematureQuize.getPlayers1();
        String[] gettingPlayers=preQuize.split("\\$\\$");
        if (players==1){
            Log.e(TAG, "getMatureQuize: one player" );
            String player1=getplayerWithID(playerlist.get(currentPlayer-1));
            finalQuize=gettingPlayers[0]+player1+gettingPlayers[2];
        }else if (players==2){
            Log.e(TAG, "getMatureQuize: two player" );
            String player1=getplayerWithID(playerlist.get(currentPlayer-1));
            String player2;
            finalQuize=gettingPlayers[0]+player1+gettingPlayers[2];
        }else if (players==3){
            Log.e(TAG, "getMatureQuize: three player" );
            String player1=getplayerWithID(playerlist.get(currentPlayer-1));
            finalQuize=gettingPlayers[0]+player1+gettingPlayers[2];
        }
        return finalQuize;
    }

    private String getplayerWithID(Integer integer) {
        return "";
    }

    private void prepareQuestions(){
        try {
            JSONObject jsonObject = new JSONObject(jsonQuestions);
//            JSONArray m_jArry = obj.getJSONArray("formules");

            Iterator<String> keys = jsonObject.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                if (jsonObject.get(key) instanceof JSONObject) {
                    // do something with jsonObject here
                    JSONObject oneQuize=new JSONObject(key);
                    String question=oneQuize.getString("question");
                    String level=oneQuize.getString("level");
                    String players=oneQuize.getString("players");
                    QuestionTable aa= new QuestionTable(question,players,level);
                    if (Integer.valueOf(level)==intLevel){
                        questionTables.add(aa);
                        Log.e(TAG, "prepareQuestions: question: level"+aa.getLevel1()+aa.getPlayers1() );
                    }
                }
            }
            getNextQuestion();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getFemaleplayer(){

    }
    private void getMaleplayer(){

    }
    private void getCompatibleplayer(){

    }
    private void getPeople() {
        class GetPeople extends AsyncTask<Void, Void, List<PeopleTable>> {

            @Override
            protected List<PeopleTable> doInBackground(Void... voids) {
                peopleTables= DatabaseClient
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
                    playerlist.add(peopleTables.get(i).getId());

                }
                if (questionTables.size()==0){
                    prepareQuestions();
                }else {
                    getNextQuestion();
                }
                getNextQuestion();
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
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("report.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
