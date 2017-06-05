package by.madcat.development.f1newsreader.Services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.Utils.ViewCreator;
import by.madcat.development.f1newsreader.dataInet.NewsLinkListToLoad;

public class MoveToSdTask extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private String pathFrom;
    private String pathTo;
    private int filesCount;
    private ProgressDialog moveImagesProgressDialog;
    private boolean move_to_sd;

    public MoveToSdTask(Context context, String pathFrom, String pathTo, boolean toSd) {
        NewsLinkListToLoad.getInstance(null, null).setLock(true);
        this.context = context;
        this.pathFrom = pathFrom;
        this.pathTo = pathTo;
        this.move_to_sd = toSd;
    }

    @Override
    protected void onPreExecute() {
        filesCount = SystemUtils.getFilesCountInDir(pathFrom);

        if(filesCount != 0) {
            String title;
            if (move_to_sd)
                title = context.getString(R.string.move_title_to_sd);
            else
                title = context.getString(R.string.move_title_to_memory);

            moveImagesProgressDialog = ViewCreator.getProgressFilesMove(
                    context,
                    filesCount,
                    context.getString(R.string.move_message),
                    title);
            moveImagesProgressDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(filesCount != 0) {
            File[] filesList = new File(pathFrom).listFiles();

            if (filesList.length > 0)
                for (int i = 0; i < filesList.length; i++) {

                    try {
                        SystemUtils.copyFile(filesList[i], new File(pathTo + filesList[i].getName().toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    publishProgress(i + 1);
                }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(filesCount != 0) {
            SystemUtils.deleteFiles(pathFrom);
            moveImagesProgressDialog.dismiss();
        }
        NewsLinkListToLoad.getInstance(null, null).setLock(false);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        moveImagesProgressDialog.setProgress(values[0]);
    }
}
