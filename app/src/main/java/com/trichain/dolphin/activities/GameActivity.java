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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    List<PeopleTable> peopleTables;
    List<QuestionTable> questionTables=null;

    ArrayList<Integer> playerlist=new ArrayList<Integer>();
    ArrayList<String> questionList=new ArrayList<String>();
    ArrayList<Integer> peopleAlreadyPlayed=new ArrayList<Integer>();
    ArrayList<Integer> peopleplayingNow=new ArrayList<Integer>();
    String jsonQuestions;
    String TAG ="GameActivity";
    int currentQuize=1;
    int currentPlayer=1;
    TextView tvChallengeMain;
    Button done;
    int activePlayerId;


    int intLevel=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tvChallengeMain=findViewById(R.id.tvChallengeMain);
        done=findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donePlayingOnegame(v);
            }
        });
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
        tvChallengeMain.setText(gameQuestion);
        accept.setOnClickListener(v -> dialog.dismiss());


    }
    private void donePlayingOnegame(View view){
        peopleplayingNow=new ArrayList<Integer>();
        addOnepoint(activePlayerId);
        getNextQuestion();
    }

    private void addOnepoint(int activePlayerId) {
        class GetPerson extends AsyncTask<Void, Void, PeopleTable> {

            @Override
            protected PeopleTable doInBackground(Void... voids) {
                PeopleTable person= DatabaseClient
                        .getInstance(GameActivity.this)
                        .getAppDatabase()
                        .peopleDao()
                        .getOnePerson(activePlayerId);
                person.setScore(person.getScore()+1);
                DatabaseClient
                        .getInstance(GameActivity.this)
                        .getAppDatabase()
                        .peopleDao()
                        .update(person);
                return person;
            }

            @Override
            protected void onPostExecute(PeopleTable person) {
                super.onPostExecute(person);
                Log.e(TAG, "onPostExecute: "+person.getScore() );
            }
        }

        GetPerson gh = new GetPerson();
        gh.execute();
    }

    private void getNextQuestion(){
//        Collections.shuffle(questionList);

        if (currentQuize>questionList.size()){
//            done
            currentQuize=1;
        }else if (currentQuize==1){
            //first time
        }else if (currentQuize>=2){
            currentQuize++;
        }
        QuestionTable prematureQuize=questionTables.get(currentQuize-1);
        String matureQuize=getMatureQuize(prematureQuize);
        showAddPlayerDialog(matureQuize);
    }

    private String getMatureQuize(QuestionTable prematureQuize) {
        String finalQuize="";
        String preQuize=prematureQuize.getQuestions();
        int players=prematureQuize.getPlayers1();
        String[] gettingPlayers=preQuize.split("\\$\\$");
        if (players==1){
            Log.e(TAG, "getMatureQuize: one player" );
            String player1=getPlayer1(gettingPlayers[1]);
            finalQuize=gettingPlayers[0]+player1+gettingPlayers[2];
        }else if (players==2){
            Log.e(TAG, "getMatureQuize: two player" );
            String player1=getPlayer1(gettingPlayers[1]);
            String player2 = getAnotherPlayer(gettingPlayers[3]);
            Log.e(TAG, "getMatureQuize: temp:player2"+player1+player2 );
            finalQuize=gettingPlayers[0]+player1+gettingPlayers[2]+player2+gettingPlayers[4];
        }else if (players==3) {
            Log.e(TAG, "getMatureQuize: three player");
            Log.e(TAG, "getMatureQuize: two player");
            String player1 = getPlayer1(gettingPlayers[1]);
            String player2 = getAnotherPlayer(gettingPlayers[3]);
            String player3 = getAnotherPlayer(gettingPlayers[5]);
            Log.e(TAG, "getMatureQuize: temp:player2" + player2 + player3);
            finalQuize = gettingPlayers[0] + player1 + gettingPlayers[2] + player2+gettingPlayers[4]+player3+gettingPlayers[6];
        }
        currentPlayer++;
        Log.e(TAG, "Final Quize: "+finalQuize );
        return finalQuize;
    }
    private String getPlayer1(String temp){
        Log.e(TAG, "getPlayer1: "+temp );
        int onlythistime=currentPlayer-1;
        String player1;
        if (temp.contentEquals("Player")){
            player1=getplayerWithID(playerlist.get(onlythistime));
            activePlayerId=playerlist.get(onlythistime);
        }else if (temp.contentEquals("PlayerMale")){
            int i=1;
            if (hasMale()){
                while (!isMale(playerlist.get(onlythistime))){
                    onlythistime+=i;
                }
            }else{
                while (isMale(playerlist.get(onlythistime))){
                    onlythistime+=i;
                    i++;
                }
            }
            player1=getplayerWithID(playerlist.get(onlythistime));
            activePlayerId=playerlist.get(onlythistime);

        }else if(temp.contentEquals("PlayerFemale")){
            int i=1;
            if (hasFemale()){
                onlythistime=0;
                while (isMale(playerlist.get(onlythistime))){
                    onlythistime+=i;
                    i++;
                }

            }else {

                onlythistime=0;
                while (!isMale(playerlist.get(onlythistime))){
                    onlythistime+=i;
                }
            }
            player1=getplayerWithID(playerlist.get(onlythistime));
            activePlayerId=playerlist.get(onlythistime);

        }else{
            player1=getplayerWithID(playerlist.get(onlythistime));
            activePlayerId=playerlist.get(onlythistime);
        }
        peopleplayingNow.add(playerlist.get(onlythistime));
        return player1;
    }
    private String getAnotherPlayer(String temp){
        Log.e(TAG, "getAnotherPlayer: "+temp );
        if (temp.contentEquals("Player")){
            int random=getRandom();
            while (playerlist.get(currentPlayer-1)==random &&!playerExists(random)&&isPlayingNow(random)){
                random=getRandom();
            }
            peopleplayingNow.add(random);
            return getplayerWithID(random);
        }else if (temp.contentEquals("PlayerMale")){
            int random=getRandom();
            if (hasMale()){
                while (playerlist.get(currentPlayer-1)==random &&!isMale(random)&&playerExists(random)&&isPlayingNow(random)){
                    random=getRandom();
                }
            }else{
                //just fetch a female
                while (playerlist.get(currentPlayer-1)==random &&isMale(random)&&!playerExists(random)&&isPlayingNow(random)){
                    random=getRandom();
                }

            }
            peopleplayingNow.add(random);
            return getplayerWithID(random);

        }else if(temp.contentEquals("PlayerFemale")){
            int random=getRandom();
            if (hasFemale()){
                while (playerlist.get(currentPlayer-1)==random &&isMale(playerlist.get(random))&&!playerExists(random)&&isPlayingNow(random)){
                    random=getRandom();
                }
            }else{
                //just fetch a male
                while (playerlist.get(currentPlayer-1)==random &&!isMale(playerlist.get(random))&&playerExists(random)&&isPlayingNow(random)){
                    random=getRandom();
                }
            }
            peopleplayingNow.add(random);
            return getplayerWithID(playerlist.get(random));
        }else if(temp.contentEquals("PlayerCompatible")){
            int random=getRandom();
            if (likesMen(playerlist.get(random))){
                if (hasMale()){
                    while (playerlist.get(currentPlayer-1)==random &&!isMale(playerlist.get(random))&&playerExists(random)&&isPlayingNow(random)){
                        random=getRandom();
                    }
                }else{
                    //just fetch a female
                    while (playerlist.get(currentPlayer-1)==random &&isMale(playerlist.get(random))&&!playerExists(random)&&isPlayingNow(random)){
                        random=getRandom();
                    }

                }
                peopleplayingNow.add(random);
                return getplayerWithID(playerlist.get(random));
            }else{
                if (hasFemale()){
                    while (playerlist.get(currentPlayer-1)==random &&isMale(playerlist.get(random))&&!playerExists(random)&&isPlayingNow(random)){
                        random=getRandom();
                    }
                }else{
                    //just fetch a male
                    while (playerlist.get(currentPlayer-1)==random &&!isMale(playerlist.get(random))&&playerExists(random)&&isPlayingNow(random)){
                        random=getRandom();
                    }
                }
                peopleplayingNow.add(random);
                return getplayerWithID(playerlist.get(random));
            }

        }else{
            return "Anonymous player";
        }
    }
    public int getRandom() {
        int rnd = new Random().nextInt(peopleTables.size());

        return peopleTables.get(rnd).getId();
    }
    public boolean playerExists(int a){
        boolean exists=false;
        for (int i = 0; i < playerlist.size(); i++) {
            if (playerlist.get(i)==a){
                exists=true;
            }

        }
        return exists;
    }
    public boolean hasMale(){
        boolean exists=false;
        for (int i = 0; i < peopleTables.size(); i++) {
            if (peopleTables.get(i).getGender()==0){
                exists=true;
            }

        }
        return exists;
    }
    public boolean hasFemale(){
        boolean exists=false;
        for (int i = 0; i < peopleTables.size(); i++) {
            Log.e(TAG, "hasFemale: size"+playerlist.size() );
            if (peopleTables.get(i).getGender()==1){
                exists=true;
            }

        }
        return exists;
    }
    public boolean hasPlayed(int a){
        boolean hasPlayed=false;
        for (int i = 0; i < peopleAlreadyPlayed.size(); i++) {
            if (peopleAlreadyPlayed.get(i)==a){
                hasPlayed=true;
            }

        }
        return hasPlayed;
    }
    public boolean isPlayingNow(int a){
        boolean hasPlayed=false;
        for (int i = 0; i < peopleplayingNow.size(); i++) {
            if (peopleplayingNow.get(i)==a){
                hasPlayed=true;
            }

        }
        return hasPlayed;
    }
    private boolean isMale(int id){
        boolean res=false;
        for (int i = 0; i < peopleTables.size(); i++) {
            if (peopleTables.get(i).getId()==id&&peopleTables.get(i).getGender()==0){
                Log.e(TAG, "isMale: " );
                res= true;
            }
        }
        return res;
    }
    private boolean likesMen(int id){
        boolean res=false;
        for (int i = 0; i < peopleTables.size(); i++) {
            if (peopleTables.get(i).getId()==id&&peopleTables.get(i).getIntrestedIn()==0){
                Log.e(TAG, "likesMen: " );
                res= true;
            }
        }
        return res;
    }
    private String getplayerWithID(Integer integer) {
        String name="";
        for (int i = 0; i < peopleTables.size(); i++) {
            if (peopleTables.get(i).getId()==integer){
                name=peopleTables.get(i).getName();
            }
        }
        Log.e(TAG, "getplayerWithID: "+name );
        return name;
    }

    private void prepareQuestions(){
        try {
            JSONObject jsonObject = new JSONObject(jsonQuestions);
//            JSONArray m_jArry = obj.getJSONArray("formules");
            questionTables=new ArrayList<>();
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();

                if (jsonObject.get(key) instanceof JSONObject) {
                    // do something with jsonObject here
                    JSONObject oneQuize=jsonObject.getJSONObject(key);
                    String question=oneQuize.getString("question");
                    String level=oneQuize.getString("level");
                    String players=oneQuize.getString("players");
                    QuestionTable aa= new QuestionTable(question,players,level);
                    if (Integer.valueOf(level)==intLevel){
                        questionTables.add(aa);
                        Log.e(TAG, "prepareQuestions: question: level"+aa.getLevel1()+aa.getPlayers1() );
                    }
                    Log.e(TAG, "prepareQuestions: "+question );
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
                if (questionTables==null){
                    prepareQuestions();
                }else {
                    getNextQuestion();
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
