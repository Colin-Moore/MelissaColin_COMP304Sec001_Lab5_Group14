package com.colin.melissacolin_comp304sec001_lab4_group14;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BikeViewModel extends AndroidViewModel {

    BikeAdapter bikeAdapter;
    ArrayList<Bike> bikes;

    private BikeRepository bikeRepository;

    public BikeViewModel(@NonNull Application application){
        super(application);
        bikeRepository = new BikeRepository(application);
        FirebaseDatabase db = FirebaseDatabase.getInstance();

    }

    public void insert(Bike bike){
        bikeRepository.insert(bike);
    }

}
