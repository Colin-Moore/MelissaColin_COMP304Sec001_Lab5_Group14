package com.colin.melissacolin_comp304sec001_lab05_group14;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

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

    public void delete(Bike bike){ bikeRepository.delete(bike);}

}
