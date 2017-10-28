package com.shop440;

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

import com.shop440.Models.SmsListener;
import com.shop440.Models.User;
import com.shop440.Receiver.SmsReciever;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Confirm extends AppCompatActivity {
    JSONObject json, login;
    String TAG = "Confirm.java";
    User user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
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
        SmsReciever.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);
                Toast.makeText(Confirm.this, "Loading Passcode", Toast.LENGTH_LONG).show();
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

    }

    private void register() {

    }
}
