package com.colin.melissacolin_comp304sec001_lab05_group14;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    //add bike to database
    public void insert(Bike bike){
        try {
            myRef.push().setValue(bike);
        }
        catch (Exception e){
            Log.i("TAG", e.toString());
        }
    }

    //remove bike from database
    public void delete(Bike bike){
        try {
            myRef.child(bike.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("TAG", "Done");
                }
            });
        }
        catch (Exception e){
            Log.i("TAG", e.toString());
        }
    }
}
