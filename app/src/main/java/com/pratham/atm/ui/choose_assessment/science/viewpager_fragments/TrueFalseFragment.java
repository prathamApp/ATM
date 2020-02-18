package com.pratham.atm.ui.choose_assessment.science.viewpager_fragments;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.atm.AssessmentApplication;
import com.pratham.atm.R;
import com.pratham.atm.custom.gif_viewer.GifView;
import com.pratham.atm.domain.ScienceQuestion;
import com.pratham.atm.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.atm.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.atm.utilities.Assessment_Constants;
import com.pratham.atm.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.FileInputStream;
import java.io.InputStream;

import static com.pratham.atm.utilities.Assessment_Utility.getFileName;

//import butterknife.BindView;

@EFragment(R.layout.layout_true_false_row)
public class TrueFalseFragment extends Fragment {
    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.rg_true_false)
    RadioGroup rg_true_false;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.rb_true)
    Button radioButtonTrue;
    @ViewById(R.id.rb_false)
    Button radioButtonFalse;
    AssessmentAnswerListener assessmentAnswerListener;

    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;

    public TrueFalseFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();

        }
        setTrueFalseQuestion();

    }

    public static TrueFalseFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        TrueFalseFragment_ trueFalseFragment = new TrueFalseFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        trueFalseFragment.setArguments(args);
        return trueFalseFragment;
    }

    /*   @Override
       public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           if (getArguments() != null) {
               pos = getArguments().getInt(POS, 0);
               scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
               assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();

           }
       }

       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
           // Inflate the layout for this fragment
           return inflater.inflate(R.layout.layout_true_false_row, container, false);
       }

       @Override
       public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
           super.onViewCreated(view, savedInstanceState);
           ButterKnife.bind(this, view);
           setTrueFalseQuestion();
       }
   */
    public void setTrueFalseQuestion() {


        question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
//            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {

            String fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
            String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


            String path = scienceQuestion.getPhotourl();
            String[] imgPath = path.split("\\.");
            int len;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            else len = 0;
            if (imgPath[len].equalsIgnoreCase("gif")) {
                try {
                    InputStream gif;
                    if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                        Glide.with(getActivity()).asGif()
                                .load(path)
                                .apply(new RequestOptions()
                                        .placeholder(Drawable.createFromPath(localPath)))
                                .into(questionImage);
//                    zoomImg.setVisibility(View.VISIBLE);
                    } else {
                        gif = new FileInputStream(localPath);
                        questionImage.setVisibility(View.GONE);
                        questionGif.setVisibility(View.VISIBLE);
                        questionGif.setGifResource(gif);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Glide.with(getActivity())
                        .load(path)
                        .apply(new RequestOptions()
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(questionImage);
            }
/*            } else {
                String fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                Bitmap bitmap = BitmapFactory.decodeFile(localPath);
                questionImage.setImageBitmap(bitmap);
            }*/
        } else questionImage.setVisibility(View.GONE);


        radioButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                questionTypeListener.setAnswer("", radioButtonTrue.getText().toString(), scienceQuestion.getQid(), null);
                assessmentAnswerListener.setAnswerInActivity("", radioButtonTrue.getText().toString(), scienceQuestion.getQid(), null);
                radioButtonTrue.setSelected(true);
              /*  radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
                radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle));
             */



                radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle_transparent));
                radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
//                radioButtonTrue.setTextColor(Assessment_Utility.selectedColor);
                radioButtonFalse.setSelected(false);
                radioButtonFalse.setTextColor(getActivity().getResources().getColor(R.color.black));


                radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle_transparent));
                GradientDrawable bgShape = (GradientDrawable) radioButtonTrue.getBackground();
                bgShape.setStroke(10, Assessment_Utility.selectedColor);
            }
        });

        radioButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assessmentAnswerListener.setAnswerInActivity("", radioButtonFalse.getText().toString(), scienceQuestion.getQid(), null);
//                questionTypeListener.setAnswer("", radioButtonFalse.getText().toString(), scienceQuestion.getQid(), null);
                radioButtonFalse.setSelected(true);
//                radioButtonFalse.setTextColor(Assessment_Utility.selectedColor);
                radioButtonTrue.setSelected(false);
               /* radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle));
                radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
               */

                radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
                radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle_transparent));
                radioButtonTrue.setTextColor(getActivity().getResources().getColor(R.color.black));

                radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle_transparent));
                GradientDrawable bgShape = (GradientDrawable) radioButtonFalse.getBackground();
                bgShape.setStroke(10, Assessment_Utility.selectedColor);

            }
        });
       /* rg_true_false.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButtonTrue.getId()*//* && (!isFirstLoad)*//*) {
                    questionTypeListener.setAnswer("", radioButtonTrue.getText().toString(), scienceQuestion.getQid(), null);
                    radioButtonTrue.setSelected(true);
                    radioButtonTrue.setTextColor(Assessment_Utility.selectedColor);
                    radioButtonFalse.setSelected(false);
                    radioButtonFalse.setTextColor(context.getResources().getColor(R.color.white));

                } else if (checkedId == radioButtonFalse.getId() *//*&& (!isFirstLoad)*//*) {
                    questionTypeListener.setAnswer("", radioButtonFalse.getText().toString(), scienceQuestion.getQid(), null);
                    radioButtonFalse.setSelected(true);
                    radioButtonFalse.setTextColor(Assessment_Utility.selectedColor);
                    radioButtonTrue.setSelected(false);
                    radioButtonTrue.setTextColor(context.getResources().getColor(R.color.white));


                } else {
                    Toast.makeText(context, "Select Answer", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        if (scienceQuestion.getUserAnswer().equalsIgnoreCase("true")) {
            radioButtonTrue.setSelected(true);
            radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
            radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle));
//            radioButtonTrue.setTextColor(Assessment_Utility.selectedColor);
            radioButtonFalse.setSelected(false);
//            radioButtonFalse.setTextColor(getActivity().getResources().getColor(R.color.white));
            radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle_transparent));
            GradientDrawable bgShape = (GradientDrawable) radioButtonTrue.getBackground();
            bgShape.setStroke(10, Assessment_Utility.selectedColor);


        } else if (scienceQuestion.getUserAnswer().equalsIgnoreCase("false")) {
            radioButtonFalse.setSelected(true);
//            radioButtonFalse.setTextColor(Assessment_Utility.selectedColor);
            radioButtonTrue.setSelected(false);
            radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle));
            radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
//            radioButtonTrue.setTextColor(getActivity().getResources().getColor(R.color.white));
            radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle_transparent));
            GradientDrawable bgShape = (GradientDrawable) radioButtonFalse.getBackground();
            bgShape.setStroke(10, Assessment_Utility.selectedColor);

        } else {
            radioButtonFalse.setSelected(false);
            radioButtonTrue.setSelected(false);
            radioButtonTrue.setTextColor(getActivity().getResources().getColor(R.color.black));
            radioButtonFalse.setTextColor(getActivity().getResources().getColor(R.color.black));

        }

    }

}
