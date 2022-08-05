package com.colin.melissacolin_comp304sec001_lab4_group14;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//adapter for the bike activity recyclerview

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.MyViewHolder> {

    Context context;
    ArrayList<Bike> bikes;

    public BikeAdapter(Context context, ArrayList<Bike> bikes){
        this.context = context;
        this.bikes = bikes;
     }

    @NonNull
    @Override
    public BikeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bike_item, parent, false);
        return new MyViewHolder(view);
    }

    public Bike getBikeAtPosition(int position){
        return bikes.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeAdapter.MyViewHolder holder, int position) {
        Bike bike = bikes.get(position);
            holder.brand.setText(bike.getBrand());
            holder.bikeName.setText(bike.getName());
            holder.cost.setText(String.valueOf(bike.getCost()));
            holder.category.setText(bike.getCategory());
    }

    @Override
    public int getItemCount() {
        return bikes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView bikeName, brand, category, cost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bikeName = itemView.findViewById(R.id.txtBikeName);
            brand = itemView.findViewById(R.id.txtBrand);
            cost = itemView.findViewById(R.id.txtCost);
            category = itemView.findViewById(R.id.txtCategory);
        }
    }
}
