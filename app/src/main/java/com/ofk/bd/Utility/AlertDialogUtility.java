package com.ofk.bd.Utility;

import android.app.Activity;
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

            case "misMatchOtp":
                pDialog = new KAlertDialog(context, KAlertDialog.ERROR_TYPE);
                pDialog.setCancelable(true);
                pDialog.setTitleText("OTP সঠিক হয় নি")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                pDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;

            case "videoNotFound":
                pDialog = new KAlertDialog(context, KAlertDialog.CUSTOM_IMAGE_TYPE);
                pDialog.setTitleText("Coming Soon!");
                pDialog.setContentText("Thank you for your interest. Videos will be back real soon!");
                pDialog.setCustomImage(R.drawable.ic_not_found, context);
                pDialog.setConfirmText("OK");
                pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        dismissAlertDialog();
                    }
                }).show();
                break;

            case "courseNotFound":
                pDialog = new KAlertDialog(context, KAlertDialog.CUSTOM_IMAGE_TYPE);
                pDialog.setTitleText("Coming Soon!");
                pDialog.setContentText("Thank you for your interest. Courses will be back real soon!");
                pDialog.setCustomImage(R.drawable.ic_not_found, context);
                pDialog.setConfirmText("OK");
                pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        dismissAlertDialog();
                        Activity activity = (Activity) context;
                        activity.finish();
                    }
                }).show();
                break;
        }

    }

    public void showQuizCompletionDialog(Context context, int score, int totalQuizQuestions) {
        pDialog = new KAlertDialog(context, KAlertDialog.CUSTOM_IMAGE_TYPE);
        pDialog.setTitleText("You have finished the quiz");
        pDialog.setContentText("You've scored " + score + " out of " + totalQuizQuestions);
        pDialog.setCustomImage(R.drawable.ic_quiz, context);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                dismissAlertDialog();
            }
        }).show();

    }

    public void dismissAlertDialog() {
        //pDialog.dismissWithAnimation();
        pDialog.dismiss();
    }
}
