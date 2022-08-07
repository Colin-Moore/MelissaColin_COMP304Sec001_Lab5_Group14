package com.colin.melissacolin_comp304sec001_lab05_group14;

import android.content.Context;

public class BikeRepository {
    private final BikeDao bikeDao;
    public BikeRepository(Context context){
        bikeDao = BikeDao.getInstance();
    }

    public void insert(Bike bike){
        bikeDao.insert(bike);
    }

    public void delete(Bike bike){ bikeDao.delete(bike);}
}