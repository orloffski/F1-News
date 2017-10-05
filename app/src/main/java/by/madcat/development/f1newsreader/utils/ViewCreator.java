package by.madcat.development.f1newsreader.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

public class ViewCreator {
    public static void sendSnackbarMessage(View rootView, String message, int lenght){
        Snackbar.make(rootView, message, lenght).show();
    }

    public static ProgressDialog getProgressFilesMove(Context context, int count, String message, String title){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMax(count);
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        return progressDialog;
    }
}
