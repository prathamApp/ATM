package com.pratham.atm.admin_pannel.PushOrAssign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pratham.atm.R;
import com.pratham.atm.async.PushDataToServer;
import com.pratham.atm.database.AppDatabase;
import com.pratham.atm.utilities.Assessment_Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/*import butterknife.BindView;
import butterknife.OnClick;*/

@EFragment(R.layout.fragment_push_or_assign)
public class PushOrAssignFragment extends Fragment {
    @ViewById(R.id.btn_assign)
    Button assign;
    @ViewById(R.id.btn_push)
    Button push;
    @ViewById(R.id.txt_video_toggle)
    TextView txt_video_toggle;
    @ViewById(R.id.video_toggle)
    ToggleButton video_toggle;
    @Bean(PushDataToServer.class)
    PushDataToServer pushDataToServer;

    public PushOrAssignFragment() {
        // Required empty public constructor
    }


    @AfterViews
    public void init() {

    }


/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_push_or_assign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }*/

    @Click(R.id.btn_assign)
    public void onAssignClick() {
        Intent intent = new Intent(getActivity(), Activity_AssignGroups_.class);
        startActivityForResult(intent, 1);
        getActivity().startActivity(intent);
    }

    @Click(R.id.btn_push)
    public void onPushClick() {
        /*Intent intent = new Intent(getActivity(), PushDataActivity.class);
        startActivityForResult(intent, 1);
        getActivity().startActivity(intent);*/
        pushDataToServer.setValue(getActivity(), false);
        pushDataToServer.doInBackground();

    }

    @Click(R.id.btn_clear_data)
    public void onClear() {
        AlertDialog clearDataDialog = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Clear Data")
                .setMessage("Are you sure you want to clear everything ?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        clearData();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        clearDataDialog.show();
        clearDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    private void clearData() {
        AppDatabase appDatabase = AppDatabase.getDatabaseInstance(getActivity());
        appDatabase.getVillageDao().deleteAllVillages();
        appDatabase.getGroupsDao().deleteAllGroups();
        appDatabase.getStudentDao().deleteAll();
        appDatabase.getCrlDao().deleteAll();
    }

    @Click(R.id.video_toggle)
    public void onVideoToggle() {
        if (video_toggle.isChecked()) {
//            txt_video_toggle.setText("Video monitoring is on");
            Assessment_Constants.VIDEOMONITORING = true;
        } else {
//            txt_video_toggle.setText("Video monitoring is Off");
            Assessment_Constants.VIDEOMONITORING = false;

        }

    }

    /*@OnClick(R.id.btn_pull_exam_data)
    public void onPullExam() {
        Intent intent = new Intent(getActivity(), PushDataActivity.class);
        startActivityForResult(intent, 1);
        getActivity().startActivity(intent);
    }*/
}
