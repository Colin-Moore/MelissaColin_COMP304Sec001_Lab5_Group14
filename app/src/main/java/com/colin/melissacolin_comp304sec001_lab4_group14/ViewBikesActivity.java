package com.colin.melissacolin_comp304sec001_lab4_group14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBikesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BikeViewModel bikeViewModel;
    DatabaseReference database;
    BikeAdapter bikeAdapter;
    ArrayList<Bike> bikes;
    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bikes);
        recyclerView = findViewById(R.id.rvBikeList);

        bikeViewModel = new ViewModelProvider(this).get(BikeViewModel.class);
        bikes = new ArrayList<Bike>(); //initialize bikes List

        categories = new ArrayList<>();
        categories.add(0, "All Categories");
        //set up recyclerview
        database = FirebaseDatabase.getInstance().getReference("COMP304Sec001-Lab4 Group14");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bikeAdapter = new BikeAdapter(this, bikes);
        recyclerView.setAdapter(bikeAdapter);

        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange (DataSnapshot snapshot){
                        bikes.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Bike bike = dataSnapshot.getValue(Bike.class);
                            if (!categories.contains(bike.category)) {
                                categories.add(bike.category);
                            }
                            if (categorySpinner.getSelectedItemPosition() == 0) {
                                bikes.add(bike);
                            } else {
                                if (bike.category.equals(categorySpinner.getSelectedItem().toString())) {
                                    bikes.add(bike);
                                }
                            }
                            bike.setKey(dataSnapshot.getKey());
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

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                 viewHolder.getAdapterPosition();
                 Bike bike = bikeAdapter.getBikeAtPosition(viewHolder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewBikesActivity.this);
                builder.setMessage(R.string.confirmDelete).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bikeViewModel.delete(bike);
                        Toast.makeText(ViewBikesActivity.this, R.string.bikeDeleted, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bikeAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            };
        }).attachToRecyclerView(recyclerView);
    }
}