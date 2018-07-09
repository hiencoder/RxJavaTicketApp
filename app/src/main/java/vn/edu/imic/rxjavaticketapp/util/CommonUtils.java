package vn.edu.imic.rxjavaticketapp.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import dagger.Provides;
import vn.edu.imic.rxjavaticketapp.R;

public class CommonUtils {
    public static ProgressDialog showProgressDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null){
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
