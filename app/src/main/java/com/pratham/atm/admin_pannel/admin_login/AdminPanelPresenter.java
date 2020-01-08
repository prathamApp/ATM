package com.pratham.atm.admin_pannel.admin_login;

import android.content.Context;

import com.pratham.atm.database.AppDatabase;
import com.pratham.atm.domain.Crl;

import org.androidannotations.annotations.EBean;


/**
 * Created by PEF on 19/11/2018.
 */
@EBean
public class AdminPanelPresenter implements AdminPanelContract.AdminPanelPresenter {
    AdminPanelContract.AdminPanelView adminPanelView;
    Context context;

    public AdminPanelPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void checkLogin(String userName, String password) {
        // if user name and password are admin then navigate to Download activity otherWise admin activity

        if (userName.equals("admin") && password.equals("admin")) {
            adminPanelView.openPullDataFragment();
        } else {
            // assign push logic
            Crl loggedCrl = AppDatabase.getDatabaseInstance(context).getCrlDao().checkUserValidation(userName, password);
            if (loggedCrl != null) {
                adminPanelView.onLoginSuccess();
                String crlId = loggedCrl.getCRLId();
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("CRLID", crlId);

            } else {
                //userNAme and password may be wrong
                adminPanelView.onLoginFail();
            }
        }
    }

    @Override
    public void clearData() {
      /*  BaseActivity.villageDao.deleteAllVillages();
        BaseActivity.groupDao.deleteAllGroups();
        BaseActivity.studentDao.deleteAllStudents();
        BaseActivity.crLdao.deleteAllCRLs();*/
        AppDatabase.getDatabaseInstance(context).getVillageDao().deleteAllVillages();
        AppDatabase.getDatabaseInstance(context).getGroupsDao().deleteAllGroups();
        AppDatabase.getDatabaseInstance(context).getStudentDao().deleteAll();
        AppDatabase.getDatabaseInstance(context).getCrlDao().deleteAll();
        adminPanelView.onDataClearToast();

    }

    @Override
    public void setView(AdminPanelContract.AdminPanelView adminPanelView) {
        this.adminPanelView = adminPanelView;
    }
}
