package com.pratham.atm.ui.choose_assessment.result;

public interface ResultContract {
    interface ResultPresenter {


        String getStudent(String studentId);

        String getSubjectName(String examId);

        String getTopicName(String examId);
    }

    interface ResultView {


    }

}
