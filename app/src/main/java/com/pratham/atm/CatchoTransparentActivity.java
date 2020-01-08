package com.pratham.atm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pratham.atm.custom.FastSave;
import com.pratham.atm.database.AppDatabase;
import com.pratham.atm.database.BackupDatabase;
import com.pratham.atm.domain.Modal_Log;
import com.pratham.atm.utilities.Assessment_Utility;


import net.alhazmy13.catcho.library.Catcho;
import net.alhazmy13.catcho.library.error.CatchoError;

public class CatchoTransparentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                CatchoError error = (CatchoError) getIntent().getSerializableExtra(Catcho.ERROR);
                Modal_Log log = new Modal_Log();
                log.setCurrentDateTime(Assessment_Utility.GetCurrentDateTime());
                log.setErrorType("ERROR");
                log.setExceptionMessage(error.toString());
                log.setExceptionStackTrace(error.getError());
                log.setMethodName("NO_METHOD");
                log.setGroupId(FastSave.getInstance().getString("", "no_group"));
                log.setDeviceId("");
                AppDatabase.getDatabaseInstance(CatchoTransparentActivity.this).getLogsDao().insertLog(log);
                new BackupDatabase().backup(CatchoTransparentActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                finishAffinity();
            }
        }.execute();
    }
}
