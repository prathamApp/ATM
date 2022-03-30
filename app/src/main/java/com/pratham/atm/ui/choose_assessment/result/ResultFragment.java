package com.pratham.atm.ui.choose_assessment.result;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pratham.atm.R;
import com.pratham.atm.database.AppDatabase;
import com.pratham.atm.domain.AssessmentPaperForPush;
import com.pratham.atm.domain.ResultModalClass;
import com.pratham.atm.domain.ResultOuterModalClass;
import com.pratham.atm.ui.choose_assessment.science.certificate.CertificateSubjects.CertificateFragment;
import com.pratham.atm.ui.choose_assessment.science.certificate.CertificateSubjects.CertificateFragment_;
import com.pratham.atm.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static com.pratham.atm.utilities.Assessment_Utility.mediaPlayer;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/

@EFragment(R.layout.fragment_result)
public class ResultFragment extends Fragment implements ResultListener {

    @ViewById(R.id.rv_question_answers)
    RecyclerView rv_question_answers;
    @ViewById(R.id.tv_out_of_marks)
    TextView tv_out_of_marks;
    @ViewById(R.id.tv_marks_obtained)
    TextView tv_marks_obtained;
    @ViewById(R.id.tv_topic)
    TextView tv_topic;
    @ViewById(R.id.tv_subject)
    TextView tv_subject;
    @ViewById(R.id.tv_name)
    TextView tv_name;
    @ViewById(R.id.btn_done)
    Button btn_done;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    /*   @ViewById(R.id.certificate_frame)
       FrameLayout certificate_frame;*/
    @Bean(ResultPresenter.class)
    ResultContract.ResultPresenter presenter;
    List<ResultModalClass> resultList;
    ResultOuterModalClass outerModalClass;
    String outOfMarks, marksObtained, studentId, examStartTime, examEndTime, examId, subjectId, paperId;


    public ResultFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
//        final Toolbar mToolbar = view.findViewById(R.id.toolbar);

        outerModalClass = (ResultOuterModalClass) getActivity().getIntent().getSerializableExtra("result");
        resultList = outerModalClass.getResultList();
        outOfMarks = outerModalClass.getOutOfMarks();
        marksObtained = outerModalClass.getMarksObtained();
        studentId = outerModalClass.getStudentId();
        examStartTime = outerModalClass.getExamStartTime();
        examEndTime = outerModalClass.getExamEndTime();
        examId = outerModalClass.getExamId();
        subjectId = outerModalClass.getSubjectId();
        paperId = outerModalClass.getPaperId();
        tv_marks_obtained.setText(marksObtained);
        tv_out_of_marks.setText(outOfMarks);

   /*     Bundle bundle = new Bundle();
        bundle.putSerializable("result", outerModalClass);
        Assessment_Utility.showFragment(getActivity(), new CertificateFragment(), R.id.certificate_frame, bundle, CertificateFragment.class.getSimpleName());
*/
        btn_done.setVisibility(View.GONE);

//        presenter = new ResultPresenter(getActivity());
        String studentName = presenter.getStudent(studentId);
        tv_name.setText(studentName);
        mToolbar.setTitle(studentName);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        String subName = presenter.getSubjectName(examId);
        String topicName = presenter.getTopicName(examId);
        tv_topic.setText(topicName);
        tv_subject.setText(subName);
        ResultAdapter resultAdapter = new ResultAdapter(getActivity(), resultList, this);
        rv_question_answers.setAdapter(resultAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_question_answers.setLayoutManager(linearLayoutManager);
        resultAdapter.notifyDataSetChanged();

//        AppBarLayout mAppBarLayout = view.findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            //            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
            }
        });

        Assessment_Utility.setInstruction(getActivity(),"check_result_instruction");

    }

    /*  @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          if (getArguments() != null) {
          }
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
          // Inflate the layout for this fragment
          return inflater.inflate(R.layout.fragment_result, container, false);
      }

      @Override
      public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          final Toolbar mToolbar = view.findViewById(R.id.toolbar);
          ButterKnife.bind(this, view);
  //        resultList = (List<ResultModalClass>) getIntent().getSerializableExtra("result");
          outerModalClass = (ResultOuterModalClass) getActivity().getIntent().getSerializableExtra("result");
          resultList = outerModalClass.getResultList();
          outOfMarks = outerModalClass.getOutOfMarks();
          marksObtained = outerModalClass.getMarksObtained();
          studentId = outerModalClass.getStudentId();
          examStartTime = outerModalClass.getExamStartTime();
          examEndTime = outerModalClass.getExamEndTime();
          examId = outerModalClass.getExamId();
          subjectId = outerModalClass.getSubjectId();
          paperId = outerModalClass.getPaperId();
          tv_marks_obtained.setText(marksObtained);
          tv_out_of_marks.setText(outOfMarks);

     *//*     Bundle bundle = new Bundle();
        bundle.putSerializable("result", outerModalClass);
        Assessment_Utility.showFragment(getActivity(), new CertificateFragment(), R.id.certificate_frame, bundle, CertificateFragment.class.getSimpleName());
*//*
        btn_done.setVisibility(View.GONE);

        presenter = new ResultPresenter(getActivity());
        String studentName = presenter.getStudent(studentId);
        mToolbar.setTitle(studentName);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        String subName = presenter.getSubjectName(examId);
        String topicName = presenter.getTopicName(examId);
        tv_topic.setText(topicName);
        tv_subject.setText(subName);
        ResultAdapter resultAdapter = new ResultAdapter(getActivity(), resultList, this);
        rv_question_answers.setAdapter(resultAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_question_answers.setLayoutManager(linearLayoutManager);
        resultAdapter.notifyDataSetChanged();

        AppBarLayout mAppBarLayout = view.findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            //            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
            }
        });
    }
*/
    @Click(R.id.btn_done)
    public void onDoneClick() {
        AssessmentPaperForPush assessmentPaperForPush = AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperForPushDao().getAssessmentPapersByPaperId(paperId);
        Bundle bundle = new Bundle();
        bundle.putSerializable("assessmentPaperForPush", assessmentPaperForPush);
        Assessment_Utility.showFragment(getActivity(), new CertificateFragment_(), R.id.certificate_frame, bundle, CertificateFragment.class.getSimpleName());

    }

    @Override
    public void showDone(boolean visibility) {
        if (visibility)
            btn_done.setVisibility(View.VISIBLE);
        else btn_done.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();

    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();

    }
}
