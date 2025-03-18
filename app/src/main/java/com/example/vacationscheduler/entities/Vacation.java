package com.example.vacationscheduler.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationId;
    private String title;
    private String hotel;
    private String startDate;
    private String endDate;

    public Vacation(int vacationId, String title, String hotel, String startDate, String endDate) {
        this.vacationId = vacationId;
        this.title = title;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
