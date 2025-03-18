package com.example.vacationscheduler.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.MainActivity;
import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private RecyclerView recyclerView;
    private VacationAdapter vacationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewVacationList);


        navToCreateVacayForm();
//        setupRecyclerView();
    }

    public void navToCreateVacayForm() {
        FloatingActionButton floatingActionButton = findViewById(R.id.vacationDetailsBtn);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu); // Ensure "your_menu_file" is your actual XML file name
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.addSampleData){
            repository = new Repository(getApplication());
            Toast.makeText(VacationList.this, "Put in sample data", Toast.LENGTH_LONG).show();
            Vacation vacation = new Vacation(0, "Hawaii", "Hilton", "10/4/2023", "10/5/2023");
            Excursion excursion = new Excursion(0, "Swimming", "10/4/2024", 1);
            repository.insert(vacation);
            repository.insert(excursion);
            return true;
        }

        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        return false;
    }


    private void setupRecyclerView(){
        repository = new Repository(getApplication());
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void loadVacations(){
        List<Vacation> allVacations = repository.getAllVacation();
        vacationAdapter.setVacation(allVacations);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
        loadVacations();
    }

}