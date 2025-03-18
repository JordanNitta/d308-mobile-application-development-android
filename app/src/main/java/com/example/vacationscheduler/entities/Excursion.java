
package com.example.vacationscheduler.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionId;
    private String excursionTitle;
    // refactored my field names
    private String excursionDate;
    private int vacationId;

    public Excursion(int excursionId, String excursionTitle, String excursionDate, int vacationId) {
        this.excursionId = excursionId;
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.vacationId = vacationId;
    }

    public int getVacationId() {
        return vacationId;
    }

    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }


    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}