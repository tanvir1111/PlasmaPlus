package com.ece.cov19;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ece.cov19.DataModels.ImageDataModel;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.RequestDataModel;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.FindPatientData.findPatientAge;
import static com.ece.cov19.DataModels.FindPatientData.findPatientBloodGroup;
import static com.ece.cov19.DataModels.FindPatientData.findPatientName;
import static com.ece.cov19.DataModels.FindPatientData.findPatientPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserBloodGroup;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDonorInfo;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserEligibility;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPass;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class ViewPatientProfileActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, bloodGroupTextView, hospitalTextView, ageTextView, needTextView,dateTextView;
    private ImageView genderImageView,backbtn;
    private ProgressBar progressBar;
    Button donateToHelpButton, updateButton, deleteButton;
    String name, age, gender, bloodGroup, hospital, division, district, date, need, phone,requestedBy;
    Bitmap insertBitmap;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_profile);

        nameTextView = findViewById(R.id.patient_profile_name);
        phoneTextView = findViewById(R.id.patient_profile_phone);
        bloodGroupTextView = findViewById(R.id.patient_profile_blood_group);
        hospitalTextView = findViewById(R.id.patient_profile_hospital);
        ageTextView = findViewById(R.id.patient_profile_age);
        needTextView = findViewById(R.id.patient_profile_need);
        genderImageView = findViewById(R.id.patient_profile_gender_icon);
        backbtn=findViewById(R.id.patient_profile_back_button);
        updateButton = findViewById(R.id.patient_profile_update_button);
        deleteButton = findViewById(R.id.patient_profile_delete_button);
        donateToHelpButton = findViewById(R.id.patient_profile_donate_button);
        dateTextView=findViewById(R.id.patient_profile_date);
        progressBar = findViewById(R.id.patient_profile_progressBar);


        Intent intent = getIntent();


        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        gender = intent.getStringExtra("gender");
        bloodGroup = intent.getStringExtra("blood_group");
        hospital = intent.getStringExtra("hospital");
        division = intent.getStringExtra("division");
        district = intent.getStringExtra("district");
        date = intent.getStringExtra("date");
        need = intent.getStringExtra("need");
        phone = intent.getStringExtra("phone");

        requestedBy="donor";
        if(intent.hasExtra("activity")){
            if(intent.getStringExtra("activity").equals("DonorRequestsActivity") || intent.getStringExtra("activity").equals("ExplorePatientsActivity")){
                requestedBy="patient";
            }
            else{
                requestedBy="donor";
            }
        }

        nameTextView.setText(name);
        if(phone.equals(loggedInUserPhone)){
            phoneTextView.setText(phone);

        }
        else {
            phoneTextView.setText(getResources().getString(R.string.not_permitted));
        }
        bloodGroupTextView.setText(bloodGroup);
        hospitalTextView.setText(hospital);
        ageTextView.setText(age);
        needTextView.setText(need);
        dateTextView.setText(date);


        downloadImage(phone);


        requestsOperation("getStatus");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        donateToHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(donateToHelpButton.getText().toString().toLowerCase().equals(getResources().getString(R.string.donate_to_help).toLowerCase())) {

                    donateToHelpAlertDialog();
                }
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateButton.getText().toString().toLowerCase().equals(getResources().getString(R.string.update_profile).toLowerCase())) {
                    Intent intent = new Intent(ViewPatientProfileActivity.this, UpdatePatientProfileActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("age", age);
                    intent.putExtra("gender", gender);
                    intent.putExtra("blood_group", bloodGroup);
                    intent.putExtra("hospital", hospital);
                    intent.putExtra("division", division);
                    intent.putExtra("district", district);
                    intent.putExtra("date", date);
                    intent.putExtra("need", need);
                    intent.putExtra("phone", phone);
                    updateAlertDialog(intent);
                }
                else if(updateButton.getText().toString().equals(getResources().getString(R.string.accept_request))){
                    requestOperationAlertDialog("accept",phone,getResources().getString(R.string.patient_profile_activity_notification_accepted_1),loggedInUserName +" "+getResources().getString(R.string.patient_profile_activity_notification_accepted_2),"DonorResponseActivity");

                }
                else if(updateButton.getText().toString().toLowerCase().equals(getResources().getString(R.string.patient_profile_activity_Call_Patient).toLowerCase())){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phone));
                    startActivity(intent);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteButton.getText().toString().toLowerCase().equals(getResources().getString(R.string.delete_profile).toLowerCase())) {
                    deleteAlertDialog();
                }
                else if(deleteButton.getText().toString().equals(getResources().getString(R.string.decline_request))){
                    requestOperationAlertDialog("decline",phone,getResources().getString(R.string.patient_profile_activity_notification_declined_1),loggedInUserName +" "+getResources().getString(R.string.patient_profile_activity_notification_declined_2),"DonorResponseActivity");

                }
                else if(deleteButton.getText().toString().toLowerCase().equals(getResources().getString(R.string.send_sms).toLowerCase())){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("smsto:"));
                    intent.putExtra("address", phone);
                    intent.putExtra("sms_body","Hello! I would like to contact with you.");
                    startActivity(intent);
                }
            }

        });




    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }


    private void updateAlertDialog(final Intent intent) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));

        builder.setPositiveButton(getResources().getString(R.string.yes_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }


    private void deleteAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.patient_profile_activity_Delete_profile_question));

        builder.setPositiveButton(getResources().getString(R.string.proceed), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
                builder.setMessage(getResources().getString(R.string.delete_profile_are_you_sure));

// Set up the input
                final EditText pass = new EditText(ViewPatientProfileActivity.this);

                float density = getResources().getDisplayMetrics().density;
                int paddingDp = (int)(12* density);
                pass.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
                pass.setHint("******");
                pass.setBackgroundResource(R.drawable.edit_text_dark);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(pass);

// Set up the buttons
                builder.setPositiveButton(getResources().getString(R.string.ok_txt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(pass.getText().toString().equals(loggedInUserPass)){

                            AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
                            builder.setMessage(getResources().getString(R.string.are_you_sure));
                            builder.setPositiveButton(getResources().getString(R.string.delete_profile), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    RetroInterface retroInterface = RetroInstance.getRetro();
                                    Call<PatientDataModel> deletePatientRecord = retroInterface.deletePatientProfile(name, age, gender, bloodGroup, hospital, division, district, date, need, phone);
                                    deletePatientRecord.enqueue(new Callback<PatientDataModel>() {
                                        @Override
                                        public void onResponse(Call<PatientDataModel> call, Response<PatientDataModel> response) {
                                            progressBar.setVisibility(View.GONE);
                                            if(response.body().getServerMsg().toLowerCase().equals("success")){
                                                ToastCreator.toastCreatorGreen(ViewPatientProfileActivity.this, getResources().getString(R.string.patient_profile_activity_Patient_Record_Deleted));
                                                finish();
                                            }
                                            else{
                                                ToastCreator.toastCreatorRed(ViewPatientProfileActivity.this, getResources().getString(R.string.delete_failed));

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<PatientDataModel> call, Throwable t) {
                                            ToastCreator.toastCreatorRed(ViewPatientProfileActivity.this, getResources().getString(R.string.connection_error));
                                        }
                                    });


                                }
                            })
                                    .setNegativeButton(getResources().getString(R.string.go_back), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });

                            AlertDialog alertDialog=builder.create();
                            alertDialog.show();


                        }
                        else{
                            ToastCreator.toastCreatorRed(ViewPatientProfileActivity.this, getResources().getString(R.string.wrong_password_error));;
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();



//                asking password with alertdialog


    }


    private void donateToHelpAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);

        builder.setMessage(getResources().getString(R.string.donor_profile_activity_send_request));


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
            }
        });

        builder.show();

    }

    private void requestOperationAlertDialog(String getstatus, String phonenumber, String title, String body, String activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));


        builder.setPositiveButton(getResources().getString(R.string.yes_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestsOperation(getstatus);

                RetroInterface retroInterface = RetroInstance.getRetro();
                Call<UserDataModel> incomingResponse = retroInterface.sendNotification(phonenumber,title,body,activity);
                incomingResponse.enqueue(new Callback<UserDataModel>() {
                    @Override
                    public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                    }

                    @Override
                    public void onFailure(Call<UserDataModel> call, Throwable t) {

                    }
                });

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void sendRequest() {
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<RequestDataModel> requestFromDonor = retroInterface.sendRequest(loggedInUserPhone, findPatientName, findPatientAge, findPatientPhone, findPatientBloodGroup, "donor");
        requestFromDonor.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getServerMsg().toLowerCase().equals("success")) {
                    ToastCreator.toastCreatorGreen(ViewPatientProfileActivity.this, getResources().getString(R.string.patient_profile_activity_Request_Sent));

                    //Push Notification


                    RetroInterface retroInterface = RetroInstance.getRetro();
                    Call<UserDataModel> incomingResponse = retroInterface.sendNotification(phone,getResources().getString(R.string.patient_profile_activity_notification_incoming_1),loggedInUserName+" "+getResources().getString(R.string.patient_profile_activity_notification_incoming_2),"PatientRequestsActivity");
                    incomingResponse.enqueue(new Callback<UserDataModel>() {
                        @Override
                        public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                        }

                        @Override
                        public void onFailure(Call<UserDataModel> call, Throwable t) {

                        }
                    });

                } else {
                    ToastCreator.toastCreatorRed(ViewPatientProfileActivity.this, getResources().getString(R.string.connection_failed_try_again));
                }
            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(ViewPatientProfileActivity.this, getResources().getString(R.string.connection_error));

            }
        });


    }

    private void requestsOperation(String operation) {
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<RequestDataModel> lookforRequestFromDonor = retroInterface.requestsOperation(loggedInUserPhone, name, age, bloodGroup,phone,requestedBy,operation);
        lookforRequestFromDonor.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getServerMsg().toLowerCase().equals("no requests")){
                        if(phone.equals(loggedInUserPhone)){
                            donateToHelpButton.setVisibility(View.GONE);
                            updateButton.setVisibility(View.VISIBLE);
                            updateButton.setText(getResources().getString(R.string.update_profile));
                            deleteButton.setVisibility(View.VISIBLE);
                            deleteButton.setText(getResources().getString(R.string.delete_profile));
                        }
                        else if(loggedInUserEligibility.toLowerCase().equals("not_eligible")){
                            donateToHelpButton.setVisibility(View.GONE);
                            updateButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.GONE);
                        }

                        else if(bloodGroup.equals(loggedInUserBloodGroup)) {
                            if(need.toLowerCase().equals("blood") && (loggedInUserDonorInfo.toLowerCase().equals("blood")||
                            loggedInUserDonorInfo.toLowerCase().equals("blood and plasma"))) {
                                donateToHelpButton.setVisibility(View.VISIBLE);
                                donateToHelpButton.setText(getResources().getString(R.string.donate_to_help));
                            }
                            else if(need.toLowerCase().equals("plasma") && (loggedInUserDonorInfo.toLowerCase().equals("plasma")||
                                    loggedInUserDonorInfo.toLowerCase().equals("blood and plasma"))){
                                donateToHelpButton.setVisibility(View.VISIBLE);
                                donateToHelpButton.setText(getResources().getString(R.string.donate_to_help));
                            }
                            else{
                                donateToHelpButton.setVisibility(View.GONE);
                            }
                            updateButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.GONE);
                        }
                        else {
                            donateToHelpButton.setVisibility(View.GONE);
                            updateButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.GONE);
                        }
                    }
                    else if(response.body().getServerMsg().toLowerCase().equals("pending")){
                        if(getIntent().getStringExtra("activity").equals("PatientResponseActivity")){

                            donateToHelpButton.setVisibility(View.VISIBLE);
                            donateToHelpButton.setText(getResources().getString(R.string.pending));
                            updateButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.GONE);
                        }

                        else {
                            donateToHelpButton.setVisibility(View.GONE);
                            updateButton.setVisibility(View.VISIBLE);
                            updateButton.setText(getResources().getString(R.string.accept_request));
                            deleteButton.setVisibility(View.VISIBLE);
                            deleteButton.setText(getResources().getString(R.string.decline_request));
                        }


                    }
                    else if(response.body().getServerMsg().equals("Accepted")){

                            donateToHelpButton.setVisibility(View.GONE);
                            updateButton.setVisibility(View.VISIBLE);
                            updateButton.setText(getResources().getString(R.string.patient_profile_activity_Call_Patient));
                            deleteButton.setVisibility(View.VISIBLE);
                            deleteButton.setText(getResources().getString(R.string.send_sms));
                            phoneTextView.setText(phone);


                    }
                    else if(response.body().getServerMsg().equals("Declined")){

                            donateToHelpButton.setVisibility(View.VISIBLE);
                            donateToHelpButton.setText(getResources().getString(R.string.decline_request));
                            updateButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.GONE);
                        

                    }


                }

            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(ViewPatientProfileActivity.this, getResources().getString(R.string.connection_error));

            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                try {

                    Bitmap bmp = getBitmapFromUri(imageUri);
                    bmp = scaleImage(bmp);
                    uploadImage(findPatientPhone,bmp);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    private void addImage() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    private void editImage(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
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



    private String convertToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }







    private void uploadImage(String title, Bitmap bitmap) {
        String image = convertToString(bitmap);
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ImageDataModel> incomingResponse = retroInterface.uploadImage(title, image);
        incomingResponse.enqueue(new Callback<ImageDataModel>() {
            @Override
            public void onResponse(Call<ImageDataModel> call, Response<ImageDataModel> response) {

                if(response.body().getServerMsg().toLowerCase().equals("true")) {
                    downloadImage(findPatientPhone);
                }
                else if(response.body().getServerMsg().toLowerCase().equals("false")){
                    ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.connection_failed_try_again));

                }
            }

            @Override
            public void onFailure(Call<ImageDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.connection_error));

            }
        });
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

                    if (gender.toLowerCase().equals("male")) {
                        genderImageView.setImageResource(R.drawable.profile_icon_male);
                    } else if (gender.toLowerCase().equals("female")) {
                        genderImageView.setImageResource(R.drawable.profile_icon_female);
                    }
                }

            }

            @Override
            public void onFailure(Call<ImageDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.connection_error));


                if (gender.toLowerCase().equals("male")) {
                    genderImageView.setImageResource(R.drawable.profile_icon_male);
                } else if (gender.toLowerCase().equals("female")) {
                    genderImageView.setImageResource(R.drawable.profile_icon_female);
                }
            }
        });

    }

    private void deleteImage(String title) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ImageDataModel> incomingResponse = retroInterface.deleteImage(title);
        incomingResponse.enqueue(new Callback<ImageDataModel>() {
            @Override
            public void onResponse(Call<ImageDataModel> call, Response<ImageDataModel> response) {
                if(response.body().getServerMsg().toLowerCase().equals("true")) {
                    downloadImage(findPatientPhone);
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
                    Bitmap bmp = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.profile_icon_male, o);
                    int width = bmp.getWidth();
                    int height = bmp.getHeight();

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) genderImageView.getLayoutParams();
                    params.width = 3*width;
                    params.height = 3*height;
                    genderImageView.setLayoutParams(params);


                }
                else if(response.body().getServerMsg().toLowerCase().equals("false")){
                    ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.image_not_found));

                }
            }

            @Override
            public void onFailure(Call<ImageDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.connection_error));

            }
        });
    }





}
