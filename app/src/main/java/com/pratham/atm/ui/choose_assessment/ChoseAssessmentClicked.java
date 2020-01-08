package com.pratham.atm.ui.choose_assessment;

import com.pratham.atm.domain.AssessmentLanguages;
import com.pratham.atm.domain.AssessmentSubjects;
import com.pratham.atm.domain.AssessmentTest;

public interface ChoseAssessmentClicked {
    public void subjectClicked(int position, AssessmentSubjects nodeId);
    void languageClicked(int pos, AssessmentLanguages languages);
    void topicClicked(int pos, AssessmentTest test);
}
