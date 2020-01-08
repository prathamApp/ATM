package com.pratham.atm.ui.login.qr_scan;

public interface QRScanContract {

    public interface QRScanView{
        void showToast(String myToast);
    }

    public interface QRScanPresenter{
        void displayToast();
    }

}
