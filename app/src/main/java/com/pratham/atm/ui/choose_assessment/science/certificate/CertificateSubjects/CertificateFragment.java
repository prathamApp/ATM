package com.pratham.atm.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.pratham.atm.AssessmentApplication;
import com.pratham.atm.R;
import com.pratham.atm.database.AppDatabase;
import com.pratham.atm.domain.AssessmentPaperForPush;
import com.pratham.atm.domain.AssessmentPaperPattern;
import com.pratham.atm.utilities.APIs;
import com.pratham.atm.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@EFragment(R.layout.fragment_certificate)
public class CertificateFragment extends Fragment implements CertificateContract.CertificateView {


    @ViewById(R.id.q1_label)
    TextView q1_label;
    @ViewById(R.id.q2_label)
    TextView q2_label;
    @ViewById(R.id.q3_label)
    TextView q3_label;

    @ViewById(R.id.iv_student)
    ImageView iv_student;
    @ViewById(R.id.tv_name)
    TextView tv_studentName;
    @ViewById(R.id.tv_correct_cnt)
    TextView tv_correct_cnt;
    @ViewById(R.id.tv_wrong_cnt)
    TextView tv_wrong_cnt;
    @ViewById(R.id.tv_skip_cnt)
    TextView tv_skip_cnt;
    @ViewById(R.id.tv_total_time)
    TextView tv_total_time;
    @ViewById(R.id.tv_time_taken)
    TextView tv_time_taken;
    /* @ViewById(R.id.tv_total_marks)
     TextView tv_total_marks;
     @ViewById(R.id.tv_student_marks)
     TextView tv_student_marks;*/
    @ViewById(R.id.tv_total_cnt)
    TextView tv_total_cnt;
    @ViewById(R.id.frame_fragment_certificate)
    FrameLayout frame_fragment_certificate;
    @ViewById(R.id.q1_ratingStars)
    RatingBar q1_ratingStars;
    @ViewById(R.id.q2_ratingStars)
    RatingBar q2_ratingStars;
    @ViewById(R.id.q3_ratingStars)
    RatingBar q3_ratingStars;
    @Bean(CertificatePresenterImpl.class)
    CertificateContract.CertificatePresenter presenter;
    AssessmentPaperForPush assessmentPaperForPush;

    public CertificateFragment() {
    }

    @AfterViews
    public void init() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            assessmentPaperForPush = (AssessmentPaperForPush) bundle.getSerializable("assessmentPaperForPush");
        }
        frame_fragment_certificate.setBackground(getResources().getDrawable(Assessment_Utility.getRandomCertificateBackground(getActivity())));
        presenter.setView(this);
//        presenter = new CertificatePresenterImpl(getActivity());

        generateCertificate();
//        presenter.getScoreData(assessmentPaperForPush.getPaperId());
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            assessmentPaperForPush = (AssessmentPaperForPush) bundle.getSerializable("assessmentPaperForPush");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_certificate, container, false);
    }*/

   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        frame_fragment_certificate.setBackground(getResources().getDrawable(Assessment_Utility.getRandomCertificateBackground(getActivity())));
        presenter = new CertificatePresenterImpl(getActivity(), this);
        generateCertificate();
//        presenter.getScoreData(assessmentPaperForPush.getPaperId());
    }*/

    private void generateCertificate() {
//        try {
        presenter.getStudentName();
        setQuestionStars();
//        String examId = assessmentPaperForPush.getExamId();
//        JSONArray questionList = fetchQuestionsFromAssets("certificate_questions.json", "QuestionList");
//            JSONArray topics = fetchQuestionsFromAssets("certificate_topics.json", "topicList");
//        String topicName = AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPatternDetailsDao().getTopicNameByExamId(examId);
//            String langId = assessmentPaperForPush.getLanguageId();
//        int langId = Integer.parseInt(assessmentPaperForPush.getLanguageId());
            /*for (int t = 0; t < topics.length(); t++) {
                JSONObject jsonObject = topics.getJSONObject(t);
                if (jsonObject.get("1").toString().equalsIgnoreCase(topicName)) {
                    topicName = jsonObject.get(langId + "").toString();
                }
            }*/

//            String examName = assessmentPaperForPush.getExamName();
//            JSONObject jsonObject = jsonArray.getJSONObject(langId);

        AssessmentPaperPattern assessmentPaperPattern = AppDatabase.getDatabaseInstance(getActivity())
                .getAssessmentPaperPatternDao().getAllAssessmentPaperPatternsBySubIdAndExamId(assessmentPaperForPush.getSubjectId(), assessmentPaperForPush.getExamId());
        if (assessmentPaperPattern != null) {
            Log.d(";;;;", assessmentPaperPattern.getCertificateQuestion1() + "");
            q1_label.setText(assessmentPaperPattern.getCertificateQuestion1() + "");
            q2_label.setText(assessmentPaperPattern.getCertificateQuestion2() + "");
            q3_label.setText(assessmentPaperPattern.getCertificateQuestion3() + "");
        } else {
            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                downloadPaperPattern(assessmentPaperForPush.getExamId());
            else {
                q1_label.setText("");
                q2_label.setText("");
                q3_label.setText("");
            }
        }

          /*  for (int i = 0; i < questionList.length(); i++) {
                JSONObject jsonObject = questionList.getJSONObject(i);
                if (jsonObject.get("langId").toString().equals(langId + "")) {
                    String q1 = jsonObject.get("question1") + topicName + jsonObject.get("end_question1");
                    q1_label.setText(q1);
                    String q2 = jsonObject.get("question2") + topicName + jsonObject.get("end_question2");
                    q2_label.setText(q2);
                    String q3 = jsonObject.get("question3") + topicName + jsonObject.get("end_question3");
                    q3_label.setText(q3);
                } else {
                    Log.d("##", jsonObject.get("langId") + "");
                }
            }*/

     /*   } catch (JSONException e) {
            e.printStackTrace();
        }*/

        tv_correct_cnt.setText("" + assessmentPaperForPush.getCorrectCnt());
        int totalWrong = assessmentPaperForPush.getWrongCnt() + assessmentPaperForPush.getSkipCnt();
        tv_wrong_cnt.setText("" + totalWrong);
        tv_skip_cnt.setText("" + assessmentPaperForPush.getSkipCnt());
        tv_total_time.setText(assessmentPaperForPush.getExamTime() + " mins.");
        calculateTime(assessmentPaperForPush.getPaperStartTime(), assessmentPaperForPush.getPaperEndTime());
//        tv_student_marks.setText(assessmentPaperForPush.getTotalMarks());
//        tv_total_marks.setText(assessmentPaperForPush.getOutOfMarks());
        int totalCnt = assessmentPaperForPush.getCorrectCnt() + assessmentPaperForPush.getWrongCnt() + assessmentPaperForPush.getSkipCnt();
        tv_total_cnt.setText(totalCnt + "");
//        presenter.getPaper(assessmentPaperForPush.getExamId(), assessmentPaperForPush.getSubjectId());
    }

    private void downloadPaperPattern(String examId) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setMessage("Downloading paper pattern...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentPaperPatternAPI + examId)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        AssessmentPaperPattern assessmentPaperPattern = gson.fromJson(response, AssessmentPaperPattern.class);
                        if (assessmentPaperPattern != null) {
                            AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperPatternDao().insertPaperPattern(assessmentPaperPattern);
                            Log.d(";;;;", assessmentPaperPattern.getCertificateQuestion1() + "");
                            q1_label.setText(assessmentPaperPattern.getCertificateQuestion1() + "");
                            q2_label.setText(assessmentPaperPattern.getCertificateQuestion2() + "");
                            q3_label.setText(assessmentPaperPattern.getCertificateQuestion3() + "");
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error downloading paper pattern..", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setQuestionStars() {
        float q1Rating, q2Rating, q3Rating;
        String paperId = assessmentPaperForPush.getPaperId();
        if (assessmentPaperForPush.getQuestion1Rating() != null
                && !assessmentPaperForPush.getQuestion1Rating().trim().equalsIgnoreCase("") && !assessmentPaperForPush.getQuestion1Rating().trim().equalsIgnoreCase("null")) {
            q1Rating = Float.parseFloat(assessmentPaperForPush.getQuestion1Rating());
        } else {
            q1Rating = presenter.getRating("1", paperId);
        }
        q1_ratingStars.setRating(q1Rating);

        if (assessmentPaperForPush.getQuestion2Rating() != null
                && !assessmentPaperForPush.getQuestion2Rating().trim().equalsIgnoreCase("") && !assessmentPaperForPush.getQuestion2Rating().trim().equalsIgnoreCase("null")) {
            q2Rating = Float.parseFloat(assessmentPaperForPush.getQuestion2Rating());
        } else {
            q2Rating = presenter.getRating("2", paperId);
        }
        q2_ratingStars.setRating(q2Rating);

        if (assessmentPaperForPush.getQuestion3Rating() != null
                && !assessmentPaperForPush.getQuestion3Rating().trim().equalsIgnoreCase("") && !assessmentPaperForPush.getQuestion3Rating().trim().equalsIgnoreCase("null")) {
            q3Rating = Float.parseFloat(assessmentPaperForPush.getQuestion3Rating());
        } else {
            q3Rating = presenter.getRating("3", paperId);
        }
        q3_ratingStars.setRating(q3Rating);

        AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperForPushDao().setAllRatings("" + q1Rating, "" + q2Rating, "" + q3Rating, paperId);

    }


   /* public JSONArray fetchQuestionsFromAssets(String fileName, String nodeTitle) {
        JSONArray returnCodeList = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
//            InputStream is = new FileInputStream(COSApplication.pradigiPath + "/.LLA/English/Game/CertificateData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            JSONObject jsonObj = new JSONObject(jsonStr);
            returnCodeList = jsonObj.getJSONArray(nodeTitle);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return returnCodeList;
    }
*/

    @Override
    public void setStudentName(String name) {
        tv_studentName.setText(name);
    }

    @Override
    public void setStudentAvatar(String studentAvatar) {
        int id = getActivity().getResources().getIdentifier(studentAvatar, "drawable", getActivity().getPackageName());
        Drawable drawable = getActivity().getResources().getDrawable(id);
        iv_student.setImageDrawable(drawable);
    }

    private void calculateTime(String paperStartTime, String paperEndTime) {
        try {
            Date date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(paperStartTime);
            Date date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(paperEndTime);

            long diff = date2.getTime() - date1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            String unit = "min.";
            String time = "";
            if (diffHours == 0 && diffMinutes == 0 && diffSeconds != 0) {
                unit = " secs.";
                time = "00 : 00 : " + diffSeconds + "";
            } else if (diffHours == 0 && diffMinutes != 0) {
                unit = " mins.";
                time = "00 : " + diffMinutes + " : " + diffSeconds;
            } else if (diffHours != 0) {
                unit = " hrs.";
                time = diffHours + " : " + diffMinutes + "  : " + diffSeconds;
            }
            tv_time_taken.setText(time + unit);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
