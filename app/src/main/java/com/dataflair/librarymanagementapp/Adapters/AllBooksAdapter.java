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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AllBooksAdapter extends FirebaseRecyclerAdapter<Model, AllBooksAdapter.Viewholder> {

    public AllBooksAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull AllBooksAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Setting data to android materials
        holder.bookName.setText("Book Name: " + model.getBookName());
        holder.booksCount.setText("Available Books: " + model.getBooksCount());
        holder.bookLocation.setText("Book Location: " + model.getBookLocation());

        Picasso.get().load(model.getImageUrl()).into(holder.imageView);


        //Implementing OnClickListener
        holder.collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bookLocation = model.getBookLocation();
                String bookName = model.getBookName();
                String booksCount = model.getBooksCount();
                String imageUrl = model.getImageUrl();

                String userId = GoogleSignIn.getLastSignedInAccount(view.getContext()).getId();
                FirebaseDatabase.getInstance().getReference().child("AllUsers").child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                //Getting user data using Model Class
                                Model model1 = snapshot.getValue(Model.class);
                                String name = model1.getName();
                                String city = model1.getCity();
                                String phoneNumber = model1.getPhoneNumber();
                                String address = model1.getAddress();
                                String pincode = model1.getPincode();


                                HashMap userDetails=new HashMap();
                                userDetails.put("name",name);
                                userDetails.put("city",city);
                                userDetails.put("phoneNumber",phoneNumber);
                                userDetails.put("address",address);
                                userDetails.put("pincode",pincode);
                                userDetails.put("bookLocation",bookLocation);
                                userDetails.put("bookName",bookName);
                                userDetails.put("booksCount",booksCount);
                                userDetails.put("imageUrl",imageUrl);
                                userDetails.put("approve_status","0");
                                userDetails.put("userId",userId);

                                String push=FirebaseDatabase.getInstance().getReference().child("OrderedBooks").push().getKey();
                                FirebaseDatabase.getInstance().getReference().child("OrderedBooks").child(push)
                                        .updateChildren(userDetails)
                                        .addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {

                                                FirebaseDatabase.getInstance().getReference().child("myOrderedBooks")
                                                        .child(userId).child(push)
                                                        .updateChildren(userDetails)
                                                        .addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(view.getContext(), "Book Ordred Successfully",Toast.LENGTH_SHORT).show();

                                                            }
                                                        });

                                            }
                                        });


                                //Toast message
                                Toast.makeText(view.getContext(), "Book Ordered", Toast.LENGTH_SHORT).show();
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
    public AllBooksAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_book_layout, parent, false);
        return new AllBooksAdapter.Viewholder(view);
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

        }
    }

}

