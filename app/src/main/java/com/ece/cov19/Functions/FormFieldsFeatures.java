package com.ece.cov19.Functions;

import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.ece.cov19.R;

public class FormFieldsFeatures {
//    Empty Form Fields check
     public boolean checkIfEmpty(EditText e){
        if(e.getText().toString().isEmpty()){
            e.setError("This Field can't be empty");
            e.requestFocus();
            return true;
        }

        return false;
    }

     public boolean checkIfEmpty(Context ctx, TextView label, String s){
        if(s.equals("not selected")){

            label.setFocusable(true);
            label.setError("This field can't be empty");
            Toast.makeText(ctx, "Select " +label.getText().toString(),Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
//    Empty form fields checking ends



    //    Blood group selection starts
    public TextView bloodGroupSelection(Context ctx, TextView newSelection, TextView oldSelection){

         if(newSelection==oldSelection){

         }
         else {
             newSelection.setBackgroundResource(R.drawable.blood_grp_selected);
             newSelection.setTextColor(Color.WHITE);
             if (oldSelection != null) {
                 oldSelection.setBackgroundResource(R.drawable.blood_grp_not_selected);
                 oldSelection.setTextColor(ContextCompat.getColor(ctx, R.color.textColorGrey));
             }
         }


         return newSelection;
    }



//    Blood group selection ends



}
