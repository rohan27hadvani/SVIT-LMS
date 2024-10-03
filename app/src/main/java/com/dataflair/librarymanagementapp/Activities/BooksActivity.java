package com.dataflair.librarymanagementapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dataflair.librarymanagementapp.Adapters.AllBooksAdapter;
import com.dataflair.librarymanagementapp.Adapters.HomeAdapter;
import com.dataflair.librarymanagementapp.Model.Model;
import com.dataflair.librarymanagementapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class BooksActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AllBooksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        String category=getIntent().getStringExtra("category");

        //Assigning the Recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.BooksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("AllBooks").child(category), Model.class)
                        .build();

        adapter = new AllBooksAdapter(options);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        //Starts listening for data from firebase when this fragment starts
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Stops listening for data from firebase
        adapter.stopListening();
    }
}