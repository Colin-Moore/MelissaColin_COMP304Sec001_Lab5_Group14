package com.colin.melissacolin_comp304sec001_lab4_group14;

import android.app.Application;
import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;

public class BikeViewModel extends AndroidViewModel {

    private BikeRepository bikeRepository;

    public BikeViewModel(@NonNull Application application){
        super(application);
        bikeRepository = new BikeRepository(application);
    }

    public void insert(Bike bike){
        bikeRepository.insert(bike);
    }
}
