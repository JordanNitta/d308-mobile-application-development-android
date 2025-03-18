package com.example.vacationscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.vacationscheduler.UI.ExcursionDetails;
import com.example.vacationscheduler.UI.VacationList;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    public static int numAlert;
    // variables
    DrawerLayout drawerLayout;
    ImageButton buttonToggle;
    NavigationView navigationView;
    Button enterApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        buttonToggle = findViewById(R.id.buttonToggle);
        navigationView = findViewById(R.id.navView);
        enterApplication = findViewById(R.id.enterApplication);



        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START); // Open the drawer on the left side
            }
        });

        enterApplication.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, VacationList.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 int itemId = item.getItemId();

                // Handle menu item clicks here
                if (itemId == R.id.home) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
//                    Toast.makeText(MainActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.vacation) {
                    Intent intent = new Intent(MainActivity.this, VacationList.class);
                    startActivity(intent);
//                    Toast.makeText(MainActivity.this, "Vacation Clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });


        }
    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }



    }



