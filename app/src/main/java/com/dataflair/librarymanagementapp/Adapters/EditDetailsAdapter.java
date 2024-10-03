package com.dataflair.librarymanagementapp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.librarymanagementapp.Activities.BooksActivity;
import com.dataflair.librarymanagementapp.Activities.EditBookDetailsActivity;
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

public class EditDetailsAdapter extends FirebaseRecyclerAdapter<Model, EditDetailsAdapter.Viewholder> {

    public EditDetailsAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull EditDetailsAdapter.Viewholder holder, int position, @NonNull Model model) {


        //Setting data to android materials
        holder.bookName.setText(model.getBookName());
        holder.booksCount.setText(model.getBooksCount());
        holder.bookLocation.setText(model.getBookLocation());

        Picasso.get().load(model.getImageUrl()).into(holder.imageView);

        String pushKey=model.getPushKey().toString();
        String category=model.getCategory().toString();

        //Implementing the OnClick Listener to delete the data from the database
        holder.updateDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bookName=holder.bookName.getText().toString();
                String booksCount=holder.booksCount.getText().toString();
                String bookLocation=holder.bookLocation.getText().toString();

                //Hash map to store values
                HashMap bookDetails = new HashMap();

                //adding the data to hashmap
                bookDetails.put("bookName", bookName);
                bookDetails.put("booksCount", booksCount);
                bookDetails.put("bookLocation", bookLocation);
                bookDetails.put("category",category);
                bookDetails.put("pushKey",pushKey);


                FirebaseDatabase.getInstance().getReference().child("AllBooks")
                        .child(category)
                        .child(pushKey)
                        .updateChildren(bookDetails)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                                Toast.makeText(view.getContext(), "Details Updated Successfully",Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });

    }


    @NonNull
    @Override
    public EditDetailsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_edit_book_details_layout, parent, false);
        return new EditDetailsAdapter.Viewholder(view);
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {


        ImageView imageView;
        EditText bookName, booksCount, bookLocation;
        Button updateDetailsBtn;


        public Viewholder(@NonNull View itemView) {
            super(itemView);


            //Assigning Address of the android materials
            imageView = (ImageView) itemView.findViewById(R.id.BookImage);
            bookName = (EditText) itemView.findViewById(R.id.BookNameTxt);
            booksCount = (EditText) itemView.findViewById(R.id.BooksCountTxt);
            bookLocation = (EditText) itemView.findViewById(R.id.BooksLocationTxt);

            updateDetailsBtn = (Button) itemView.findViewById(R.id.UpdateDataBtn);
            updateDetailsBtn.setText("Update Details");

        }
    }

}
