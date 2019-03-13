package com.example.jake.fantasy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularPlayers extends AppCompatActivity {
    DatabaseReference dref;
    ArrayList <Players> players;
    ArrayList <Players> filtered;
    ListView listView;
    int userMon,fore;
    ArrayList<Integer> pids = new ArrayList<>();
    String role,country,name,maxPrice,minPrice,userId,team,posi,one,t1,t2,mid;
    int tot,par;
    Button back;
    public ProgressDialog mProgressDialog;
    ValueEventListener mListener;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        t1=getIntent().getStringExtra("Team1");
        t2=getIntent().getStringExtra("Team2");
        mid=getIntent().getStringExtra("mid");
        userId=getIntent().getStringExtra("userId");
        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(PopularPlayers.this,TabbedActiviy.class);
                startIntent.putExtra("Team1",t1);
                startIntent.putExtra("Team2",t2);
                startIntent.putExtra("mid",mid);
                startIntent.putExtra("userId",userId);
                startActivity(startIntent);
            }
        });

        listView = findViewById(R.id.playerList1);
        showProgressDialog();
        populatePlayers();
       }
    void populatePlayers(){
        players = new ArrayList<>();
        dref = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference ddref = dref.child("Players")
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               /* int par2 = 0;
                //Log.d(TAG, "Dhuke");
                if(role.equals("Bat")) {
                    par = Integer.parseInt(dataSnapshot.child("USERS").child(userId).child("BatsmenSel").getValue().toString());

                }
                if(role.equals("Wkt")) {
                    par = Integer.parseInt(dataSnapshot.child("USERS").child(userId).child("WktKeeperSel").getValue().toString());
                    // par2 = Integer.parseInt(dataSnapshot.child("USERS").child(userId).child("BatsmenSel").getValue().toString());
                }
                if(role.equals("Bowl"))
                    par = Integer.parseInt( dataSnapshot.child("USERS").child(userId).child("BowlerSel").getValue().toString());
                if(role.equals("All"))
                    par = Integer.parseInt( dataSnapshot.child("USERS").child(userId).child("AllrounderSel").getValue().toString());
                for(int i=0;i<par;i++){
                    pids.add(Integer.parseInt(dataSnapshot.child("USERS").child(userId).child(role).child(Integer.toString(i)).child("PID").getValue().toString()));

                }
               if(role.equals("Wkt")){
                    for(int i=0;i<par2;i++){
                        pids.add(Integer.parseInt(dataSnapshot.child("USERS").child(userId).child("Bat").child(Integer.toString(i)).child("PID").getValue().toString()));

                    }
                }
                userMon = Integer.parseInt( dataSnapshot.child("USERS").child(userId).child("Price").getValue().toString());
                fore = Integer.parseInt( dataSnapshot.child("USERS").child(userId).child("Foreign").getValue().toString());*/


                for(int i=0;i<=141;i++){
                    DataSnapshot ds = dataSnapshot.child("PLAYERS").child(Integer.toString(i));
                    Players player = new Players();
                    String team =(String)ds.child("Team").getValue();
                    if((team.equals(t1)||team.equals(t2))){

                    //player.setAge(Integer.parseInt((String) ds.child("Age").getValue()));
                    player.setAge(18);
                    player.setCountry((String)ds.child("Country").getValue());
                    //Log.d("cou", player.getCountry());
                    player.setName((String)ds.child("Name").getValue());
                    player.setRole((String)ds.child("Role").getValue());
                    player.setTeam((String)ds.child("Team").getValue());
                    player.setUrl((String)ds.child("ImageURL").getValue());
                    player.setTotScore(0);
                    player.setPrice(((Long) ds.child("Price").getValue()).intValue());
                    player.setId(((Long) ds.child("PlayerId").getValue()).intValue());
                    players.add(player);}

                }
                filterPlayers();
                generateListView();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dref.addValueEventListener(mListener);

        //while(players.size()<165);
        //Log.d(TAG, players.get(0).getName());

    }
    void filterPlayers(){
        filtered = new ArrayList<>();
        for(int i=0;i<players.size();i++){
            Players p = players.get(i);
            Log.i("team",p.getTeam());

           if(((p.getTeam().equals(t1)||p.getTeam().equals(t2))))

                filtered.add(p);

        }
    }
    void generateListView(){


        CustopmAdapter custopmAdapter = new CustopmAdapter();

        listView.setAdapter(custopmAdapter);
        try {
            Thread.sleep(1000);
        } catch(Exception ex) {/* */}
        hideProgressDialog();

       /* try {
            Thread.sleep(1000);
        } catch(Exception ex) {}

        finish();*/


    }
    class CustopmAdapter extends BaseAdapter{

        int id;
        @Override
        public int getCount() {
            return filtered.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return id;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            dref = FirebaseDatabase.getInstance().getReference();

            view = getLayoutInflater().inflate(R.layout.custom_layout,null);
            ImageView image = view.findViewById(R.id.listIm);
            TextView name = view.findViewById(R.id.pName);
            TextView roll= view.findViewById(R.id.pRole);
            TextView price = view.findViewById(R.id.pPrice);
            TextView team = view.findViewById(R.id.pTeam);
            final TextView prices = view.findViewById(R.id.price);
            String url = filtered.get(i).getUrl();
            loadimage(url,image);
            name.setText(filtered.get(i).getName());
            roll.setText(filtered.get(i).getRole());
            team.setText(filtered.get(i).getTeam());
            dref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    prices.setText(String.format("%.2f",((Long)dataSnapshot.child("PLAYERS").child(Integer.toString(filtered.get(i).getId())).child("count").getValue()*1.0/(Long) dataSnapshot.child("MATCHES").child(mid).child("contestants").getValue()*1.0)*100));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            price.setText("%age");


            id = players.get(i).getId();

            return view;
        }

    }
    void loadimage(String url,ImageView imageView){
        Picasso.with(this).load(url).placeholder(R.drawable.anon).error(R.drawable.anon)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);

            mProgressDialog.setMessage("Generating Player List");

            //mProgressDialog.setMessage("Adding Player to the squad.");
            mProgressDialog.setIndeterminate(true);

        }



        mProgressDialog.show();

    }



    @Override
    protected void onStop() {
        if (mListener != null && dref!=null) {
            dref.removeEventListener(mListener);
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (mListener != null && dref!=null) {
            dref.removeEventListener(mListener);
        }
        super.onPause();
    }

    public void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {

            mProgressDialog.dismiss();

        }

    }


}
