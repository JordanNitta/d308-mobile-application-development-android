package com.example.vacationscheduler.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.example.vacationscheduler.util.Converters;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VacationDetails extends AppCompatActivity {
    Repository repository;
    private RecyclerView recyclerView;
    private ExcursionAdapter excursionAdapter;
    int vacationId;
    EditText editTitle, editHotel;
    String title, hotel, startDate, endDate;
    Button editVacationStartDate, editVacationEndDate;
    TextView dateValidationError;
    TextView titleValidationError;
    TextView hotelValidationError;
    DatePickerDialog.OnDateSetListener myStartDate;
    DatePickerDialog.OnDateSetListener myEndDate;

    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        dateValidationError = findViewById(R.id.dateErrorMsg);
        titleValidationError = findViewById(R.id.titleErrMsg);
        hotelValidationError = findViewById(R.id.hotelErrMsg);

        editTitle = findViewById(R.id.editVacationTitle);
        editHotel = findViewById(R.id.editVacationHotel);
        editVacationStartDate = findViewById(R.id.editVacationStartDate);
        editVacationEndDate = findViewById(R.id.editVacationEndDate);

        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");

        editTitle.setText(title);
        editHotel.setText(hotel);

        vacationId = getIntent().getIntExtra("id", -1);

        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        // Get the current date
        String currentDate = Converters.getStringFromDate(new Date());

        editVacationStartDate.setText(currentDate);
        editVacationEndDate.setText(currentDate);

        // Get the button view
        editVacationStartDate.setText(startDate);
        editVacationEndDate.setText(endDate);

        recyclerView = findViewById(R.id.excursionRecyclerView);
//        repository = new Repository(getApplication());
//        excursionAdapter = new ExcursionAdapter(this);
//        recyclerView.setAdapter(excursionAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        setUpExcursionRecyclerView();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton createVacation = findViewById(R.id.createVacationDetail);

        createVacation.setOnClickListener(v -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("vacayId", vacationId);
            intent.putExtra("startDate", startDate);
            intent.putExtra("endDate", endDate);
            startActivity(intent);
        });

//        setUpExcursionRecyclerView();


        editVacationStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Converters.parseDateFromString(editVacationStartDate.getText().toString());
                calendarStart.setTime(date);
                new DatePickerDialog(
                        VacationDetails.this,
                        myStartDate,
                        calendarStart.get(Calendar.YEAR),
                        calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        editVacationEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Converters.parseDateFromString(editVacationEndDate.getText().toString());
                calendarEnd.setTime(date);
                new DatePickerDialog(VacationDetails.this, myEndDate, calendarEnd.get(Calendar.YEAR),
                        calendarEnd.get(Calendar.MONTH), calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Going to update the calendarStart date with the date the user selected
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartDateLabel();
            }
        };

        myEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Going to update the calendarEnd date with the date the user selected
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, month);
                calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndDateLabel();
            }
        };
    }

    public void updateStartDateLabel() {
        editVacationStartDate.setText(Converters.getStringFromDate(calendarStart.getTime()));
    }

    public void updateEndDateLabel() {
        editVacationEndDate.setText(Converters.getStringFromDate(calendarEnd.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    private void createVacationId() {
        if (repository.getAllVacation().isEmpty()) {
            vacationId = 1;
        } else {
            vacationId = repository.getAllVacation().get(repository.getAllVacation().size() - 1).getVacationId() + 1;
        }
    }

    private void manageSaveOrUpdateVacation() {
        repository = new Repository(getApplication());
//        TextView dateValidationError = findViewById(R.id.dateErrorMsg);
//        TextView titleValidationError = findViewById(R.id.titleErrMsg);
//        TextView hotelValidationError = findViewById(R.id.hotelErrMsg);

        Date startDate = Converters.parseDateFromString(editVacationStartDate.getText().toString());
        Date endDate = Converters.parseDateFromString(editVacationEndDate.getText().toString());

        if (!validateEndDatAfterStartDate(startDate, endDate, dateValidationError)) {
            // Show an error message and prevent saving if the validation fails
            Toast.makeText(VacationDetails.this, "End date must be after start date", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validateHotel(editHotel, hotelValidationError)) {
            Toast.makeText(VacationDetails.this, "Hotel Required", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validateTitle(editTitle, titleValidationError)) {
            Toast.makeText(VacationDetails.this, "Title Required", Toast.LENGTH_LONG).show();
            return;
        }

        if (vacationId == -1) {
            // Want to create a new vacation using the createVacationId function
            createVacationId();
            saveVacation();
        } else {
            updateVacation();
        }
        this.finish();
    }

    private void saveVacation() {
        Vacation vacation = new Vacation(
                vacationId,
                editTitle.getText().toString(),
                editHotel.getText().toString(),
                editVacationStartDate.getText().toString(),
                editVacationEndDate.getText().toString()
        );
        repository.insert(vacation);
        Toast.makeText(VacationDetails.this, editTitle.getText().toString() + " Was Successfully Added", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
//        intent.putExtra("vacayId", vacationId);
//        startActivity(intent);
    }

    private void updateVacation() {
        Vacation vacation = new Vacation(
                vacationId,
                editTitle.getText().toString(),
                editHotel.getText().toString(),
                editVacationStartDate.getText().toString(),
                editVacationEndDate.getText().toString()
        );
        repository.update(vacation);
        Toast.makeText(VacationDetails.this, editTitle.getText().toString() + " Was Successfully Updated", Toast.LENGTH_LONG).show();
    }

    private void manageDeleteVacation() {
        repository = new Repository(getApplication());
        Vacation vacation = new Vacation(
                vacationId,
                editTitle.getText().toString(),
                editHotel.getText().toString(),
                editVacationStartDate.getText().toString(),
                editVacationEndDate.getText().toString()
        );
        List<Excursion> associatedExcursions = repository.getAssociatedExcursion(vacationId);

        if (!associatedExcursions.isEmpty()) {
            Toast.makeText(VacationDetails.this, "Excursions are still associated with the vacation", Toast.LENGTH_LONG).show();
        } else {
            repository.delete(vacation);
            Toast.makeText(VacationDetails.this, editTitle.getText().toString() + " Was Successfully Deleted", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }

    // Validations
    private boolean validateEndDatAfterStartDate(Date startDate, Date endDate, TextView dateError) {
        // Check if the end date is before the start date
        if (endDate.before(startDate)) {
            // Display the error message in the TextView
            dateError.setText("End date must be after start date");
            // Make the error visible
            dateError.setVisibility(View.VISIBLE);
            // Correct validation
            return false;
        } else {
            // Clear error message
            dateError.setText("");
            // Hide the message
            dateError.setVisibility(View.GONE);
            // Validation passed
            return true;
        }
    }

    private boolean validateTitle(EditText titleInput, TextView titleError) {
        String title = titleInput.getText().toString();
        if (title.isEmpty()) {
            titleError.setText("Title Required");
            titleError.setVisibility(View.VISIBLE);
            return false;
        } else {
            titleError.setText("");
            titleError.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validateHotel(EditText hotelInput, TextView hotelError) {
        String title = hotelInput.getText().toString();
        if (title.isEmpty()) {
            hotelError.setText("Hotel Required");
            hotelError.setVisibility(View.VISIBLE);
            return false;
        } else {
            hotelError.setText("");
            hotelError.setVisibility(View.GONE);
            return true;
        }
    }

    // start vacation notification

    public void notifyStartVacation() {
        // get the start from the vacation field
        String startDate = editVacationStartDate.getText().toString();
//        String vacationTitle = editTitle.getText().toString();
        Date start = Converters.parseDateFromString(startDate);
//        Date currentDate = Converters.parseDateFromString(Converters.getStringFromDate(new Date()));

        scheduleVacationNotification(start.getTime(), "Vacation Starting:", editTitle.getText().toString() + " starts " + startDate);

//        else if (currentDate.getTime().before(start)) {
//            Toast.makeText(VacationDetails.this, "Won't a", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(VacationDetails.this, "Can't schedule an start alert due to being pass start date", Toast.LENGTH_SHORT).show();
//        }
    }

    public void notifyEndVacation() {
        // get the enddate from the vacation field
        String endDate = editVacationEndDate.getText().toString();
//        String vacationTitle = editTitle.getText().toString();
        Date end = Converters.parseDateFromString(endDate);
//        Date currentDate = Converters.parseDateFromString(Converters.getStringFromDate(new Date()));
        scheduleVacationNotification(end.getTime(), "Vacation Ending:", editTitle.getText().toString() + " ends " + endDate);

//        Toast.makeText(VacationDetails.this, "Can't schedule an alert due to being pass end date", Toast.LENGTH_SHORT).show();
    }

    public void scheduleVacationNotification(long triggerTime, String alert, String message) {

        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        // more data to add
        intent.putExtra("alertType", alert);
        // create the message
        intent.putExtra("message", message);
        PendingIntent sender = PendingIntent.getBroadcast(
                VacationDetails.this,
                ++MainActivity.numAlert, intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // set notification to go off
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
    }

    private void shareVacation() {
        String vacayTitle = editTitle.getText().toString();
        String vacayHotel = editHotel.getText().toString();
        String vacayStartDate = editVacationStartDate.getText().toString();
        String vacayEndDate = editVacationEndDate.getText().toString();
        String vacationDetail = "Vacation: " + vacayTitle + "\n" +
                "Hotel: " + vacayHotel + "\n" +
                "Start Date: " + vacayStartDate + "\n" +
                "End Date: " + vacayEndDate + "\n";

        for (Excursion e : repository.getAllExcursion()) {
            if (e.getVacationId() == vacationId) {
                vacationDetail += "Excursion: " + e.getExcursionTitle() + "\n" +
                        "Date: " + e.getExcursionDate() + "\n";
            }
        }



        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, vacationDetail);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveVacation) {
            manageSaveOrUpdateVacation();
            return true;
        } else if (item.getItemId() == R.id.deleteVacation) {
            manageDeleteVacation();
            return true;
        } else if (item.getItemId() == R.id.notifyStart) {
            notifyStartVacation();
            return true;
        } else if (item.getItemId() == R.id.notifyEnd) {
            notifyEndVacation();
            return true;
        } else if (item.getItemId() == R.id.share) {
            shareVacation();
        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpExcursionRecyclerView() {
        repository = new Repository(getApplication());
        excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursion()) {
            if (e.getVacationId() == vacationId) {
                filteredExcursions.add(e);
            }
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

//    private void loadAssociatedExcursion(){
//        List<Excursion> allAssociatedExcursion = repository.getAssociatedExcursion(vacationId);
//        excursionAdapter.setExcursions(allAssociatedExcursion);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpExcursionRecyclerView();
    }
}