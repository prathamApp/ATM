package com.pratham.atm.ui.choose_assessment.science.certificate.CertificateSubjects;

public interface CertificateContract {
    public interface CertificatePresenter {
        void getStudentName();

//        void getStudentAvatar();

//        void getScoreData(String paperId);

        float getRating(String level, String paperId);

        void setView(CertificateContract.CertificateView certificateView);

//        void getPaper(String examId,String subjectid);
    }

    interface CertificateView {
        void setStudentName(String name);

        void setStudentAvatar(String studentAvatar);
    }
}
