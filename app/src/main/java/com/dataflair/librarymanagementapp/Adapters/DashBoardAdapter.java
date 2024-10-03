package com.dataflair.librarymanagementapp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.librarymanagementapp.Activities.BooksActivity;
import com.dataflair.librarymanagementapp.Model.Model;
import com.dataflair.librarymanagementapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DashBoardAdapter extends FirebaseRecyclerAdapter<Model, DashBoardAdapter.Viewholder> {

    public DashBoardAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull DashBoardAdapter.Viewholder holder, int position, @NonNull Model model) {


        //Setting data to android materials
        holder.bookName.setText("Book Name: " + model.getBookName());
        holder.booksCount.setText("Available Books: " + model.getBooksCount());
        holder.bookLocation.setText("Book Location: " + model.getBookLocation());

        Picasso.get().load(model.getImageUrl()).into(holder.imageView);

        //Implementing the OnClick Listener to delete the data from the database
        holder.collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting user id from the gmail sing in
                String userId = GoogleSignIn.getLastSignedInAccount(view.getContext()).getId();
                //Path to the database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("myOrderedBooks").child(userId);
                reference.orderByChild("bookName").equalTo(model.getBookName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            //getting the parent node of the data
                            String key = ds.getKey();

                            //removing the data from the database
                            reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("OrderedBooks")
                                                .child(key).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            //Showing the Toast message to the user
                                                            Toast.makeText(view.getContext(), "Book Order Canceled Successfully", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }
                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

    }


    @NonNull
    @Override
    public DashBoardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_book_layout, parent, false);
        return new DashBoardAdapter.Viewholder(view);
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView bookName, booksCount, bookLocation;
        Button collectBtn;


        public Viewholder(@NonNull View itemView) {
            super(itemView);


            //Assigning Address of the android materials
            imageView = (ImageView) itemView.findViewById(R.id.BookImage);
            bookName = (TextView) itemView.findViewById(R.id.BookNameTxt);
            booksCount = (TextView) itemView.findViewById(R.id.BooksCountTxt);
            bookLocation = (TextView) itemView.findViewById(R.id.BooksLocationTxt);

            collectBtn = (Button) itemView.findViewById(R.id.CollectBookBtn);
            collectBtn.setText("Cancel Book");

        }
    }

}

