package com.dataflair.librarymanagementapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflair.librarymanagementapp.Adapters.DashBoardAdapter;
import com.dataflair.librarymanagementapp.Adapters.HomeAdapter;
import com.dataflair.librarymanagementapp.Model.Model;
import com.dataflair.librarymanagementapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;


public class DashBoardFragment extends Fragment {


    RecyclerView recyclerView;
    DashBoardAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_dash_board, container, false);

        //Assigning the Recyclerview
        recyclerView = (RecyclerView) view.findViewById(R.id.DashBoardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //Getting User id
        String userId= GoogleSignIn.getLastSignedInAccount(getContext()).getId();

        //Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("myOrderedBooks").child(userId)
                                .orderByChild("approve_status").equalTo("0"), Model.class)
                        .build();


        //Setting adapter to RecyclerView
        adapter = new DashBoardAdapter(options);
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