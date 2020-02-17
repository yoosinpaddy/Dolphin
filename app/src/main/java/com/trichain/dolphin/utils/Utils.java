package com.trichain.dolphin.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trichain.dolphin.R;

public class Utils {
    public static void displayToast(Activity activity, boolean isSuccess, String message) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout;
        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.setDuration(Toast.LENGTH_LONG);
        if (isSuccess) {
            layout = inflater.inflate(R.layout.success_toast, null);
        } else {
            layout = inflater.inflate(R.layout.failure_toast, null);
        }
        toast.setView(layout);
        TextView tv_message = layout.findViewById(R.id.tv_message);
        tv_message.setText(message);
        toast.show();
    }
}
