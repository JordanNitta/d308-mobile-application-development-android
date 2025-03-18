package com.example.vacationscheduler.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.vacationscheduler.MainActivity;
import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.util.Converters;

import java.util.Calendar;
import java.util.Date;

public class ExcursionDetails extends AppCompatActivity {
    private Repository repository;
    int excursionId;
    int vacayId;
    EditText editExcurTitle;
    Button editExcurDate;
    String excurTitle;
    String excurDate;
    TextView excurDateErrMsg;
    String vacationStartDate;
    String vacationEndDate;
    Date vacayStart;
    Date vacayEnd;
    DatePickerDialog.OnDateSetListener myExcurStartDate;
    TextView excurTitleValidationError;

    final Calendar calendarStart = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);

        repository = new Repository(getApplication());

        editExcurTitle = findViewById(R.id.editExcursionTitle);
        editExcurDate = findViewById(R.id.editExcursionDate);
        excurDateErrMsg = findViewById(R.id.excurDateErrMsg);
        excurTitleValidationError = findViewById(R.id.excurTitleErrMsg);
        excurTitle = getIntent().getStringExtra("title");
        excurDate = getIntent().getStringExtra("date");
        excursionId = getIntent().getIntExtra("id", -1);
        vacayId = getIntent().getIntExtra("vacayId", -1);

        for (Vacation vacation: repository.getAllVacation()){
            if(vacation.getVacationId() == vacayId){
                vacationStartDate = vacation.getStartDate();
                vacationEndDate = vacation.getEndDate();
            }
        }

        editExcurTitle.setText(excurTitle);

        String currentDate = Converters.getStringFromDate(new Date());
        editExcurDate.setText(currentDate);

        editExcurTitle.setText(excurTitle);
        editExcurDate.setText(excurDate);

        final Calendar calendarStart = Calendar.getInstance();

        vacayStart = Converters.parseDateFromString(vacationStartDate);
        vacayEnd = Converters.parseDateFromString(vacationEndDate);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myExcurStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String datePicked = Converters.getStringFromDate(calendarStart.getTime());
                editExcurDate.setText(datePicked);
            }
        };

        editExcurDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Converters.parseDateFromString(editExcurDate.getText().toString());
                calendarStart.setTime(date);
                new DatePickerDialog(
                        ExcursionDetails.this,
                        myExcurStartDate,
                        calendarStart.get(Calendar.YEAR),
                        calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    private void createExcursionId(){
        if(repository.getAllExcursion().isEmpty()){
            excursionId = 1;
        } else {
            excursionId = repository.getAllExcursion().get(repository.getAllExcursion().size() - 1).getExcursionId() + 1;
        }
    }

    private boolean validateExcurBetweenVacation(Date excurDate, Date vacayStart, Date vacayEnd, TextView dateErr){
        if(excurDate.before(vacayStart) || excurDate.after(vacayEnd)){
            dateErr.setText("Excursion date must be between vacation date");
            dateErr.setVisibility(View.VISIBLE);
            return false;
        } else {
            dateErr.setText("");
            dateErr.setVisibility(View.GONE);
            return true;
        }
    }

    private void manageSaveOrUpdateExcursion(){
        repository = new Repository(getApplication());

        Date excursionDate = Converters.parseDateFromString(editExcurDate.getText().toString());
        Date vacayStart = Converters.parseDateFromString(vacationStartDate);
        Date vacayEnd = Converters.parseDateFromString(vacationEndDate);

//        System.out.println("Excursion Date: " + excursionDate);
//        System.out.println("Vacation Start Date: " + vacayStart);
//        System.out.println("Vacation End Date: " + vacayEnd);

        if(!validateExcurTitle(editExcurTitle, excurTitleValidationError)){
            Toast.makeText(ExcursionDetails.this, "Excursion Title Required", Toast.LENGTH_LONG).show();
            return;
        }

        if(!validateExcurBetweenVacation(excursionDate, vacayStart, vacayEnd, excurDateErrMsg)){
            Toast.makeText(ExcursionDetails.this, "Excursion needs to be between vacation dates", Toast.LENGTH_LONG).show();
            return;
        }

        repository = new Repository(getApplication());
        if (excursionId == -1) {
            createExcursionId();
            saveAssociatedExcursion();
        } else {
            updateExcursion();
        }
        this.finish();
    }


    private void saveAssociatedExcursion(){
        Excursion excursion = new Excursion(
                excursionId,
                editExcurTitle.getText().toString(),
                editExcurDate.getText().toString(),
                vacayId
        );
        repository.insert(excursion);
        Toast.makeText(ExcursionDetails.this,  editExcurTitle.getText().toString() + " Was Successfully Added", Toast.LENGTH_LONG).show();
//        this.finish();
    }

    private void updateExcursion(){
        Excursion excursion = new Excursion(
                excursionId,
                editExcurTitle.getText().toString(),
                editExcurDate.getText().toString(),
                vacayId
        );
        repository.update(excursion);
        Toast.makeText(ExcursionDetails.this,  editExcurTitle.getText().toString() + " Was Successfully Updated", Toast.LENGTH_LONG).show();
    }

    private void deleteExcursion(){
        repository = new Repository(getApplication());
        Excursion excursion = new Excursion(
                excursionId,
                editExcurTitle.getText().toString(),
                editExcurDate.getText().toString(),
                vacayId
        );
        repository.delete(excursion);
        Toast.makeText(ExcursionDetails.this,  editExcurTitle.getText().toString() + " Was Successfully Deleted", Toast.LENGTH_LONG).show();
        this.finish();
    }

    private boolean validateExcurTitle(EditText titleInput, TextView textView){
        String title = titleInput.getText().toString();
        if(title.isEmpty()){
            textView.setText("Excursion Title Required");
            textView.setVisibility(View.VISIBLE);
            return false;
        } else {
            textView.setText("");
            textView.setVisibility(View.GONE);
            return true;
        }
    }

    public void notifyStartExcursion(){
        // Get the start date from the excursion field
        String startDate = editExcurDate.getText().toString();
        Date startExcur = Converters.parseDateFromString(startDate);
        // Get the excursion title (assuming it's stored in editExcurTitle)
        String excursionTitle = editExcurTitle.getText().toString();
//        Date currentDate = Converters.parseDateFromString(Converters.getStringFromDate(new Date()));

        schuduleExcursionNotification(startExcur.getTime(), "Excursion Starting", "Excursion " + excursionTitle + " is starting " + startDate);

//        if(currentDate.equals(startExcur)){
//            schuduleExcursionNotification(startExcur.getTime(), "Excursion Starting", "Excursion " + excursionTitle + " is starting " + startDate);
//        }
        // Schedule the notification with the excursion title in the message
    }

    public void schuduleExcursionNotification(long triggerTime, String alert, String message){
        Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
        // more data to add
        intent.putExtra("alertType", alert);
        // create the message
        intent.putExtra("message", message);
        PendingIntent sender = PendingIntent.getBroadcast(
                ExcursionDetails.this,
                ++MainActivity.numAlert, intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // set notification to go off
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.saveExcursion){
            manageSaveOrUpdateExcursion();
        } else if (item.getItemId() == R.id.deleteExcursion) {
            deleteExcursion();
        } else if (item.getItemId() == R.id.notifyExcurStart) {
            notifyStartExcursion();
        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

