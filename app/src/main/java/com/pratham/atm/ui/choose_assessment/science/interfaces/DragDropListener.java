package com.pratham.atm.ui.choose_assessment.science.interfaces;

import com.pratham.atm.domain.ScienceQuestionChoice;

import java.util.List;

public interface DragDropListener {
    void setList(List<ScienceQuestionChoice> list, String qid);
}
