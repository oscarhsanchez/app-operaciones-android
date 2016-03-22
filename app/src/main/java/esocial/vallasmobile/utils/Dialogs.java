package esocial.vallasmobile.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;


import java.util.ArrayList;

import esocial.vallasmobile.R;


public class Dialogs {

    public static AlertDialog newAlertDialog(Context context, String title, String message,
                                             String textButton, DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        return builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(textButton, listener)
                .create();
    }


    public static void showConfirmAlertDialog(Context context, int message, int positiveText,
                                              DialogInterface.OnClickListener positiveListener,
                                              int negativeText,
                                              DialogInterface.OnClickListener negativeListener,
                                              boolean isCancelable) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.Dialog);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setMessage(message)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener)
                .setCancelable(isCancelable)
                .create().show();
    }

    public static void showAlertDialog(Context context, String title, String message) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.Dialog);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    public static void showAlertDialog(Context context, String title, String message,
                                       DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.Dialog);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.accept), listener).create().show();
    }


    public static ProgressDialog newProgressDialog(Context context, String title, Boolean cancelable) {
        ProgressDialog progressDialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressDialog = new ProgressDialog(context, R.style.Dialog);
        } else {
            progressDialog = new ProgressDialog(context);
        }

        progressDialog.setMessage(title);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(cancelable);

        return  progressDialog;
    }


}
