package com.pratham.atm.domain;


import android.arch.persistence.room.Embedded;

import java.util.List;

public class AssessmentTestModal {
    private String subjectname;
    @Embedded
    private List<AssessmentTest> lstsubjectexam;

    private String subjectid;

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public List<AssessmentTest> getLstsubjectexam() {
        return lstsubjectexam;
    }

    public void setLstsubjectexam(List<AssessmentTest> lstsubjectexam) {
        this.lstsubjectexam = lstsubjectexam;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

}

