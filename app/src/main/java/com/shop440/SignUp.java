package com.shop440;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.shop440.Models.User;
import com.shop440.Utils.Image;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SMILECS on 12/26/16.
 */

public class SignUp extends AppCompatActivity {
    private Uri fileUri;
    User user;
    JSONObject json;
    RequestQueue queue;
    Context c;
    VolleySingleton volleySingleton;
    private static final int PICK_IMAGE = 1;
    @BindView(R.id.imageView) CircleImageView profile;
    @BindView(R.id.phone) EditText phone;
    @BindView(R.id.name) EditText name;
    @OnClick(R.id.create) void submit(){
        user.setFullname(phone.getText().toString());
        user.setFullname(name.getText().toString());
        Intent i = new Intent(c, confirm.class);
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
        json = new JSONObject();
        user = new User();
        volleySingleton = VolleySingleton.getsInstance();
        queue = volleySingleton.getmRequestQueue();
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
            profile.setImageBitmap(image.getBitmapFromUri());
            String base = Base64.encodeToString(image.bitmapToByteArray(image.getBitmapFromUri()), Base64.CRLF);
            user.setImage(base);
        }

    }
}
