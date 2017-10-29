package com.example.eric.popularmovies;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 *
 * Created by eric on 23/10/2017.
 */

public class Notify {
    Context context;

    public Notify(Context context) {
        this.context = context;
    }

    public void makeToast( String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public  void makeSnack(View view, String message){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
    }

}
