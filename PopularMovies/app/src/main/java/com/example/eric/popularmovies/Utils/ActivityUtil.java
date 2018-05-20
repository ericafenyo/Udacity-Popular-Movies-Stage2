package com.example.eric.popularmovies.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * TODO: modify methods to static ones
 * Created by eric on 23/10/2017.
 */

public class ActivityUtil {

    public static void makeToast(Context context ,Object message){
        Toast toast = Toast.makeText(context,String.valueOf(message),Toast.LENGTH_SHORT);

        if (toast != null) {
            toast.cancel();
            toast.show();
        }
    }

    public static void makeSnack(View view, String message){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
    }

}
