package com.dataflair.librarymanagementapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.dataflair.librarymanagementapp.Fragments.AddBooksFragment;
import com.dataflair.librarymanagementapp.Fragments.AdminDashBoardFragment;
import com.dataflair.librarymanagementapp.Fragments.AdminProfileFragment;
import com.dataflair.librarymanagementapp.Fragments.ApprovedFragment;
import com.dataflair.librarymanagementapp.Fragments.EditBookDetailsFragment;
import com.dataflair.librarymanagementapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Assigning framelayout resource file to show appropriate fragment using address
        frameLayout = (FrameLayout) findViewById(R.id.AdminFragmentContainer);

        //Assigining Bottomnavigaiton Menu
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.AdminBottomNavigationView);
        Menu menuNav = bottomNavigationView.getMenu();


        //Setting the default fragment as HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.AdminFragmentContainer, new AddBooksFragment()).commit();


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
                        case R.id.AddBookMenu:
                            fragment = new AddBooksFragment();
                            break;
                        case R.id.EditDetailsMenu:
                            fragment = new EditBookDetailsFragment();
                            break;
                        case R.id.DashBoardMenu:
                            fragment=new AdminDashBoardFragment();
                            break;
                        case R.id.AdminProfile:
                            fragment = new AdminProfileFragment();
                            break;
                        case R.id.Approved_Books:
                            fragment = new ApprovedFragment();
                            break;

                    }
                    //Sets the selected Fragment into the Framelayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.AdminFragmentContainer, fragment).commit();
                    return true;
                }
            };

}