package com.colin.melissacolin_comp304sec001_lab4_group14;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BikeDao {

    private static volatile BikeDao INSTANCE;
    private static final String DATABASE_NAME = "COMP304Sec001-Lab4 Group14";
    FirebaseDatabase database;
    DatabaseReference myRef;

    private BikeDao(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(DATABASE_NAME);
    }

    public static BikeDao getInstance(){
        if(INSTANCE == null){
            INSTANCE = new BikeDao();
        }
        return INSTANCE;
    }

    public void insert(Bike bike){
        myRef.push().setValue(bike);
    }


}
