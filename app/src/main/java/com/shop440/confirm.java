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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
    RequestQueue queue;
    JSONObject json, login;
    String TAG = "confirm.java";
    User user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    VolleySingleton volleySingleton;
    String compare;
    @BindView(R.id.editText)
    EditText passcode;
    @BindView(R.id.textView2)
    TextView feedback;
    @BindView(R.id.progressBar)
    ProgressBar bar;
    @BindView(R.id.button)
    Button continueButton;
    @BindView(R.id.retry)
    Button retrybut;
    private String phone;
    private String serverCode;
    private String query;

    @OnClick(R.id.textView3)
    void resend() {
        GetPasscode(phone);

    }

    @OnClick(R.id.button)
    void submit() {
        if (ComparePasscode(passcode.getText().toString(), compare)) {
            user.setPasscode(compare);
            register();

        } else {
            feedback.setVisibility(View.VISIBLE);
            feedback.setTextColor(Color.parseColor("Red"));
            feedback.setText("Incorrect Passcode!");
        }

    }

    @OnClick(R.id.retry)
    void retry() {
        register();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        phone = getIntent().getStringExtra("phone");
        user = (User) getIntent().getSerializableExtra("user");
        //phone = "";
        json = new JSONObject();
        login = new JSONObject();
        Log.d(TAG, user.getName());
        volleySingleton = VolleySingleton.getsInstance();
        queue = volleySingleton.getmRequestQueue();
        SmsReciever.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);
                Toast.makeText(confirm.this, "Loading Passcode", Toast.LENGTH_LONG).show();
                String[] newString = messageText.split(" ");
                if (ComparePasscode(newString[newString.length - 1], compare)) {
                    Log.d("Passcode", newString[newString.length - 1]);
                    user.setPasscode(compare);
                    register();
                }

            }
        });

        GetPasscode(phone);
        ButterKnife.bind(this);
        continueButton.setEnabled(false);
        passcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("text", String.valueOf(i));
                if (i >= 1) {
                    continueButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void GetPasscode(String q) {
        try {
            q = URLEncoder.encode(phone, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest ObjectRequest = new StringRequest(Request.Method.GET, Urls.INSTANCE.getBASE_URL() + Urls.INSTANCE.getPASSCODE() + "?phone=" + URLEncoder.encode(q), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    compare = new JSONObject(response).getString("Passcode");
                } catch (Exception e) {
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
        ObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(ObjectRequest);
    }

    private Boolean ComparePasscode(String fromSms, String fromServer) {
        if (fromSms.equals(fromServer)) {
            try {
                bar.setVisibility(View.VISIBLE);
                feedback.setVisibility(View.VISIBLE);
                feedback.setText("Authenticating.....");
                continueButton.setVisibility(View.GONE);
                passcode.setVisibility(View.GONE);
                json.put("Phone", user.getPhone());
                json.put("Name", user.getName());
                json.put("Image", user.getImage());
                json.put("Passcode", fromSms);
                login.put("Phone", user.getPhone());
                login.put("Passcode", fromSms);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }

    private void get_token() {
        feedback.setText("Loading token");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.INSTANCE.getBASE_URL() + Urls.INSTANCE.getUPDATE_USER(), login, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("response", response.toString());
                    editor.putString(Urls.INSTANCE.getTOKEN(), response.getString("Token"));
                    editor.putString(getResources().getString(R.string.profileImage), user.getImage());
                    editor.putString(getResources().getString(R.string.username), user.getName());
                    editor.commit();
                    Intent i = new Intent(confirm.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }

    private void register() {
        bar.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.GONE);
        feedback.setText("Creating Account!");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.INSTANCE.getBASE_URL() + Urls.INSTANCE.getNEW_USER(), json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("NewUser", "success");
                get_token();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                feedback.setText("Error uploading data Please try again");
                retrybut.setVisibility(View.VISIBLE);


            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);
    }
}
