package com.ofk.bd.Utility;

import android.content.Context;
import android.graphics.Color;

import com.developer.kalert.KAlertDialog;
import com.ofk.bd.R;

public class AlertDialogUtility {

    private KAlertDialog pDialog;

    public void showAlertDialog(Context context, String command) {

        switch (command) {
            case "start":
                pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#00c1c3"));
                pDialog.setTitleText("লোড হচ্ছে");
                pDialog.setCancelable(true);
                pDialog.show();
                break;

            case "done":
                pDialog = new KAlertDialog(context, KAlertDialog.ERROR_TYPE);
                pDialog.setCancelable(true);
                pDialog.setTitleText(context.getResources().getString(R.string.notFoundVideo))
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                pDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;

            case "verifyDone":
                pDialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#00c1c3"));
                pDialog.setTitleText("ভেরিফিকেশন সম্পন্ন হয়েছে");
                pDialog.setCancelable(true);
                pDialog.setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                pDialog.dismissWithAnimation();
                            }
                        });
                pDialog.show();
                break;

                case "wait":
                pDialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#00c1c3"));
                    pDialog.setTitleText("অপেক্ষা করো");
                pDialog.setCancelable(false);
                pDialog.show();
                break;

            case "noConnection":
                pDialog = new KAlertDialog(context, KAlertDialog.ERROR_TYPE);
                pDialog.setCancelable(true);
                pDialog.setTitleText("ইন্টারনেট সংযোগ নেই")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                pDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;
        }

    }

    public void dismissAlertDialog() {
        //pDialog.dismissWithAnimation();
        pDialog.dismiss();
    }
}
