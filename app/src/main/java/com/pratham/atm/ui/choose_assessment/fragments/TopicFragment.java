package com.pratham.atm.ui.choose_assessment.fragments;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.atm.AssessmentApplication;
import com.pratham.atm.R;
import com.pratham.atm.custom.FastSave;
import com.pratham.atm.database.AppDatabase;
import com.pratham.atm.domain.AssessmentTest;
import com.pratham.atm.domain.AssessmentTestModal;
import com.pratham.atm.ui.choose_assessment.ChooseAssessmentActivity;
import com.pratham.atm.utilities.APIs;
import com.pratham.atm.utilities.Assessment_Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/

@EFragment(R.layout.fragment_topic)
public class TopicFragment extends Fragment {
    List<AssessmentTestModal> assessmentTestModals;
    List<AssessmentTest> assessmentTests = new ArrayList<>();
    @ViewById(R.id.rv_topics)
    RecyclerView rv_topics;
    String subjectId;
    ProgressDialog progressDialog;
    public  MediaPlayer instruction;


    public TopicFragment() {
        // Required empty public constructor
    }


    @AfterViews
    public void init(){
        subjectId = FastSave.getInstance().getString("SELECTED_SUBJECT_ID", "1");
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            getExamData();
        } else {
            getOfflineTests();
        }
    }


    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectId = FastSave.getInstance().getString("SELECTED_SUBJECT_ID", "1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }*/

    private void getExamData() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Exams");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentExamAPI + subjectId + "&languageid=" + Assessment_Constants.SELECTED_LANGUAGE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<AssessmentTestModal>>() {
                        }.getType();
                        assessmentTestModals = gson.fromJson(response, listType);
                        assessmentTests.clear();
                        for (int i = 0; i < assessmentTestModals.size(); i++) {
                            assessmentTests.addAll(assessmentTestModals.get(i).getLstsubjectexam());
                            for (int j = 0; j < assessmentTests.size(); j++) {
                                assessmentTests.get(j).setSubjectid(assessmentTestModals.get(i).getSubjectid());
                                assessmentTests.get(j).setSubjectname(assessmentTestModals.get(i).getSubjectname());
                                assessmentTests.get(j).setLanguageId(Assessment_Constants.SELECTED_LANGUAGE);
                            }
                        }
                        if (assessmentTests.size() > 0) {
                            AppDatabase.getDatabaseInstance(getActivity()).getTestDao().deleteTestsByLangIdAndSubId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
                            AppDatabase.getDatabaseInstance(getActivity()).getTestDao().insertAllTest(assessmentTests);
                            progressDialog.dismiss();

                            setTopicsToRecyclerView();
                           /* flowLayout.removeAllViews();
                            setTopicsToCheckBox(assessmentTests);*/
                        } else {
                            progressDialog.dismiss();
                          /*  ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                            ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
                            getActivity().getSupportFragmentManager().popBackStack();
                            Toast.makeText(getActivity(), "No Exams..", Toast.LENGTH_SHORT).show();
*/
//                            getOfflineTests();

//                           btnOk.setEnabled(false);

                            AppDatabase.getDatabaseInstance(getActivity()).getTestDao().deleteTestsByLangIdAndSubId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
                            Toast.makeText(getActivity(), "No exams", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                        ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager().popBackStack();

                        Toast.makeText(getActivity(), "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setTopicsToRecyclerView() {
        TopicAdapter topicAdapter = new TopicAdapter(getActivity(), assessmentTests);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_topics.setLayoutManager(linearLayoutManager);
        rv_topics.setAdapter(topicAdapter);
        topicAdapter.notifyDataSetChanged();

        //todo change audio
        instruction = MediaPlayer.create(getActivity(), R.raw.select_exam);
        instruction.start();
    }

   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            getExamData();
        } else {
            getOfflineTests();
        }

    }
*/
    private void getOfflineTests() {
        assessmentTests = AppDatabase.getDatabaseInstance(getContext()).getTestDao().getTopicBySubIdAndLangId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
        if (assessmentTests.size() > 0)
            setTopicsToRecyclerView();
        else {
           /* if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                getExamData();
            } else*/
            if (progressDialog != null)
                progressDialog.dismiss();
            ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
            ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
            getActivity().getSupportFragmentManager().popBackStack();
            Toast.makeText(getActivity(), "Connect to internet to download exams", Toast.LENGTH_SHORT).show();
        }
    }
}
