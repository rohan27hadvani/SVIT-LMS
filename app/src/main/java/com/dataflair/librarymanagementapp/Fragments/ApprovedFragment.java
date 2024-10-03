package com.dataflair.librarymanagementapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflair.librarymanagementapp.Adapters.AdminDashBoardAdapter;
import com.dataflair.librarymanagementapp.Adapters.ApprovedAdapter;
import com.dataflair.librarymanagementapp.Model.Model;
import com.dataflair.librarymanagementapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class ApprovedFragment extends Fragment {
    RecyclerView recyclerView;
    ApprovedAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view= inflater.inflate(R.layout.fragment_approved, container, false);
        //Assigning the Recyclerview to lost Items
        recyclerView = (RecyclerView) view.findViewById(R.id.AdminDashBoardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        //Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("OrderedBooks")
                                .orderByChild("approve_status").equalTo("1"), Model.class)
                        .build();


        //Setting adapter to RecyclerView
        adapter = new ApprovedAdapter(options);
        recyclerView.setAdapter(adapter);

        return view;
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