package com.pratham.atm.ui.choose_assessment;


import com.pratham.atm.domain.AssessmentSubjects;

import java.util.List;

public interface ChooseAssessmentContract {

    public interface ChooseAssessmentView{
        void clearContentList();

        void addContentToViewList(List<AssessmentSubjects> contentTable);

        void notifyAdapter();
    }

    public interface ChooseAssessmentPresenter{
        public void startActivity(String activityName);

        void copyListData();

        void clearNodeIds();

        void endSession();

//        void startAssessSession();
    }

}
