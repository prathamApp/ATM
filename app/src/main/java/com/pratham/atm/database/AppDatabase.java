package com.pratham.atm.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pratham.atm.dao.AssessmentDao;
import com.pratham.atm.dao.AssessmentPaperForPushDao;
import com.pratham.atm.dao.AssessmentPaperPatternDao;
import com.pratham.atm.dao.AssessmentPatternDetailsDao;
import com.pratham.atm.dao.AssessmentTestDao;
import com.pratham.atm.dao.AssessmentTopicDao;
import com.pratham.atm.dao.AttendanceDao;
import com.pratham.atm.dao.ContentTableDao;
import com.pratham.atm.dao.CrlDao;
import com.pratham.atm.dao.DownloadMediaDao;
import com.pratham.atm.dao.GroupDao;
import com.pratham.atm.dao.LanguageDao;
import com.pratham.atm.dao.LogDao;
import com.pratham.atm.dao.ScienceQuestionChoiceDao;
import com.pratham.atm.dao.ScienceQuestionDao;
import com.pratham.atm.dao.ScoreDao;
import com.pratham.atm.dao.SessionDao;
import com.pratham.atm.dao.StatusDao;
import com.pratham.atm.dao.StudentDao;
import com.pratham.atm.dao.SubjectDao;
import com.pratham.atm.dao.SupervisorDataDao;
import com.pratham.atm.dao.VillageDao;
import com.pratham.atm.domain.Assessment;
import com.pratham.atm.domain.AssessmentLanguages;
import com.pratham.atm.domain.AssessmentPaperForPush;
import com.pratham.atm.domain.AssessmentPaperPattern;
import com.pratham.atm.domain.AssessmentPatternDetails;
import com.pratham.atm.domain.AssessmentSubjects;
import com.pratham.atm.domain.AssessmentTest;
import com.pratham.atm.domain.AssessmentToipcsModal;
import com.pratham.atm.domain.Attendance;
import com.pratham.atm.domain.ContentTable;
import com.pratham.atm.domain.Crl;
import com.pratham.atm.domain.DownloadMedia;
import com.pratham.atm.domain.Groups;
import com.pratham.atm.domain.Modal_Log;
import com.pratham.atm.domain.ScienceQuestion;
import com.pratham.atm.domain.ScienceQuestionChoice;
import com.pratham.atm.domain.Score;
import com.pratham.atm.domain.Session;
import com.pratham.atm.domain.Status;
import com.pratham.atm.domain.Student;
import com.pratham.atm.domain.SupervisorData;
import com.pratham.atm.domain.Village;


@Database(entities = {Crl.class, Student.class, Score.class, Session.class, Attendance.class,
        Status.class, Village.class, Groups.class, Assessment.class, Modal_Log.class,
        ContentTable.class, AssessmentToipcsModal.class, ScienceQuestion.class,
        ScienceQuestionChoice.class, AssessmentSubjects.class, AssessmentLanguages.class,
        AssessmentTest.class, AssessmentPaperForPush.class,
        AssessmentPaperPattern.class, AssessmentPatternDetails.class, SupervisorData.class, DownloadMedia.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase appDatabase;

    public static final String DB_NAME = "assessment_database";

    public abstract CrlDao getCrlDao();

    public abstract StudentDao getStudentDao();

    public abstract ScoreDao getScoreDao();

    public abstract AssessmentDao getAssessmentDao();

    public abstract SessionDao getSessionDao();

    public abstract AttendanceDao getAttendanceDao();

    public abstract VillageDao getVillageDao();

    public abstract GroupDao getGroupsDao();

    public abstract LogDao getLogsDao();

    public abstract ContentTableDao getContentTableDao();

    public abstract StatusDao getStatusDao();

    public abstract AssessmentTopicDao getAssessmentTopicDao();

    public abstract ScienceQuestionDao getScienceQuestionDao();

    public abstract ScienceQuestionChoiceDao getScienceQuestionChoicesDao();

    public abstract SubjectDao getSubjectDao();

    public abstract LanguageDao getLanguageDao();

    public abstract AssessmentTestDao getTestDao();

    public abstract AssessmentPaperForPushDao getAssessmentPaperForPushDao();

    public abstract AssessmentPaperPatternDao getAssessmentPaperPatternDao();

    public abstract AssessmentPatternDetailsDao getAssessmentPatternDetailsDao();

    public abstract SupervisorDataDao getSupervisorDataDao();

    public abstract DownloadMediaDao getDownloadMediaDao();



   /* public static AppDatabase getDatabaseInstance(Context context) {
        if(appDatabase!=null) {
            appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, AppDatabase.DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }*/

    public static AppDatabase getDatabaseInstance(Context context) {
        if (appDatabase == null)
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "assessment_database")
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries().build();
        return appDatabase;
    }


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d("$$$", "MIGRATION_1_2");
            try {

                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question4Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperForPush add COLUMN question5Rating text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion4 text");
                database.execSQL("ALTER TABLE AssessmentPaperPattern add COLUMN certificateQuestion5 text");
                Log.d("$$$", "MIGRATION_1_3After");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    /* static final Migration MIGRATION_1_2 = new Migration(1, 2) {


       @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE  AssessmentPaperForPush  ( languageId  TEXT,  subjectId  TEXT, " +
                    " examId  TEXT,  paperId  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,  paperStartTime  TEXT," +
                    "  paperEndTime  TEXT,  outOfMarks  TEXT,  totalMarks  TEXT,  studentId  TEXT, " +
                    " CorrectCnt  INTEGER NOT NULL,  wrongCnt  INTEGER NOT NULL,  SkipCnt  INTEGER NOT NULL, " +
                    " sentFlag  INTEGER NOT NULL,  SessionID  TEXT)");

            database.execSQL("CREATE TABLE  SupervisorData  ( sId  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "  supervisorId  TEXT,  assessmentSessionId  TEXT,  supervisorName  TEXT,  supervisorPhoto  TEXT," +
                    "  sentFlag  INTEGER NOT NULL)");

            database.execSQL("drop table ScienceAssesmentAnswer");

            database.execSQL("ALTER TABLE Score add COLUMN isAttempted boolean");
            database.execSQL("ALTER TABLE Score add COLUMN isCorrect boolean");
            database.execSQL("ALTER TABLE Score add COLUMN userAnswer text");
            database.execSQL("ALTER TABLE Score add COLUMN examId text");
        }
    };*/


}
