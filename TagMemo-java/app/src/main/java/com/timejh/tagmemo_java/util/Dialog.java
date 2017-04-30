package com.timejh.tagmemo_java.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by tokijh on 2017. 4. 29..
 */

public class Dialog {

    private static ProgressDialog progressDialog;

    public static void show(Context context) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
    }

    public static void show(Context context, ProgressDialog customDialog) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog = customDialog;
        }
        show(context);
    }

    public static void dismiss() {
        if (progressDialog == null) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
