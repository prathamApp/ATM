package com.pratham.atm.ui.splash_activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.atm.AssessmentApplication;
import com.pratham.atm.R;
import com.pratham.atm.async.PushDataToServer;
import com.pratham.atm.custom.FastSave;
import com.pratham.atm.database.AppDatabase;
import com.pratham.atm.database.BackupDatabase;
import com.pratham.atm.domain.AssessmentPaperForPush;
import com.pratham.atm.domain.Student;
import com.pratham.atm.interfaces.Interface_copying;
import com.pratham.atm.interfaces.PermissionResult;
import com.pratham.atm.services.AppExitService;
import com.pratham.atm.ui.bottom_fragment.BottomStudentsFragment;
import com.pratham.atm.ui.login.group_selection.SelectGroupActivity_;
import com.pratham.atm.utilities.APIs;
import com.pratham.atm.utilities.Assessment_Constants;
import com.pratham.atm.utilities.Assessment_Utility;
import com.pratham.atm.utilities.PermissionUtils;
import com.pratham.atm.utilities.SplashSupportActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pratham.atm.AssessmentApplication.sharedPreferences;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends SplashSupportActivity implements SplashContract.SplashView, PermissionResult, Interface_copying {

    @ViewById(R.id.btn_start)
    Button btn_start_game;
    @ViewById(R.id.iv_logo)
    ImageView iv_logo;
    /*    @BindView(R.id.iv_logo_pradigi)
        ImageView iv_logo_pradigi;
        @BindView(R.id.temppp)
        RelativeLayout temppp;*/
    static String fpath, appname;
    public static MediaPlayer bgMusic;
    //    public static AppDatabase appDatabase;
    public ProgressDialog progressDialog;
    boolean isActivityRunning = false;
    List<AssessmentPaperForPush> newStudentCertificates;

    Context context;
    Dialog dialog, STTDialog;
    @Bean(SplashPresenter.class)
    SplashContract.SplashPresenter splashPresenter;
    @Bean(PushDataToServer.class)
    PushDataToServer pushDataToServer;
    public static boolean firstPause = true, fragmentBottomOpenFlg = false, fragmentBottomPauseFlg = false, fragmentAddStudentPauseFlg = false, fragmentAddStudentOpenFlg = false;

    @AfterViews
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog = new ProgressDialog(this);
        fpath = "";
        appname = "";
//        splashPresenter = new SplashPresenter(this, this);
        context = SplashActivity.this;
        btn_start_game.setVisibility(View.GONE);
//        iv_logo_pradigi.setVisibility(View.GONE);
        initiateApp();
    }

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog = new ProgressDialog(this);
        fpath = "";
        appname = "";
        splashPresenter = new SplashPresenter(this, this);
        context = SplashActivity.this;
        btn_start_game.setVisibility(View.GONE);
//        iv_logo_pradigi.setVisibility(View.GONE);
        initiateApp();
    }*/

    public void initiateApp() {

        String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA,
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_RECORD_AUDIO,
                PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
                PermissionUtils.Manifest_ACCESS_FINE_LOCATION
        };

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            if (!isPermissionsGranted(SplashActivity.this, permissionArray)) {
                askCompactPermissions(permissionArray, SplashActivity.this);
            } else {
                splashPresenter.checkVersion();
            }
        } else {
            splashPresenter.checkVersion();
        }

        //bgMusic.start();
//        ImageViewAnimatedChange(this, iv_logo);
    }

/*    public void ImageViewAnimatedChange(Context c, final ImageView iv_logo) {
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.zoom_in_new);
        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ImageViewAnimatedChangeSecond(c, iv_logo);
            }
        });
        iv_logo.setAnimation(anim_in);
    }

    public void ImageViewAnimatedChangeSecond(Context c, final ImageView iv_logo) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out_new);
        iv_logo.setAnimation(anim_out);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pradigiAnimation(c, iv_logo_pradigi);
            }
        });
    }

    public void pradigiAnimation(Context c, final ImageView iv_logo_pradigi) {
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.item_animation_from_bottom);
        iv_logo_pradigi.setVisibility(View.VISIBLE);

        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA,
                        PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                        PermissionUtils.Manifest_RECORD_AUDIO,
                        PermissionUtils.Manifest_ACCESS_COARSE_LOCATION,
                        PermissionUtils.Manifest_ACCESS_FINE_LOCATION
                };

                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                    if (!isPermissionsGranted(SplashActivity.this, permissionArray)) {
                        askCompactPermissions(permissionArray, SplashActivity.this);
                    } else {
                        splashPresenter.checkVersion();
                    }
                } else {
                    splashPresenter.checkVersion();
                }
            }
        });
        iv_logo_pradigi.setAnimation(anim_in);
    }*/

    @Override
    public void showButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Assessment_Constants.SD_CARD_Content = splashPresenter.getSdCardPath();
                if (!sharedPreferences.getBoolean(Assessment_Constants.SD_CARD_Content_STR, false)) {
                    if (!Assessment_Constants.SD_CARD_Content)
                        splashPresenter.copyZipAndPopulateMenu();
                    else
                        splashPresenter.populateSDCardMenu();
                } else
                    gotoNextActivity();
            }
        }, 2000);
    }

    @Override
    public void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Upgrade to radio_button_bg better version !");
        builder.setCancelable(false);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Click button action
                dialog.dismiss();
                if (Assessment_Utility.isDataConnectionAvailable(SplashActivity.this)) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.pratham.cityofstories")));
                    finish();
                } else {
                    Assessment_Utility.showAlertDialogue(SplashActivity.this, "No internet connection! Try updating later.");
                    startApp();
                }
            }
        });
        builder.show();
    }

    @Override
    public void startApp() {
        createDataBase();
        if (!AssessmentApplication.isTablet) {
//            splashPresenter.pushData();
            //todo uncomment PushDataToServer below
            //new PushDataToServer(this, false).execute();
            pushDataToServer.setValue(this, false);
            pushDataToServer.doInBackground();
        }
    }

    /*public void getSdCardPath() {
        CharSequence c = "";
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());

        try {
            c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
            appname = c.toString();
            Log.w("LABEL", c.toString());
        } catch (Exception e) {//Name Not FOund Exception
        }
        if (appname.equals("City of Stories")) {
            ArrayList<String> base_path = SDCardUtil.getExtSdCardPaths(this);
            if (base_path.size() > 0) {
                String path = base_path.get(0).replace("[", "");
                path = path.replace("]", "");
                fpath = path;
            } else
                fpath = Environment.getExternalStorageDirectory().getAbsolutePath();
            fpath = fpath + "/.LLA/English/";
            File file = new File(fpath);
            Assessment_Constants.ext_path = fpath;
            Log.d("getSD", "getSdCardPath: " + Assessment_Constants.ext_path);
            if (file.exists())
                updateSdCardPath(fpath);
            else {
                File direct = new File(Environment.getExternalStorageDirectory().toString() + ".LLA");
                if (!direct.exists()) direct.mkdir();
                direct = new File(Environment.getExternalStorageDirectory().toString() + ".LLA/English");
                if (!direct.exists()) direct.mkdir();
                file = new File(Environment.getExternalStorageDirectory().toString() + ".LLA/English/");
                if (file.exists())
                    updateSdCardPath("" + Environment.getExternalStorageDirectory().toString() + "/.LLA/English/");
            }
        }
    }

    public void updateSdCardPath(final String path) {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Assessment_Constants.ext_path = path;
                    Log.d("$path", "\n\n\n\n\n\n\n\n\n\nPATH: " + Assessment_Constants.ext_path + "\n\n\n\n\n\n\\n\n\n\n");
                    appDatabase.getStatusDao().updateValue("SdCardPath", "" + path);
                    BackupDatabase.backup(SplashActivity.this);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }
*/
    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading... Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if ((fragmentBottomOpenFlg && fragmentBottomPauseFlg) ||
                (fragmentBottomOpenFlg && fragmentAddStudentOpenFlg && fragmentBottomPauseFlg && fragmentAddStudentPauseFlg)) {
            try {
                if (bgMusic != null && bgMusic.isPlaying()) {
                    bgMusic.setLooping(false);
                    bgMusic.stop();
                    bgMusic.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (firstPause) {
            try {
                if (bgMusic != null && bgMusic.isPlaying()) {
                    bgMusic.setLooping(false);
                    bgMusic.stop();
                    bgMusic.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    public void showExitDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView title = dialog.findViewById(R.id.dia_title);
        Button exit_btn = dialog.findViewById(R.id.dia_btn_exit);
        Button restart_btn = dialog.findViewById(R.id.dia_btn_restart);

        title.setText("Do you want to exit?");
        restart_btn.setText("Yes");
        exit_btn.setText("No");
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                showBottomFragment();
                BottomStudentsFragment bottomStudentsFragment = new BottomStudentsFragment();
                bottomStudentsFragment.show(getSupportFragmentManager(), BottomStudentsFragment.class.getSimpleName());
            }
        });

        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                String curSession = AppDatabase.getDatabaseInstance(SplashActivity.this).getStatusDao().getValue("CurrentSession");
                String toDateTemp = AppDatabase.getDatabaseInstance(SplashActivity.this).getSessionDao().getToDate(curSession);

                Log.d("AppExitService:", "curSession : " + curSession + "      toDateTemp : " + toDateTemp);

                if (toDateTemp != null) {
                    if (toDateTemp.equalsIgnoreCase("na"))
                        AppDatabase.getDatabaseInstance(context).getSessionDao().UpdateToDate(curSession, Assessment_Utility.GetCurrentDateTime());
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void permissionGranted() {
        Log.d("Splash", "permissionGranted: HAHAHAHAHAHA");
        splashPresenter.checkVersion();
    }

    @Override
    public void permissionDenied() {
    }

    @Override
    public void permissionForeverDenied() {
    }

    public void createDataBase() {
        Log.d("$$$", "createDataBase");

        try {
            boolean dbExist = checkDataBase();
            if (!dbExist) {
                try {
                    Log.d("$$$", "createDataBasebefore");

                    appDatabase = AppDatabase.getDatabaseInstance(this);

                    Log.d("$$$", "createDataBaseAfter");

                          /*  Room.databaseBuilder(this,
                            AppDatabase.class, AppDatabase.DB_NAME)
                            .allowMainThreadQueries()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }
                            })
                            .build();*/
                    if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrathamBackups" + "/assessment_database").exists()) {
                        try {
                            splashPresenter.copyDataBase();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showButton();
                        //populateMenu();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                appDatabase = AppDatabase.getDatabaseInstance(this);
               /* appDatabase = Room.databaseBuilder(this,
                        AppDatabase.class, AppDatabase.DB_NAME)
                        .allowMainThreadQueries()
                        .build();*/
                showButton();
//                getSdCardPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(getDatabasePath(AppDatabase.DB_NAME).getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    @Override
    public void gotoNextActivity() {
/*        if (Assessment_Constants.SMART_PHONE && !Assessment_Constants.SD_CARD_Content) {
            Assessment_Constants.ext_path = COSApplication.contentSDPath + "/.LLA/English/";
            dismissProgressDialog();
        } else {*/

        context.startService(new Intent(context, AppExitService.class));
        dismissProgressDialog();


        if (!AssessmentApplication.isTablet) {
            if (!FastSave.getInstance().getBoolean(Assessment_Constants.VOICES_DOWNLOAD_INTENT, false))
                show_STT_Dialog();
            else
                showBottomFragment();
        } else {
            startActivity(new Intent(context, SelectGroupActivity_.class));
        }




       /* if (AssessmentApplication.isTablet)
            startActivity(new Intent(context, SelectGroupActivity.class));
        else showBottomFragment();*/

//                    startActivity(new Intent(context, MenuActivity.class));


// startActivity(new Intent(context, MainActivity.class));
//        }
    }

    public void showBottomFragment() {
        try {

            fragmentBottomOpenFlg = true;
            firstPause = false;
            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                if (!AssessmentApplication.isTablet) {
                    if (!FastSave.getInstance().getBoolean("STUDENTS_DOWNLOADED", false))
                        pullOldStudentsCertificates();
                    else {
                        BottomStudentsFragment bottomStudentsFragment = new BottomStudentsFragment();
                        bottomStudentsFragment.show(getSupportFragmentManager(), BottomStudentsFragment.class.getSimpleName());
                    }
                }
            } else {
                Toast.makeText(context, "Connect to internet to download students of this device..", Toast.LENGTH_LONG).show();
                BottomStudentsFragment bottomStudentsFragment = new BottomStudentsFragment();
                bottomStudentsFragment.show(getSupportFragmentManager(), BottomStudentsFragment.class.getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void pullOldStudentsCertificates() {
        String url = APIs.pullCertificateByDeviceIdAPI + Assessment_Utility.getDeviceId(this);
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading Students..");
        AndroidNetworking.get(url)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            newStudentCertificates = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                AssessmentPaperForPush paper = new AssessmentPaperForPush();
                                paper.setPaperStartTime(response.getJSONObject(i).getString("paperstarttime"));
                                paper.setPaperEndTime(response.getJSONObject(i).getString("paperendtime"));
                                paper.setLanguageId(response.getJSONObject(i).getString("languageid"));
                                paper.setExamId(response.getJSONObject(i).getString("examid"));
                                paper.setSubjectId(response.getJSONObject(i).getString("subjectid"));
                                paper.setPaperId(response.getJSONObject(i).getString("paperId"));
                                paper.setOutOfMarks(response.getJSONObject(i).getString("TotalMarks"));
                                paper.setTotalMarks(response.getJSONObject(i).getString("ScoredMarks"));
                                paper.setCorrectCnt(response.getJSONObject(i).getInt("correctCount"));
                                paper.setWrongCnt(response.getJSONObject(i).getInt("wrongCount"));
                                paper.setStudentId(response.getJSONObject(i).getString("StudentID"));
                                paper.setExamName(response.getJSONObject(i).getString("examname"));
                                paper.setExamTime(response.getJSONObject(i).getString("examduration"));
                                paper.setQuestion1Rating(response.getJSONObject(i).getString("question1Rating"));
                                paper.setQuestion2Rating(response.getJSONObject(i).getString("question2Rating"));
                                paper.setQuestion3Rating(response.getJSONObject(i).getString("question3Rating"));
                                paper.setFullName(response.getJSONObject(i).getString("FullName"));
                                paper.setGender(response.getJSONObject(i).getString("Gender"));
                                paper.setAge(response.getJSONObject(i).getInt("Age"));
                                newStudentCertificates.add(paper);
                            }
                            if (newStudentCertificates.size() > 0) {
                                savePaperToDB();

                            } else {
                                Toast.makeText(context, "No students to pull..", Toast.LENGTH_SHORT).show();
//                                subjectView.setSubjectToSpinner();
                                BottomStudentsFragment bottomStudentsFragment = new BottomStudentsFragment();
                                bottomStudentsFragment.show(getSupportFragmentManager(), BottomStudentsFragment.class.getSimpleName());
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            BottomStudentsFragment bottomStudentsFragment = new BottomStudentsFragment();
                            bottomStudentsFragment.show(getSupportFragmentManager(), BottomStudentsFragment.class.getSimpleName());

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().deletePaperPatterns();
                        progressDialog.dismiss();
                    }
                });
    }

    private void savePaperToDB() {
        try {
//        List<Student> AllStudents = AppDatabase.getDatabaseInstance(this).getStudentDao().getAllStudents();
//        if (AllStudents.size() > 0) {
            if (newStudentCertificates.size() > 0) {
                for (int i = 0; i < newStudentCertificates.size(); i++) {
                    Student student = AppDatabase.getDatabaseInstance(this).getStudentDao().getStudent(newStudentCertificates.get(i).getStudentId());
                    if (student == null)
                        addPulledStudentToDb(newStudentCertificates.get(i));
//            }
                }
            }
            BackupDatabase.backup(context);
//            if (isActivityRunning) {
            BottomStudentsFragment bottomStudentsFragment = new BottomStudentsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(bottomStudentsFragment, null);
            ft.commitAllowingStateLoss();
//                bottomStudentsFragment.show(getSupportFragmentManager(), BottomStudentsFragment.class.getSimpleName());
//            }
            progressDialog.dismiss();
            FastSave.getInstance().saveBoolean("STUDENTS_DOWNLOADED", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPulledStudentToDb(AssessmentPaperForPush assessmentPaperForPush) {
        Student student = new Student();
        student.setStudentID(assessmentPaperForPush.getStudentId());
        student.setFullName(assessmentPaperForPush.getFullName());
        student.setAge(assessmentPaperForPush.getAge());
        student.setGender(assessmentPaperForPush.getGender());
        student.setAvatarName(Assessment_Utility.getRandomAvatarNames(context));
        student.setGroupId("PS");
        student.setDeviceId(Assessment_Utility.getDeviceId(this));
        AppDatabase.getDatabaseInstance(context).getStudentDao().insert(student);


    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }

    @Override
    protected void onResume() {
        super.onResume();
/*        try {
            if (!bgMusic.isPlaying()) {
                bgMusic = MediaPlayer.create(this, R.raw.bg_sound);
                bgMusic.setLooping(true);
                bgMusic.start();
            }
        } catch (Exception e) {
        }
        try {
            if (bgMusic.equals(null)) {
                bgMusic = MediaPlayer.create(this, R.raw.bg_sound);
                bgMusic.setLooping(true);
                bgMusic.start();
            }
        } catch (Exception e) {
        }*/
//        EventBus.getDefault().register(this);
        EventBus.getDefault().post("reload");

//        showBottomFragment();
    }

    @Override
    public void copyingExisting() {
    }

    @Override
    public void successCopyingExisting(String path) {
    }

    @Override
    public void failedCopyingExisting() {
    }

    @Override
    public void onStop() {
        super.onStop();
        isActivityRunning = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        isActivityRunning = true;
    }

    private void show_STT_Dialog() {
        STTDialog = new Dialog(this);
        STTDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        STTDialog.setContentView(R.layout.lang_custom_dialog);
/*        Bitmap map=COS_Utility.takeScreenShot(HomeActivity.this);
        Bitmap fast=COS_Utility.fastblur(map, 20);
        final Drawable draw=new BitmapDrawable(getResources(),fast);
        dialog.getWindow().setBackgroundDrawable(draw);*/
        STTDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        STTDialog.setCancelable(false);
        STTDialog.setCanceledOnTouchOutside(false);

        STTDialog.show();

        TextView dia_title = STTDialog.findViewById(R.id.dia_title);
        Button skip = STTDialog.findViewById(R.id.dia_btn_green);
        Button ok = STTDialog.findViewById(R.id.dia_btn_yellow);
        dia_title.setText("Please download language packs offline for better performance");
        ok.setText("OK");
        skip.setText("SKIP");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomFragment();
                FastSave.getInstance().saveBoolean(Assessment_Constants.VOICES_DOWNLOAD_INTENT, true);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.google.android.googlequicksearchbox",
                        "com.google.android.voicesearch.greco3.languagepack.InstallActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                STTDialog.dismiss();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastSave.getInstance().saveBoolean(Assessment_Constants.VOICES_DOWNLOAD_INTENT, true);
                showBottomFragment();
                STTDialog.dismiss();
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}