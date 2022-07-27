package com.colin.melissacolin_comp304sec001_lab4_group14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    BikeAdapter bikeAdapter;
    ArrayList<Bike> bikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvBikeList);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance().getReference("COMP304Sec001-Lab4 Group14");

        BikeViewModel bikeViewModel = new ViewModelProvider(this).get(BikeViewModel.class);
        bikes = new ArrayList<Bike>(); //initialize bikes List

        //set up recyclerview
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bikeAdapter = new BikeAdapter(this, bikes);
        recyclerView.setAdapter(bikeAdapter);

        //get data from Firebase
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String, Bike> bikeMap = new HashMap<String, Bike>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Bike b = dataSnapshot.getValue(Bike.class);
                    bikeMap.put(dataSnapshot.getKey(), b);
                    }
                for(String s: bikeMap.keySet()){
                    bikes.add(bikeMap.get(s));
                }

                bikeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

}