package com.example.vacationscheduler.database;

import android.app.Application;

import com.example.vacationscheduler.dao.ExcursionDao;
import com.example.vacationscheduler.dao.VacationDao;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private VacationDao mVacationDao;
    private ExcursionDao mExcursionDao;

    private List<Vacation> mAllVacation;
    private List<Excursion> mAllExcursion;

    // If more then four task are submitted they will wait in a queue until a thread is available
    private static final int NUMBER_OF_THREADS = 4;
    // The Executors can open thread from the thread pool that why
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mVacationDao = db.vacationDao();
        mExcursionDao = db.excursionDao();
    }

    // When you need to get things from the database we will need to use an Executive
    public List<Vacation> getAllVacation(){
        databaseExecutor.execute(() -> {
            mAllVacation = mVacationDao.getAllVacations();
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAllVacation;
    }

    public void insert(Vacation vacation){
        databaseExecutor.execute(() -> {
            mVacationDao.insert(vacation);
        });
    }

    public void update(Vacation vacation){
        databaseExecutor.execute(() -> {
            mVacationDao.update(vacation);
        });
    }

    public void delete(Vacation vacation){
        databaseExecutor.execute(() -> {
            mVacationDao.delete(vacation);
        });
    }

    public List<Excursion> getAssociatedExcursion(int vacationId) {
        databaseExecutor.execute(() -> {
            mAllExcursion = mExcursionDao.getAssociatedExcursions(vacationId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursion;
    }

    public List<Excursion> getAllExcursion(){
        databaseExecutor.execute(() -> {
            mAllExcursion = mExcursionDao.getAllExcursions();
        });
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        return mAllExcursion;

    }

    public void insert(Excursion excursion){
        databaseExecutor.execute(() -> {
            mExcursionDao.insert(excursion);
        });
    }

    public void update(Excursion excursion){
        databaseExecutor.execute(() -> {
            mExcursionDao.update(excursion);
        });
    }

    public void delete(Excursion excursion){
        databaseExecutor.execute(() -> {
            mExcursionDao.delete(excursion);
        });
    }

}
