package com.dataflair.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dataflair.librarymanagementapp.Activities.AdminActivity;
import com.dataflair.librarymanagementapp.Activities.SplashScreenActivity;
import com.dataflair.librarymanagementapp.Fragments.DashBoardFragment;
import com.dataflair.librarymanagementapp.Fragments.HomeFragment;
import com.dataflair.librarymanagementapp.Fragments.NotificationsFragment;
import com.dataflair.librarymanagementapp.Fragments.UserApprovedFragment;
import com.dataflair.librarymanagementapp.Fragments.UserProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Assigning framelayout resource file to show appropriate fragment using address
        frameLayout = (FrameLayout) findViewById(R.id.UserFragmentContainer);
        //Assigining Bottomnavigaiton Menu
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.UserBottomNavigationView);
        Menu menuNav = bottomNavigationView.getMenu();
        //Setting the default fragment as HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.UserFragmentContainer, new HomeFragment()).commit();
        //Calling the bottoNavigationMethod when we click on any menu item
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                    //Assigining Fragment as Null
                    Fragment fragment = null;
                    switch (item.getItemId()) {

                        //Shows the Appropriate Fragment by using id as address
                        case R.id.HomeMenu:
                            fragment = new HomeFragment();
                            break;

                        case R.id.DashBoardMenu:
                            fragment = new DashBoardFragment();
                            break;

                        case R.id.NotificationsMenu:
                            fragment = new NotificationsFragment();
                            break;


                        case R.id.ProfileMenu:
                            fragment = new UserProfileFragment();
                            break;

                        case R.id.Approved_fragment:
                            fragment = new UserApprovedFragment();
                            break;

                    }
                    //Sets the selected Fragment into the Framelayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.UserFragmentContainer, fragment).commit();
                    return true;
                }
            };


    @Override
    protected void onStart() {
        super.onStart();
        //checking user already logged or not
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            startActivity(intent);
            finish();
        } else {
            //Checks for user Role and starts the appropriate activity
            String id = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(id).child("role");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.getValue().toString() != null) {
                        String data = snapshot.getValue().toString();

                        Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                        if (data.equals("Admin")) {
                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            //do nothing
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}