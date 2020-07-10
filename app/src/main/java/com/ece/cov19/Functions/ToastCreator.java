package com.ece.cov19.Functions;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.R;

public class ToastCreator {
    public static void toastCreatorGreen(Context ctx, String s){
        View view = LayoutInflater.from(ctx).inflate(R.layout.toast_layout,null,false);
        TextView text=view.findViewById(R.id.toast_green_text);
        text.setBackgroundResource(R.drawable.button_style_green);
        text.setText(s);
        Toast toast=new Toast(ctx);
        toast.setGravity(Gravity.BOTTOM,0,120);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }
    public static void toastCreatorRed(Context ctx, String s){
        View view = LayoutInflater.from(ctx).inflate(R.layout.toast_layout,null,false);
        TextView text=view.findViewById(R.id.toast_green_text);
        text.setBackgroundResource(R.drawable.button_style_red);
        text.setText(s);
        Toast toast=new Toast(ctx);
        toast.setGravity(Gravity.BOTTOM,0,120);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }


}
