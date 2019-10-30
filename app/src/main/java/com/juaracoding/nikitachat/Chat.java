package com.juaracoding.nikitachat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juaracoding.nikitachat.Model.MessageActivity;
import com.juaracoding.nikitachat.Model.PeopleModel;

import java.util.ArrayList;

public class Chat extends AppCompatActivity implements  View.OnClickListener {

    ArrayList<PeopleModel> peopleModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        RecyclerView recyclerView = findViewById(R.id.List);


        peopleModels.add(new PeopleModel("gatot", ":)"));
        peopleModels.add(new PeopleModel("elva", ":)"));
        peopleModels.add(new PeopleModel("ichsan", ":)"));
        peopleModels.add(new PeopleModel("sidik", ":)"));
        peopleModels.add(new PeopleModel("gefan", ":)"));
        peopleModels.add(new PeopleModel("diki", ":)"));
        peopleModels.add(new PeopleModel("dwi", ":)"));
        peopleModels.add(new PeopleModel("sofi", ":)"));
        peopleModels.add(new PeopleModel("febri", ":)"));
        peopleModels.add(new PeopleModel("bram", ":)"));


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messageitem, parent, false);
                v.setOnClickListener(Chat.this);
                return new RecyclerView.ViewHolder(v) {
                };

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                holder.itemView.setTag(String.valueOf(position));
                TextView textView=holder.itemView.findViewById(R.id.txtDate);
                textView.setText(peopleModels.get(position).getName());
            }

            @Override
            public int getItemCount() {
                return peopleModels.size();
            }
        });

    }


    public void onClick(View v) {
        int position= Integer.parseInt(String.valueOf(v.getTag()));

Intent intent=new Intent(Chat.this, MessageActivity.class);
intent.putExtra("nama", peopleModels.get(position).getName());
startActivity(intent);
    }
}

