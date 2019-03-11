package com.example.jake.fantasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fixture extends AppCompatActivity {
    private static final String TAG = "FixtFragment";
    DatabaseReference dref;
    ArrayList<String> teamsL,matchnoL,venueL,timeL;
    ArrayList<Integer>isValid;
    ListView listView;
    ValueEventListener mListener;
    Button button;
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture);
        final String userId=getIntent().getStringExtra("userId");
        teamsL = new ArrayList<>();
        matchnoL = new ArrayList<>();
        venueL = new ArrayList<>();
        timeL = new ArrayList<>();
        isValid=new ArrayList<>();
        listView = (ListView) findViewById(R.id.matchList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String teams = teamsL.get(position);
                int valid=isValid.get(position);
                String[] data = teams.split("vs");
                Log.i("Team1",data[0]);
                Log.i("Team2",data[1]);
                Log.i("Valid",Integer.toString(valid));
                if(valid==0){
                    Intent startIntent = new Intent(Fixture.this,CreatingTeam1.class);
                    startIntent.putExtra("userId",userId);
                    startIntent.putExtra("Team1",data[0]);
                    startIntent.putExtra("Team2",data[1]);
                    startIntent.putExtra("mid",Integer.toString(position));
                    Log.i("mid",Integer.toString(position));
                    startActivity(startIntent);

                }
                else{

                    Toast.makeText(Fixture.this, "Match not available",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        populateMatches();
    }
    void populateMatches(){
        dref = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference ddref = dref.child("Players")
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(int i=0;i<=61;i++){
                    DataSnapshot ds = dataSnapshot.child("MATCHES").child(Integer.toString(i));
                    String team1 = (String)ds.child("Team1").getValue();
                    String team2 = (String)ds.child("Team2").getValue();
                    int valid=Integer.valueOf(String.valueOf(ds.child("isValid").getValue()));
                    teamsL.add(team1+"vs"+team2);
                    venueL.add("");
                    matchnoL.add(Integer.toString(i+1));
                    isValid.add(valid);
                    String times = "";
                    String dates = "";
                    timeL.add("");

                }
                geterateList();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dref.addValueEventListener(mListener);

        Log.d(TAG, "Dhuke");
        //while(players.size()<165);
        //Log.d(TAG, players.get(0).getName());

    }
    void geterateList(){
        CustopmAdapter custopmAdapter = new CustopmAdapter();

        Log.d(TAG, "geege");
        listView.setAdapter(custopmAdapter);
    }
    class CustopmAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 62;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.fixture_items,null);
            TextView teams = view.findViewById(R.id.teams);
            TextView matchno= view.findViewById(R.id.matchNo);


            teams.setText(teamsL.get(i));
            matchno.setText(matchnoL.get(i));


            return view;
        }
    }
    @Override
    public void onPause() {
        if (mListener != null && dref!=null) {
            dref.removeEventListener(mListener);
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (mListener != null && dref!=null) {
            dref.removeEventListener(mListener);
        }
        super.onStop();
    }
}