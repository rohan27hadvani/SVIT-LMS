package com.dataflair.librarymanagementapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map;

public class AdminDashBoardAdapter extends FirebaseRecyclerAdapter<Model, AdminDashBoardAdapter.Viewholder> {

    public AdminDashBoardAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull AdminDashBoardAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Setting data to android materials

        if(model.getApprove_status().equals("1"))
        {
            holder.mainll.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.GONE);
        }
        else
        {
            holder.mainll.setVisibility(View.VISIBLE);
        }
        holder.bookName.setText("Book Name: " + model.getBookName());
        holder.booksCount.setText("Available Books: " + model.getBooksCount());
        holder.bookLocation.setText("Book Location: " + model.getBookLocation());

        holder.userNameTxt.setText(model.getName());
        holder.userPhoneNumberTxt.setText(model.getPhoneNumber());
        holder.userAddressTxt.setText(model.getAddress());
        Picasso.get().load(model.getImageUrl()).into(holder.imageView);
        //Implementing the OnClick Listener to delete the data from the database
        holder.cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting user id from the gmail sing in
                String userId=model.getUserId();
                //Path to the database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("OrderedBooks");
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
                                        FirebaseDatabase.getInstance().getReference().child("myOrderedBooks").child(userId)
                                                .child(key).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            //Generating Unique Key
                                                            String pushKey=FirebaseDatabase.getInstance().getReference().child("UserNotifications").push().getKey();

                                                            //Adding Notification To Database
                                                            FirebaseDatabase.getInstance().getReference().child("UserNotifications").child(userId).child(pushKey)
                                                                    .child("notification").setValue("Your Request for Book " + model.getBookName() + " is Accepted By Admin")
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {

                                                                            //Showing the Toast message to the user
                                                                            Toast.makeText(view.getContext(), "Book Order Canceled Successfully", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });



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

        //implementing onClickListener
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Generating Unique key
                String pushKey=FirebaseDatabase.getInstance().getReference().child("UserNotifications").push().getKey();

                //update book status
                updatestauts(model,position);




                //Adding Notification to database
                FirebaseDatabase.getInstance().getReference().child("UserNotifications").child(model.getUserId())
                        .child(pushKey)
                        .child("notification").setValue("Your Request for Book " + model.getBookName() + " is Denied By Admin")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                //Showing toast Message
                                Toast.makeText(view.getContext(), "Book Accepted",Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }

    private void updatestauts(Model model, int position)
    {
        String userId=model.getUserId();
        //Path to the database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("OrderedBooks");
        reference.orderByChild("bookName").equalTo(model.getBookName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    //getting the parent node of the data
                    String key = ds.getKey();
                    Map map=new HashMap<>();
                    map.put("approve_status","1");
                    reference.child(key).updateChildren(map);
                    FirebaseDatabase.getInstance().getReference().child("myOrderedBooks").child(userId)
                            .child(key).updateChildren(map);
                    String pushKey=FirebaseDatabase.getInstance().getReference().child("UserNotifications").push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("UserNotifications").child(userId).child(pushKey)
                            .child("notification").setValue("Your Request for Book " + model.getBookName() + " is Accepted By Admin")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            });


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    @NonNull
    @Override
    public AdminDashBoardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_dashboard_layout_admin, parent, false);
        return new AdminDashBoardAdapter.Viewholder(view);
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView bookName, booksCount, bookLocation;
        Button acceptBtn, cancleBtn;

        TextView userNameTxt, userAddressTxt, userPhoneNumberTxt;
        LinearLayout mainll;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


            //Assigning Address of the android materials
            imageView = (ImageView) itemView.findViewById(R.id.BookImage);
            bookName = (TextView) itemView.findViewById(R.id.BookNameTxt);
            booksCount = (TextView) itemView.findViewById(R.id.BooksCountTxt);
            bookLocation = (TextView) itemView.findViewById(R.id.BooksLocationTxt);
            mainll=itemView.findViewById(R.id.mainll);

            userNameTxt = (TextView) itemView.findViewById(R.id.UserNameTxt);
            userAddressTxt = (TextView) itemView.findViewById(R.id.UserAddressTxt);
            userPhoneNumberTxt = (TextView) itemView.findViewById(R.id.UserPhoneNumberTxt);

            acceptBtn=(Button)itemView.findViewById(R.id.AcceptBookBtn);
            cancleBtn=(Button)itemView.findViewById(R.id.CancleBookBtn);

        }
    }

}


