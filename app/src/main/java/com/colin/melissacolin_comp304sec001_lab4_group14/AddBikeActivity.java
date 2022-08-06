package com.colin.melissacolin_comp304sec001_lab4_group14;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddBikeActivity extends AppCompatActivity{

    Bike bike;
    int errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);
        setTitle("Add Bike");

        BikeViewModel bikeViewModel = new ViewModelProvider(this).get(BikeViewModel.class); //instantiate bikeViewModel

        EditText txtBikeName = findViewById(R.id.editTextBikeName);
        EditText txtCategory = findViewById(R.id.editTextCategory);
        EditText txtBrand = findViewById(R.id.editTextBrand);
        EditText txtCost = findViewById(R.id.editTextCost);

        Button btn = findViewById(R.id.btnAddBike);

        btn.setOnClickListener(new View.OnClickListener() { //handle clicking on Add Bike button
            @Override
            public void onClick(View view) {
                errors = 0;
                //validate input fields
                validate(txtBikeName);
                validate(txtBrand);
                validate(txtCost);
                validate(txtCategory);

                //if any fields aren't filled out, display a message informing the user
                if(errors > 0){
                    Toast.makeText(AddBikeActivity.this, R.string.fieldError, Toast.LENGTH_SHORT).show();
                }
                //if there are no blank fields, add the bike to the database
                else{
                    String bikeName = txtBikeName.getText().toString();
                    String category = txtCategory.getText().toString();
                    String brand = txtBrand.getText().toString();
                    double cost = Double.parseDouble(txtCost.getText().toString());
                    try {
                        bike = new Bike(bikeName, brand, category, cost);
                        bikeViewModel.insert(bike);
                        Toast.makeText(AddBikeActivity.this, bikeName + " Added!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.i("TAG", e.toString());
                    }
                    finish(); //return to parent activity
                }
            }
            });
        }

        //method to validate input fields
        public void validate(EditText editText){
        //if the field is empty, turn the background red and increase the error counter.
        if(editText.getText().toString().isEmpty()){
            editText.setBackgroundColor(Color.RED);
            errors++;
        }
        //if the field is not empty, remove background colour (if any)
        else{
            editText.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}