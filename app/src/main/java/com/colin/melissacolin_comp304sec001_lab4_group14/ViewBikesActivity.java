package com.colin.melissacolin_comp304sec001_lab4_group14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewBikesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BikeViewModel bikeViewModel;
    UserViewModel userViewModel;
    GoogleSignInClient gsc;
    DatabaseReference database;
    BikeAdapter bikeAdapter;
    ArrayList<Bike> bikes;
    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bikes);
        recyclerView = findViewById(R.id.rvBikeList);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        bikeViewModel = new ViewModelProvider(this).get(BikeViewModel.class);

        gsc = GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);

        bikes = new ArrayList<>(); //initialize bikes List
        categories = new ArrayList<>(); //initialize category list
        categories.add(0, "All Categories"); //add default element to the category list

        //get reference to the firebase database
        database = FirebaseDatabase.getInstance().getReference("COMP304Sec001-Lab4 Group14");

        //set up the recyclerview to view bikes
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bikeAdapter = new BikeAdapter(this, bikes);
        recyclerView.setAdapter(bikeAdapter);

        //set up the spinner to display bike categories for filtering bikes
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        //selection listener for bike spinner - used to filter bikes by category
        //this also populates the recyclerview with the list of bikes
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
                            //if the current spinner selected item is the first item (all categories), add all bikes from the database to the list
                            if (categorySpinner.getSelectedItemPosition() == 0) {
                                bikes.add(bike);
                            } else {
                                // if a different category is selected, add only matching bikes to the list
                                if (bike.category.equals(categorySpinner.getSelectedItem().toString())) {
                                    bikes.add(bike);
                                }
                            }
                            bike.setKey(dataSnapshot.getKey()); //set the key of the bike (used for deletion from database)
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
                adapterView.setSelection(0); //when spinner is first created, select the first item
            }
        });

        //functionality for recyclerview on-swipe delete
        //set up itemTouchHelper, specifying which directions to listen for
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            //Handle swipe on an item in the recyclerview
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewHolder.getAdapterPosition(); //get the position of the item
                Bike bike = bikeAdapter.getBikeAtPosition(viewHolder.getAdapterPosition()); // get that bike information

                //prompt the user to verify they want to delete the bike
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewBikesActivity.this);
                builder.setMessage(R.string.confirmDelete).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bikeViewModel.delete(bike); //delete the bike
                        Toast.makeText(ViewBikesActivity.this, R.string.bikeDeleted, Toast.LENGTH_SHORT).show();
                    }
                    //if they select no, notify the adapter to repopulate the list (re-add the swiped-off bike to the list)
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bikeAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            };
        }).attachToRecyclerView(recyclerView); //attach the itemTouchHelper to the recyclerview


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;

        switch(item.getItemId()){
            case R.id.menuAddBike:
                intent = new Intent(ViewBikesActivity.this, AddBikeActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuLogout:
                userViewModel.logout(); //logout user through firebase
                gsc.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ViewBikesActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                            }
                        });
                intent = new Intent(ViewBikesActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // kill this activity so that it can't be navigated back to after logging out
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}