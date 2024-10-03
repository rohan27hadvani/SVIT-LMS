package com.dataflair.librarymanagementapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dataflair.librarymanagementapp.MainActivity;
import com.dataflair.librarymanagementapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UserDetailsActivity extends AppCompatActivity {

    EditText userPhoneNumber, userAddress, userCity, userPinCode;
    Button addDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        //Assigning the address of the android materials
        userPhoneNumber = (EditText) findViewById(R.id.PhoneNumberEditText);
        userAddress = (EditText) findViewById(R.id.AddressEditText);
        userCity = (EditText) findViewById(R.id.CityEditText);
        userPinCode = (EditText) findViewById(R.id.PinCodeExitText);

        addDataBtn = (Button) findViewById(R.id.UpdateProfileBtn);

        //implementing onclicklistener
        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting test from the edit text
                String phoneNumber = userPhoneNumber.getText().toString().trim();
                String address = userAddress.getText().toString().trim();
                String city = userCity.getText().toString().trim();
                String pinCode = userPinCode.getText().toString().trim();


                //checking for the empty fields
                if (phoneNumber.isEmpty() || address.isEmpty() || city.isEmpty() || pinCode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please,Fill all the Details", Toast.LENGTH_SHORT).show();
                } else {

                    //method to add data to firebase
                    addUserDetails(phoneNumber, address, city, pinCode);

                }

            }
        });
    }

    private void addUserDetails(String phoneNumber, String address, String city, String pinCode) {

        //Getting the user id form the google signin
        String id = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId();

        //Creating Hashmap to store data
        HashMap userDetails = new HashMap();
        userDetails.put("phoneNumber", phoneNumber);
        userDetails.put("address", address);
        userDetails.put("city", city);
        userDetails.put("pincode", pinCode);

        //Adding the user datials to firebase
        FirebaseDatabase.getInstance().getReference().child("AllUsers").child(id)
                .updateChildren(userDetails)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task task) {
                        if (task.isSuccessful()) {


                            //showing the toast message to user
                            Toast.makeText(getApplicationContext(), "Details added Successfully", Toast.LENGTH_SHORT).show();

                            //Changing current intent after adding the details to firebase
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    }
                });
    }
}