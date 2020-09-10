package com.ece.cov19;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ece.cov19.DataModels.ImageDataModel;
import com.ece.cov19.DataModels.RequestDataModel;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.FindPatientData.findPatientAge;
import static com.ece.cov19.DataModels.FindPatientData.findPatientBloodGroup;
import static com.ece.cov19.DataModels.FindPatientData.findPatientDate;
import static com.ece.cov19.DataModels.FindPatientData.findPatientName;
import static com.ece.cov19.DataModels.FindPatientData.findPatientNeed;
import static com.ece.cov19.DataModels.FindPatientData.findPatientPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserGender;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class ViewDonorProfileActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, bloodGroupTextView, addressTextView, ageTextView, donorInfoTextView, sendRequestSuggestion;
    private ImageView genderImageView;
    private Button askForHelpBtn, acceptBtn, declineBtn, confirmBtn, cancelBtn;
    private ImageView backbtn;
    private ProgressBar progressBar;
    String name, age, bloodGroup, donorphone, donorInfo, address,requestedBy;
    Bitmap insertBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor_profile);


        nameTextView = findViewById(R.id.donor_profile_name);
        phoneTextView = findViewById(R.id.donor_profile_phone);
        bloodGroupTextView = findViewById(R.id.donor_profile_blood_group);
        addressTextView = findViewById(R.id.donor_profile_address);
        ageTextView = findViewById(R.id.donor_profile_age);
        donorInfoTextView = findViewById(R.id.donor_profile_type);
        genderImageView = findViewById(R.id.donor_profile_gender_icon);
        askForHelpBtn = findViewById(R.id.donor_profile_ask_for_help_button);
        acceptBtn = findViewById(R.id.donor_profile_accept_button);
        declineBtn = findViewById(R.id.donor_profile_decline_button);
        backbtn = findViewById(R.id.donor_profile_back_button);
        confirmBtn = findViewById(R.id.donor_profile_confirm_donation_button);
        cancelBtn = findViewById(R.id.donor_profile_decline_donation_button);
        sendRequestSuggestion = findViewById(R.id.donor_profile_send_request_suggestion);
        progressBar = findViewById(R.id.donor_profile_progressBar);


        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        bloodGroup = intent.getStringExtra("blood");
        donorphone = intent.getStringExtra("phone");
        donorInfo = intent.getStringExtra("donorinfo");
        address = intent.getStringExtra("address");
        if(intent.hasExtra("activity")){
            if(intent.getStringExtra("activity").equals("RequestsFromDonorsActivity")){
                requestedBy="donor";
            }
            else{
                requestedBy="patient";
            }
        }

        nameTextView.setText(name);
        if (donorphone.equals(loggedInUserPhone)) {
            phoneTextView.setText(donorphone);

        } else {
            phoneTextView.setText(getResources().getString(R.string.not_permitted));
        }
        bloodGroupTextView.setText(bloodGroup);
        addressTextView.setText(address);
        ageTextView.setText(age);
        donorInfoTextView.setText(donorInfo);

        if (findPatientName.equals("") && findPatientAge.equals("") && findPatientPhone.equals("") && findPatientBloodGroup.equals("any")) {
            askForHelpBtn.setVisibility(View.GONE);
            sendRequestSuggestion.setVisibility(View.VISIBLE);

        }
        else{
            requestsOperation("getStatus");
        }

        downloadImage(donorphone);



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        askForHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(askForHelpBtn.getText().toString().equals(getResources().getString(R.string.ask_for_help))) {
                    askForHelpAlertDialog();
                    askForHelpBtn.setEnabled(false);
                }


            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acceptBtn.getText().toString().equals(getResources().getString(R.string.accept_request))) {
                    acceptBtn.setEnabled(false);
                    requestOperationAlertDialog("accept",donorphone,getResources().getString(R.string.donor_profile_activity_notification_accepted_1),loggedInUserName + " " + getResources().getString(R.string.donor_profile_activity_notification_accepted_2),"PatientResponseActivity","");

                } else if (acceptBtn.getText().toString().equals(getResources().getString(R.string.donor_profile_activity_Call_Donor))) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + donorphone));
                    startActivity(intent);
                }
            }
            });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(declineBtn.getText().toString().equals(getResources().getString(R.string.decline_request))) {
                    declineBtn.setEnabled(false);
                    requestOperationAlertDialog("decline",donorphone,getResources().getString(R.string.donor_profile_activity_notification_declined_1),loggedInUserName + " " + getResources().getString(R.string.donor_profile_activity_notification_declined_2),"PatientResponseActivity","");

                }
                else if(declineBtn.getText().toString().equals(getResources().getString(R.string.send_sms))){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("smsto:"));
                    intent.putExtra("address", donorphone);
                    intent.putExtra("sms_body","Hello! I would like to contact with you.");
                    startActivity(intent);
                }

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestedBy.equals("donor")) {
                    requestOperationAlertDialog("confirm", donorphone, getResources().getString(R.string.donor_profile_activity_notification_confirmed_1), loggedInUserName + " " + getResources().getString(R.string.donor_profile_activity_notification_confirmed_2), "PatientResponseActivity", "donor");
                }
                else if (requestedBy.equals("patient")) {
                    requestOperationAlertDialog("confirm", donorphone, getResources().getString(R.string.donor_profile_activity_notification_confirmed_1), loggedInUserName + " " + getResources().getString(R.string.donor_profile_activity_notification_confirmed_2), "RequestsFromPatientsActivity", "patient");

                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestedBy.equals("donor")) {
                    requestOperationAlertDialog("not_confirm", donorphone, getResources().getString(R.string.donor_profile_activity_notification_not_confirmed_1), loggedInUserName + " " + getResources().getString(R.string.donor_profile_activity_notification_not_confirmed_2), "PatientResponseActivity", "donor");
                }
                else if (requestedBy.equals("patient")) {
                    requestOperationAlertDialog("not_confirm", donorphone, getResources().getString(R.string.donor_profile_activity_notification_not_confirmed_1), loggedInUserName + " " + getResources().getString(R.string.donor_profile_activity_notification_not_confirmed_2), "RequestsFromPatientsActivity", "patient");

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void askForHelpAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewDonorProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.donor_profile_activity_send_request));
        builder.setCancelable(false);

        builder.setPositiveButton(getResources().getString(R.string.yes_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendRequest();

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                askForHelpBtn.setEnabled(true);
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    private void requestOperationAlertDialog(String getstatus, String phonenumber, String title, String body, String activity, String hidden) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewDonorProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));
        builder.setCancelable(false);

        builder.setPositiveButton(getResources().getString(R.string.yes_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestsOperation(getstatus);
                RetroInterface retroInterface = RetroInstance.getRetro();
                Call<UserDataModel> incomingResponse = retroInterface.sendNotification(phonenumber,title,body,activity,hidden);
                incomingResponse.enqueue(new Callback<UserDataModel>() {
                    @Override
                    public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                        acceptBtn.setEnabled(true);
                        declineBtn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<UserDataModel> call, Throwable t) {

                        acceptBtn.setEnabled(true);
                        declineBtn.setEnabled(true);
                    }
                });


                
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                acceptBtn.setEnabled(true);
                declineBtn.setEnabled(true);

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

    }

    private void sendRequest() {
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<RequestDataModel> requestFromPatient = retroInterface.sendRequest(donorphone, findPatientName, findPatientAge, findPatientBloodGroup, findPatientDate, findPatientPhone,findPatientNeed, "patient", "Pending");
        requestFromPatient.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                progressBar.setVisibility(View.GONE);
                askForHelpBtn.setEnabled(true);
                if (response.body().getServerMsg().equals("Success")) {
                    ToastCreator.toastCreatorGreen(ViewDonorProfileActivity.this, getResources().getString(R.string.donor_profile_activity_Request_Sent));
                    progressBar.setVisibility(View.GONE);




                    //Push Notification


                    RetroInterface retroInterface = RetroInstance.getRetro();
                    Call<UserDataModel> incomingResponse = retroInterface.sendNotification(donorphone,getResources().getString(R.string.donor_profile_activity_notification_incoming_1),findPatientName +" "+getResources().getString(R.string.donor_profile_activity_notification_incoming_2),"RequestsFromPatientsActivity","");
                    incomingResponse.enqueue(new Callback<UserDataModel>() {
                        @Override
                        public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                        }

                        @Override
                        public void onFailure(Call<UserDataModel> call, Throwable t) {

                        }
                    });



                }
                else if(response.body().getServerMsg().equals("This Request Already exists! Wait for Response")){
                    ToastCreator.toastCreatorRed(ViewDonorProfileActivity.this, getResources().getString(R.string.already_exists));
                    progressBar.setVisibility(View.GONE);

                }
                else{
                    ToastCreator.toastCreatorRed(ViewDonorProfileActivity.this, getResources().getString(R.string.connection_failed_try_again));
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(ViewDonorProfileActivity.this, getResources().getString(R.string.connection_error));
                progressBar.setVisibility(View.GONE);
                askForHelpBtn.setEnabled(true);

            }
        });


    }

    private void requestsOperation(String operation) {
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();


        Call<RequestDataModel> lookforRequestFromPatient = retroInterface.requestsOperation(donorphone, findPatientName, findPatientAge, findPatientBloodGroup, findPatientDate, findPatientPhone, findPatientNeed, requestedBy, operation);
        lookforRequestFromPatient.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                progressBar.setVisibility(View.GONE);
                acceptBtn.setEnabled(true);
                declineBtn.setEnabled(true);
                if (response.isSuccessful()) {

                    if (response.body().getServerMsg().isEmpty()) {
                        Toast.makeText(ViewDonorProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                   else if(response.body().getServerMsg().toLowerCase().equals("no requests")) {
                        if(donorphone.equals(loggedInUserPhone)){
                            askForHelpBtn.setVisibility(View.GONE);
                            acceptBtn.setVisibility(View.GONE);
                            declineBtn.setVisibility(View.GONE);
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                        } else {
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.ask_for_help));
                            acceptBtn.setVisibility(View.GONE);
                            declineBtn.setVisibility(View.GONE);
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                        }
                    }
                    else if (response.body().getServerMsg().toLowerCase().equals("pending")) {
                        if(getIntent().getStringExtra("activity").equals("DonorResponseActivity")  || getIntent().getStringExtra("activity").equals("FindDonorActivity")){
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.pending));
                            acceptBtn.setVisibility(View.GONE);
                            declineBtn.setVisibility(View.GONE);
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                        }
                        else {
                            askForHelpBtn.setVisibility(View.GONE);


                                acceptBtn.setVisibility(View.VISIBLE);
                                acceptBtn.setText(getResources().getString(R.string.accept_request));
                                declineBtn.setVisibility(View.VISIBLE);
                                declineBtn.setText(getResources().getString(R.string.decline_request));


                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                        }

                    }
                    else if (response.body().getServerMsg().toLowerCase().equals("accepted")) {
                        if(getIntent().getStringExtra("activity").equals("DonorResponseActivity") ||getIntent().getStringExtra("activity").equals("RequestsFromDonorsActivity") || getIntent().getStringExtra("activity").equals("FindDonorActivity")){
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.accepted));
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }
                        else {
                            askForHelpBtn.setVisibility(View.GONE);
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }

                    }

                    else if (response.body().getServerMsg().toLowerCase().equals("claimed")) {
                        if(getIntent().getStringExtra("activity").equals("DonorResponseActivity")||getIntent().getStringExtra("activity").equals("RequestsFromDonorsActivity") || getIntent().getStringExtra("activity").equals("FindDonorActivity")){
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getString(R.string.claimed));
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));


                                confirmBtn.setVisibility(View.VISIBLE);
                                confirmBtn.setText(getResources().getString(R.string.confirm_donation));
                                cancelBtn.setVisibility(View.VISIBLE);
                                cancelBtn.setText(getResources().getString(R.string.decline_donation));
                                phoneTextView.setText(donorphone);

                        }
                        else {
                            askForHelpBtn.setVisibility(View.GONE);
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }

                    }

                    else if (response.body().getServerMsg().toLowerCase().equals("not_donated")) {
                        if(getIntent().getStringExtra("activity").equals("DonorResponseActivity")||getIntent().getStringExtra("activity").equals("RequestsFromDonorsActivity") || getIntent().getStringExtra("activity").equals("FindDonorActivity")){
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.not_donated));
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }
                        else {
                            askForHelpBtn.setVisibility(View.GONE);
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }

                    }

                    else if (response.body().getServerMsg().toLowerCase().equals("donated")) {

                        if(getIntent().getStringExtra("activity").equals("DonorResponseActivity") || getIntent().getStringExtra("activity").equals("RequestsFromDonorsActivity") || getIntent().getStringExtra("activity").equals("FindDonorActivity")){
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.donated));
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }
                        else {
                            askForHelpBtn.setVisibility(View.GONE);
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }

                    }



                    else if (response.body().getServerMsg().toLowerCase().equals("declined")) {
                        if(getIntent().getStringExtra("activity").equals("DonorResponseActivity") ||getIntent().getStringExtra("activity").equals("RequestsFromDonorsActivity") || getIntent().getStringExtra("activity").equals("FindDonorActivity")){
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.declined));
                            acceptBtn.setVisibility(View.GONE);
                            declineBtn.setVisibility(View.GONE);
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                        }
                        else {
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.declined));
                            acceptBtn.setVisibility(View.GONE);
                            declineBtn.setVisibility(View.GONE);
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                        }
                    }

                    else if (response.body().getServerMsg().toLowerCase().equals("canceled")) {
                        if(getIntent().getStringExtra("activity").equals("DonorResponseActivity") ||getIntent().getStringExtra("activity").equals("RequestsFromDonorsActivity") || getIntent().getStringExtra("activity").equals("FindDonorActivity")){
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.canceled));
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }
                        else {
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText(getResources().getString(R.string.canceled));
                            acceptBtn.setVisibility(View.VISIBLE);
                            acceptBtn.setText(getResources().getString(R.string.donor_profile_activity_Call_Donor));
                            declineBtn.setVisibility(View.VISIBLE);
                            declineBtn.setText(getResources().getString(R.string.send_sms));
                            confirmBtn.setVisibility(View.GONE);
                            cancelBtn.setVisibility(View.GONE);
                            phoneTextView.setText(donorphone);
                        }
                    }

                    else {
                        Toast.makeText(ViewDonorProfileActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(ViewDonorProfileActivity.this, getResources().getString(R.string.connection_error));
                progressBar.setVisibility(View.GONE);
                acceptBtn.setEnabled(true);
                declineBtn.setEnabled(true);

            }
        });

    }








    private Bitmap scaleImage(Bitmap bitmap) {


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(150);


        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return scaledBitmap;

    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }




    private void showImage(ImageView view, Bitmap bitmap, int drawable) {

        BitmapDrawable result = new BitmapDrawable(bitmap);
        view.setImageDrawable(result);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap bmp = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                drawable, o);
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = 5*width;
        params.height = 5*height;
        view.setLayoutParams(params);
    }




    private void downloadImage(String title) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ImageDataModel> incomingResponse = retroInterface.downloadImage(title);
        incomingResponse.enqueue(new Callback<ImageDataModel>() {
            @Override
            public void onResponse(Call<ImageDataModel> call, Response<ImageDataModel> response) {

                if(response.body().getServerMsg().toLowerCase().equals("true")){
                    String image = response.body().getImage();
                    byte[] imageByte = Base64.decode(image, Base64.DEFAULT);
                    insertBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                    insertBitmap = scaleImage(insertBitmap);
                    showImage(genderImageView, insertBitmap, R.drawable.profile_icon_male);
                }

                else if(response.body().getServerMsg().toLowerCase().equals("false")) {

                    if (loggedInUserGender.toLowerCase().equals("male")) {
                        genderImageView.setImageResource(R.drawable.profile_icon_male);
                    } else if (loggedInUserGender.toLowerCase().equals("female")) {
                        genderImageView.setImageResource(R.drawable.profile_icon_female);
                    }
                }

            }

            @Override
            public void onFailure(Call<ImageDataModel> call, Throwable t) {

                if (loggedInUserGender.toLowerCase().equals("male")) {
                    genderImageView.setImageResource(R.drawable.profile_icon_male);
                } else if (loggedInUserGender.toLowerCase().equals("female")) {
                    genderImageView.setImageResource(R.drawable.profile_icon_female);
                }
            }
        });

    }


}
