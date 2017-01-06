package com.shop440;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Models.SmsListener;
import com.shop440.Models.User;
import com.shop440.Receiver.SmsReciever;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONObject;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class confirm extends AppCompatActivity {
    private String phone;
    private String serverCode;
    RequestQueue queue;
    private String query;
    JSONObject json;
    User user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    VolleySingleton volleySingleton;
    String compare;
    @BindView(R.id.editText) EditText passcode;
    @BindView(R.id.feedback) TextView feedback;
    @BindView(R.id.progressBar) ProgressBar bar;
    @BindView(R.id.button) Button continueButton;
    @OnClick(R.id.button) void submit(){
        if(ComparePasscode(passcode.getText().toString(), compare)){
            get_token();
            Intent i = new Intent(confirm.this, MainActivity.class);
            startActivity(i);
            finish();
        }else {
            feedback.setVisibility(View.VISIBLE);
            feedback.setTextColor(Color.parseColor("Red"));
            feedback.setText("Incorrect Passcode!");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // BroadcastReceiver reciever = new SmsReciever();
        setContentView(R.layout.confirmation);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        phone = getIntent().getStringExtra("phone");
        user = (User) getIntent().getSerializableExtra("user");
        //phone = "me";
        volleySingleton = VolleySingleton.getsInstance();
        queue = volleySingleton.getmRequestQueue();
        SmsReciever.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text",messageText);
                Toast.makeText(confirm.this,"Loading Passcode",Toast.LENGTH_LONG).show();
                String[] newString = messageText.split("");
                if(ComparePasscode(newString[newString.length -1], compare)){
                    get_token();
                    Intent i = new Intent(confirm.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });
        Validate(phone);
        ButterKnife.bind(this);
        continueButton.setEnabled(false);
        passcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(i1 > 3){
                    continueButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void Validate(String q){
        try{
            q = URLEncoder.encode(phone, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Urls.BASE_URL + Urls.PASSCODE + "?phone=" + URLEncoder.encode(q), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    compare = response.getString("Passcode");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                feedback.setVisibility(View.VISIBLE);
                feedback.setText("Unable to process request. Please click Resend passcode!");
            }
        });
        queue.add(jsonObjectRequest);
    }

    private Boolean ComparePasscode(String fromSms, String fromServer){
        json = new JSONObject();
        if(fromSms.equals(fromServer)){
            try{
                bar.setVisibility(View.VISIBLE);
                feedback.setVisibility(View.VISIBLE);
                feedback.setText("Authenticating.....");
                continueButton.setVisibility(View.GONE);
                passcode.setVisibility(View.GONE);
                json.put("Phone", user.getPhone());
                json.put("Name", user.getFullname());
                json.put("Image", user.getImage());
                json.put("Passcode", fromSms);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.NEW_USER, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
                queue.add(jsonObjectRequest);
            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }

    private void get_token(){

        feedback.setText("Loading token");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.GET_TOKEN, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    editor.putString("token", response.getString("Token"));
                    editor.commit();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        queue.add(jsonObjectRequest);

    }
}
