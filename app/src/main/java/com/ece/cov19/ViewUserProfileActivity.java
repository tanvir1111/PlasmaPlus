package com.ece.cov19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ece.cov19.DataModels.ImageDataModel;
import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserAge;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserBloodGroup;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDistrict;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDonorInfo;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserGender;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPass;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserThana;
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class ViewUserProfileActivity extends AppCompatActivity {
    private TextView nameTextView, phoneTextView, bloodGroupTextView, addressTextView, ageTextView, donorInfoTextView;
    private ImageView genderImageView,backbtn,profileImageView;
    private Button logoutBtn,updateInfoBtn,updatePasswordBtn, deleteBtn;
    private ProgressBar progressBar;
    Bitmap insertBitmap;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);

        nameTextView = findViewById(R.id.profile_name);
        phoneTextView = findViewById(R.id.profile_phone);
        bloodGroupTextView = findViewById(R.id.profile_blood_group);
        addressTextView = findViewById(R.id.profile_address);
        ageTextView = findViewById(R.id.profile_age);
        donorInfoTextView = findViewById(R.id.profile_type);
        genderImageView = findViewById(R.id.profile_gender_icon);
        profileImageView = findViewById(R.id.profile_image_upload);
        backbtn=findViewById(R.id.profile_back_button);
        logoutBtn=findViewById(R.id.profile_logout_btn);
        updateInfoBtn=findViewById(R.id.profile_update_button);
        updatePasswordBtn=findViewById(R.id.profile_change_password_btn);
        deleteBtn = findViewById(R.id.profile_delete_button);
        progressBar = findViewById(R.id.profile_progressBar);

//      setting logged in user info
        nameTextView.setText(loggedInUserName);
        phoneTextView.setText(loggedInUserPhone);
        bloodGroupTextView.setText(loggedInUserBloodGroup);
        addressTextView.setText(String.format("%s, %s", loggedInUserThana, loggedInUserDistrict));
        ageTextView.setText(loggedInUserAge);

        switch (loggedInUserDonorInfo){
            case "Blood":
                donorInfoTextView.setText(R.string.profile_blood);
                break;
            case "Plasma":
                donorInfoTextView.setText(R.string.profile_plasma);
                break;
            case "Blood and Plasma":
                donorInfoTextView.setText(R.string.profile_blood_and_plasma);
                break;
            case "None":
                donorInfoTextView.setText(R.string.profile_none);
                break;
        }

        downloadImage(loggedInUserPhone);



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutBtn.setEnabled(false);

               logoutAlertDialog();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.Mystyle);
                PopupMenu popupMenu = new PopupMenu(wrapper, profileImageView);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.activity_view_user_profile_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.add_image) {
                           addImage();
                        }

                        else if(id == R.id.delete_image){

                           deleteImageAlertDialog();
                        }
                        return true;
                    }
                });
            }
        });

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewUserProfileActivity.this, UpdateUserProfileActivity.class);
                updateAlertDialog(intent);


            }
        });
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ViewUserProfileActivity.this, UpdatePasswordActivity.class);
                intent.putExtra("phone", loggedInUserPhone);
                confirmAlertDialog(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProfileAlertDialog();
            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_view_user_profile);
        if(LoginUser.checkLoginStat().equals("failed")){
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            String phone,password;

            if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
                phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
                password= sharedPreferences.getString(LOGIN_USER_PASS, "");

                LoginUser.loginUser(this,phone,password,ViewUserProfileActivity.class);
            }
            else {
                ToastCreator.toastCreatorRed(this,getString(R.string.login_failed));
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        nameTextView = findViewById(R.id.profile_name);
        phoneTextView = findViewById(R.id.profile_phone);
        bloodGroupTextView = findViewById(R.id.profile_blood_group);
        addressTextView = findViewById(R.id.profile_address);
        ageTextView = findViewById(R.id.profile_age);
        donorInfoTextView = findViewById(R.id.profile_type);
        genderImageView = findViewById(R.id.profile_gender_icon);
        profileImageView = findViewById(R.id.profile_image_upload);
        backbtn=findViewById(R.id.profile_back_button);
        logoutBtn=findViewById(R.id.profile_logout_btn);
        updateInfoBtn=findViewById(R.id.profile_update_button);
        updatePasswordBtn=findViewById(R.id.profile_change_password_btn);
        deleteBtn = findViewById(R.id.profile_delete_button);
        progressBar = findViewById(R.id.profile_progressBar);

//      setting logged in user info
        nameTextView.setText(loggedInUserName);
        phoneTextView.setText(loggedInUserPhone);
        bloodGroupTextView.setText(loggedInUserBloodGroup);
        addressTextView.setText(String.format("%s, %s", loggedInUserThana, loggedInUserDistrict));
        ageTextView.setText(loggedInUserAge);


        switch (loggedInUserDonorInfo){
            case "Blood":
                donorInfoTextView.setText(R.string.profile_blood);
                break;
            case "Plasma":
                donorInfoTextView.setText(R.string.profile_plasma);
                break;
            case "Blood and Plasma":
                donorInfoTextView.setText(R.string.profile_blood_and_plasma);
                break;
            case "None":
                donorInfoTextView.setText(R.string.profile_none);
                break;
        }
        downloadImage(loggedInUserPhone);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutBtn.setEnabled(false);
              logoutAlertDialog();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.Mystyle);
                PopupMenu popupMenu = new PopupMenu(wrapper, profileImageView);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.activity_view_user_profile_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.add_image) {
                            addImage();
                        }
                        else if(id == R.id.delete_image){
                            deleteImageAlertDialog();
                        }
                        return true;
                    }
                });
            }
        });

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfoBtn.setEnabled(false);
                Intent intent=new Intent(ViewUserProfileActivity.this, UpdateUserProfileActivity.class);
                updateAlertDialog(intent);


            }
        });
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ViewUserProfileActivity.this, UpdatePasswordActivity.class);
                updatePasswordBtn.setEnabled(false);
                intent.putExtra("phone", loggedInUserPhone);
                confirmAlertDialog(intent);

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBtn.setEnabled(false);
                deleteProfileAlertDialog();

            }
        });
    }


    private void confirmAlertDialog(final Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));
        builder.setCancelable(false);

        builder.setPositiveButton(getResources().getString(R.string.yes_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                showAlertDialog(intent);

            }
        })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        updatePasswordBtn.setEnabled(true);
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void showAlertDialog(final Intent intent) {

//                asking password with alertdialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.enter_password_hint));
        builder.setCancelable(false);

// Set up the input
        final EditText pass = new EditText(ViewUserProfileActivity.this);

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

                    startActivity(intent);
                    updatePasswordBtn.setEnabled(true);
                }
                else{
                    ToastCreator.toastCreatorRed(ViewUserProfileActivity.this, getResources().getString(R.string.wrong_password_error));
                    updatePasswordBtn.setEnabled(true);
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                updatePasswordBtn.setEnabled(true);
            }
        });
        AlertDialog alertDialog =builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.show();

    }



    private void logoutAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.yes_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
//To do//
                                    return;
                                }

// Get the Instance ID token//
                                String token = task.getResult().getToken();
                                String msg = getString(R.string.fcm_token, token);
                                Log.d(TAG, msg);
                                RetroInterface retroInterface = RetroInstance.getRetro();
                                Call<UserDataModel> incomingResponse = retroInterface.deleteToken(LoggedInUserData.loggedInUserPhone,token);
                                incomingResponse.enqueue(new Callback<UserDataModel>() {
                                    @Override
                                    public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                                        if(response.body().getServerMsg().equals("Success")) {
                                            SharedPreferences sharedPreferences=getSharedPreferences(LOGIN_SHARED_PREFS,MODE_PRIVATE);
                                            sharedPreferences.edit().clear().apply();
                                            loggedInUserPhone = "";
                                            Intent login = new Intent(ViewUserProfileActivity.this, LoginActivity.class);
                                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(login);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserDataModel> call, Throwable t) {
                                    logoutBtn.setEnabled(true);
                                    }
                                });

                            }
                        });





            }
        })
                .setNegativeButton(getResources().getString(R.string.profile_activity_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        logoutBtn.setEnabled(true);

                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }


    private void updateAlertDialog(final Intent intent){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.yes_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(intent);
                updateInfoBtn.setEnabled(true);
            }
        })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        updateInfoBtn.setEnabled(true);

                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void deleteProfileAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.delete_profile), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserProfileActivity.this);
                builder.setMessage(getResources().getString(R.string.enter_password_hint));
                builder.setCancelable(false);

                final EditText pass = new EditText(ViewUserProfileActivity.this);

                float density = getResources().getDisplayMetrics().density;
                int paddingDp = (int)(12* density);
                pass.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
                pass.setHint("******");
                pass.setBackgroundResource(R.drawable.edit_text_dark);
                pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(pass);

                builder.setPositiveButton(getResources().getString(R.string.ok_txt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(pass.getText().toString().equals(loggedInUserPass)){

                            AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserProfileActivity.this);
                            builder.setMessage(getResources().getString(R.string.delete_profile_are_you_sure));
                            builder.setCancelable(false);
                            builder.setPositiveButton(getResources().getString(R.string.delete_profile), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {

                                    RetroInterface retroInterface = RetroInstance.getRetro();
                                    Call<UserDataModel> incomingResponse = retroInterface.deleteUserProfile(loggedInUserPhone);
                                    incomingResponse.enqueue(new Callback<UserDataModel>() {
                                        @Override
                                        public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                                            if(response.body().getServerMsg().toLowerCase().equals("success")){
                                                SharedPreferences sharedPreferences=getSharedPreferences(LOGIN_SHARED_PREFS,MODE_PRIVATE);
                                                sharedPreferences.edit().clear().apply();
                                                Intent login= new Intent(ViewUserProfileActivity.this, LoginActivity.class);
                                                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(login);
                                                finish();
                                                ToastCreator.toastCreatorGreen(getApplicationContext(), getResources().getString(R.string.profile_activity_Delete_Successful));

                                            }
                                            else if(response.body().getServerMsg().toLowerCase().equals("failed")){
                                                ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.delete_failed));


                                            }
                                            deleteBtn.setEnabled(true);
                                        }

                                        @Override
                                        public void onFailure(Call<UserDataModel> call, Throwable t) {
                                            ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.connection_error));
                                            deleteBtn.setEnabled(true);
                                        }
                                    });
                                }
                            })
                                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            deleteBtn.setEnabled(true);
                                        }
                                    });

                            AlertDialog finalDialog=builder.create();
                            finalDialog.setCanceledOnTouchOutside(false);
                            finalDialog.show();

                        }
                        else{
                            ToastCreator.toastCreatorRed(ViewUserProfileActivity.this, getResources().getString(R.string.wrong_password_error));
                            deleteBtn.setEnabled(true);
                        }
                    }
                })
                        .setNegativeButton(getResources().getString(R.string.profile_activity_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                deleteBtn.setEnabled(true);
                            }
                        });

                AlertDialog passDialog=builder.create();
                passDialog.setCanceledOnTouchOutside(false);
                passDialog.show();

            }
        })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        deleteBtn.setEnabled(true);
                    }
                });

        AlertDialog firstDialog = builder.create();
        firstDialog.setCanceledOnTouchOutside(false);
        firstDialog.show();
    }



    private void deleteImageAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewUserProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.profile_activity_Delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteImage(loggedInUserPhone);
            }
        })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

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
                    uploadImage(LoggedInUserData.loggedInUserPhone,bmp);

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
                    downloadImage(loggedInUserPhone);
                }
                else if(response.body().getServerMsg().toLowerCase().equals("false")){
                    ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.profile_activity_image_did_not_upload));

                }
            }

            @Override
            public void onFailure(Call<ImageDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.profile_activity_image_upload_failed));

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

    private void deleteImage(String title) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ImageDataModel> incomingResponse = retroInterface.deleteImage(title);
        incomingResponse.enqueue(new Callback<ImageDataModel>() {
            @Override
            public void onResponse(Call<ImageDataModel> call, Response<ImageDataModel> response) {
                if(response.body().getServerMsg().toLowerCase().equals("true")) {
                    downloadImage(loggedInUserPhone);
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
                ToastCreator.toastCreatorRed(getApplicationContext(), getResources().getString(R.string.profile_activity_image_delete_failed));

            }
        });
    }
}
