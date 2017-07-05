package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.shop440.Models.User;
import com.shop440.Utils.Image;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SMILECS on 12/26/16.
 */

public class SignUp extends AppCompatActivity {
    private Uri fileUri;
    User user;
    Context c;
    private static final int PICK_IMAGE = 1;
    @BindView(R.id.imageView) ImageView profile;
    @BindView(R.id.phone) EditText phone;
    @BindView(R.id.name) EditText name;
    @OnClick(R.id.create) void submit(){
        user.setName(phone.getText().toString());
        user.setName(name.getText().toString());
        user.setPhone(phone.getText().toString());
        Intent i = new Intent(c, Confirm.class);
        i.putExtra("phone", phone.getText().toString());
        i.putExtra("user", user);
        startActivity(i);
        finish();
    }
    @OnClick(R.id.imageView) void Submit(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Prevent crash if no app can handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_IMAGE);
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //json = new JSONObject();
        user = new User();
        c = this;
        ButterKnife.bind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data.getData() != null) {
            Log.d("working", "true");
            //model.setType("2");
            fileUri = data.getData();
            Image image = new Image(this, fileUri);
            Bitmap bt = getResizedBitmap(image.getBitmapFromUri(), 120, 120);
            profile.setImageBitmap(bt);
            String base = Base64.encodeToString(image.bitmapToByteArray(bt), Base64.DEFAULT);
            Log.d("base64", base);
            user.setImage(base);
        }

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
