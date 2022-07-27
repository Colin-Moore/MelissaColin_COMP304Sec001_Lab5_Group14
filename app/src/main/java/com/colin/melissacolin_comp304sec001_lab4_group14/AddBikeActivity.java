package com.colin.melissacolin_comp304sec001_lab4_group14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddBikeActivity extends AppCompatActivity {

    Bike bike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);

        BikeViewModel bikeViewModel = new ViewModelProvider(this).get(BikeViewModel.class);

        EditText txtBikeName = findViewById(R.id.editTextBikeName);
        EditText txtCategory = findViewById(R.id.editTextCategory);
        EditText txtBrand = findViewById(R.id.editTextBrand);
        EditText txtCost = findViewById(R.id.editTextCost);


        Button btn = findViewById(R.id.btnAddBike);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bikeName = txtBikeName.getText().toString();
                String category = txtCategory.getText().toString();
                String brand = txtBrand.getText().toString();
                double cost = Double.parseDouble(txtCost.getText().toString());

                try {
                    bike = new Bike(bikeName, brand, category, cost);
                }
                catch(Exception e){
                    Log.i("TAG", e.toString());
                }

                bikeViewModel.insert(bike);
            }
        });
    }

}