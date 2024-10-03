package com.dataflair.librarymanagementapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dataflair.librarymanagementapp.Activities.SplashScreenActivity;
import com.dataflair.librarymanagementapp.Model.Model;
import com.dataflair.librarymanagementapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileFragment extends Fragment {

    CircleImageView circleImageView;
    TextView userNameTxt;
    EditText phoneNumberEditTxt, addressEditTxt, cityNameEditTxt, pinCodeEdittxt;
    Button signOutBtn, updateDetailsBtn;
    DatabaseReference databaseReference;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //Assigning all the addresses of the android materials
        circleImageView = (CircleImageView) view.findViewById(R.id.ProfileImageView);
        userNameTxt = (TextView) view.findViewById(R.id.UserNameTxt);
        cityNameEditTxt = (EditText) view.findViewById(R.id.CityEditText);
        phoneNumberEditTxt = (EditText) view.findViewById(R.id.PhoneNumberEditText);
        pinCodeEdittxt = (EditText) view.findViewById(R.id.PinCodeExitText);
        addressEditTxt = (EditText) view.findViewById(R.id.AddressEditText);

        updateDetailsBtn = (Button) view.findViewById(R.id.UpdateProfileBtn);
        signOutBtn = (Button) view.findViewById(R.id.SignOutBtn);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllUsers");
        //Getting user detials from GoogleSignin
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            userId = acct.getId().toString();

            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    //Gettign the data form the firebase using model class
                    Model model = snapshot.getValue(Model.class);
                    //setting the data to android materials
                    Picasso.get().load(model.getProfilepic()).into(circleImageView);
                    userNameTxt.setText(model.getName());
                    cityNameEditTxt.setText(model.getCity());
                    phoneNumberEditTxt.setText(model.getPhoneNumber());
                    addressEditTxt.setText(model.getAddress());
                    pinCodeEdittxt.setText(model.getPincode());

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }


        //Implementing OnClick Listener to update data to firebase
        updateDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting the current data from the edit Text to update it to firebase
                String phoneNumber = phoneNumberEditTxt.getText().toString();
                String cityName = cityNameEditTxt.getText().toString();
                String pinCode = pinCodeEdittxt.getText().toString();
                String address = addressEditTxt.getText().toString();

                //Checking for empty fields
                if (phoneNumber.isEmpty() || cityName.isEmpty() || pinCode.isEmpty() || address.isEmpty()) {
                    Toast.makeText(getContext(), "Please,Fill Details", Toast.LENGTH_SHORT).show();
                } else {
                    //calling method to update data to firebase
                    updateDetails(phoneNumber, cityName, pinCode, address, userId);
                }
            }
        });

        //implementing onClickListener to make the user signOut
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                //GoogleSignInClient to access the current user
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //User Signout
                            FirebaseAuth.getInstance().signOut();

                            //Redirecting to starting Activity
                            Intent intent = new Intent(getContext(), SplashScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }
                });
            }
        });

        return view;
    }

    private void updateDetails(String phoneNumber, String cityName, String pinCode, String address, String userId) {


        //Storing the user details in hashmap
        HashMap userDetails = new HashMap();

        //adding the data to hashmap
        userDetails.put("phoneNumber", phoneNumber);
        userDetails.put("cityName", cityName);
        userDetails.put("pinCode", pinCode);
        userDetails.put("address", address);

        //adding the data to firebase
        databaseReference.child(userId).updateChildren(userDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {

                if (task.isSuccessful()) {
                    //Showing the Toast message to user
                    Toast.makeText(getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    //Showing the toast message to user
                    Toast.makeText(getContext(), "Please,Try again Later", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}