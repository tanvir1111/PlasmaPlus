package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.FindPatientData;
import com.ece.cov19.DataModels.ImageDataModel;
import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.R;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserGender;

public class SearchDonorAdapter extends RecyclerView.Adapter<SearchDonorViewHolder>{

    public Context context;
    public UserDataModel userDataModel;
    public ArrayList<UserDataModel> userDataModels;

    public static final String Language_pref="Language";
    public static final String Selected_language="Selected Language";
    SharedPreferences langPrefs;

    Bitmap insertBitmap;
    Uri imageUri;

    public SearchDonorAdapter(Context context, ArrayList<UserDataModel> userDataModels) {
        this.context = context;
        this.userDataModels = userDataModels;
    }


    @NonNull
    @Override
    public SearchDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.search_donor_child, parent, false);
        SearchDonorViewHolder searchDonorViewHolder = new SearchDonorViewHolder(view, userDataModels);
        return searchDonorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchDonorViewHolder holder, int position) {

        langPrefs=context.getSharedPreferences(Language_pref,MODE_PRIVATE);
        if(langPrefs.contains(Selected_language)){
            setLocale(langPrefs.getString(Selected_language,""));

        }

        userDataModel = userDataModels.get(position);

        downloadImage(userDataModel.getPhone(), holder.donorImageView, userDataModel.getGender());


        if(userDataModel.getBloodGroup().equals(FindPatientData.findPatientBloodGroup)) {
            holder.askButton.setVisibility(View.VISIBLE);
        }

        holder.nameTextView.setText(userDataModel.getName());
        holder.locationTextView.setText(userDataModel.getDistrict());
        holder.bloodTextView.setText(userDataModel.getBloodGroup());
        if(userDataModel.getDonor().equals("Blood")){
            holder.donortype.setText(holder.itemView.getContext().getResources().getString(R.string.blood));
        }
        else if(userDataModel.getDonor().equals("Plasma")){
            holder.donortype.setText(holder.itemView.getContext().getResources().getString(R.string.plasma));
        }
        else if(userDataModel.getDonor().equals("Blood and Plasma")){
            holder.donortype.setText(holder.itemView.getContext().getResources().getString(R.string.bloodandplasma));
        }

        if(userDataModel.getGender().toLowerCase().equals("male")) {
            holder.donorImageView.setImageResource(R.drawable.profile_icon_male);
        } else {
            holder.donorImageView.setImageResource(R.drawable.profile_icon_female);
        }
        holder.locationImageView.setImageResource(R.drawable.location_icon);

    }

    @Override
    public int getItemCount() {
        return userDataModels.size();
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
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    private void showImage(ImageView view, Bitmap bitmap, int drawable) {

        BitmapDrawable result = new BitmapDrawable(bitmap);
        view.setImageDrawable(result);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                drawable, o);
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = 2*width;
        params.height = 2*height;
        view.setLayoutParams(params);
    }

    private void showDrawable(ImageView view, int drawable) {
        view.setImageResource(drawable);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                drawable, o);
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = 2*width;
        params.height = 2*height;
        view.setLayoutParams(params);
    }



    private void downloadImage(String title, ImageView genderImageView, String gender) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ImageDataModel> incomingResponse = retroInterface.downloadImage(title);
        incomingResponse.enqueue(new Callback<ImageDataModel>() {
            @Override
            public void onResponse(Call<ImageDataModel> call, Response<ImageDataModel> response) {

                if (response.body().getServerMsg().toLowerCase().equals("true")) {
                    String image = response.body().getImage();
                    byte[] imageByte = Base64.decode(image, Base64.DEFAULT);
                    insertBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                    insertBitmap = scaleImage(insertBitmap);
                    showImage(genderImageView, insertBitmap, R.drawable.profile_icon_male);
                } else if (response.body().getServerMsg().toLowerCase().equals("false")) {

                    if (gender.toLowerCase().toLowerCase().equals("male")) {
                        showDrawable(genderImageView, R.drawable.profile_icon_male);
                    } else if (gender.toLowerCase().toLowerCase().equals("female")) {
                        showDrawable(genderImageView, R.drawable.profile_icon_female);
                    }
                }

            }

            @Override
            public void onFailure(Call<ImageDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(context, "Profile Image retrieve failed. " + t.getMessage());


                if (gender.toLowerCase().equals("male")) {
                    genderImageView.setImageResource(R.drawable.profile_icon_male);
                } else if (gender.toLowerCase().equals("female")) {
                    genderImageView.setImageResource(R.drawable.profile_icon_female);
                }
            }
        });

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        langPrefs = context.getSharedPreferences(Language_pref,MODE_PRIVATE);
        SharedPreferences.Editor langPrefsEditor = langPrefs.edit();
        langPrefsEditor.putString(Selected_language, lang);
        langPrefsEditor.apply();

    }

}
