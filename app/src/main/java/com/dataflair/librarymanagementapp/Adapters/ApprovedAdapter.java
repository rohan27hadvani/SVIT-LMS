package com.dataflair.librarymanagementapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.librarymanagementapp.Model.Model;
import com.dataflair.librarymanagementapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ApprovedAdapter extends FirebaseRecyclerAdapter<Model, ApprovedAdapter.Viewholder> {

    public ApprovedAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {
        if(model.getApprove_status().equals("0"))
        {
            holder.mainll.setVisibility(View.GONE);
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
        holder.acceptBtn.setVisibility(View.GONE);
        holder.cancleBtn.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_dashboard_layout_admin, parent, false);
        return new Viewholder(view);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView bookName, booksCount, bookLocation;
        Button acceptBtn, cancleBtn;
        LinearLayout mainll;
        TextView userNameTxt, userAddressTxt, userPhoneNumberTxt;

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
