package com.pratham.atm.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pratham.atm.ui.choose_assessment.science.ScienceAssessmentActivity_;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, ScienceAssessmentActivity_.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}
