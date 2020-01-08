package com.pratham.atm.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pratham.atm.AssessmentApplication;
import com.pratham.atm.custom.FastSave;
import com.pratham.atm.database.BackupDatabase;
import com.pratham.atm.ui.splash_activity.SplashActivity;
import com.pratham.atm.ui.splash_activity.SplashActivity_;

import static com.pratham.atm.BaseActivity.appDatabase;

public class AppExitService extends Service {

//    private AppDatabase appDatabase;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        try {

            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {

                        /*appDatabase = Room.databaseBuilder(AppExitService.this,
                                AppDatabase.class, AppDatabase.DB_NAME)
                                .build();*/

                        String EndTime = "" + AssessmentApplication.getCurrentDateTime();
                        String currentSession = FastSave.getInstance().getString("currentSession", "");

//                        String toDateTemp = appDatabase.getSessionDao().getToDate(Assessment_Constants.currentSession);
                        String toDateTemp = appDatabase.getSessionDao().getToDate(currentSession);

                        if (toDateTemp.equalsIgnoreCase("na")) {
//                            appDatabase.getSessionDao().UpdateToDate(Assessment_Constants.currentSession, EndTime);
                            appDatabase.getSessionDao().UpdateToDate(currentSession, EndTime);
                        }

                        // String assessmentSession = FastSave.getInstance().getString("assessmentSession", "");

//                        String toDateAssessment = appDatabase.getSessionDao().getToDate(Assessment_Constants.assessmentSession);
                       /* String toDateAssessment = appDatabase.getSessionDao().getToDate(assessmentSession);
                        if (toDateAssessment.equalsIgnoreCase("na")) {
                            appDatabase.getSessionDao().UpdateToDate(assessmentSession, EndTime);
                        }*/


                        BackupDatabase.backup(AppExitService.this);
                        stopService(new Intent(AppExitService.this, SplashActivity_.class));
                        stopSelf();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}