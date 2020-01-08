package com.pratham.atm.ui.choose_assessment.science.interfaces;

import android.content.Intent;

import com.pratham.atm.domain.ScienceQuestion;
import com.pratham.atm.domain.ScienceQuestionChoice;

import java.util.List;

public interface AssessmentAnswerListener {


    void setAnswerInActivity(String ansId, String answer, String qid, List<ScienceQuestionChoice> list);
//    void setVideoResult(Intent intent, int videoCapture, ScienceQuestion scienceQuestion);
    void setAudio(String fileName, boolean isRecording);
}
