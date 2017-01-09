package com.shop440;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shop440.Utils.Urls;
import com.shop440.Utils.VolleySingleton;

import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    JSONObject login;
    private TextView createAcct;
    private View mLoginFormView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    VolleySingleton volleySingleton;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        // Set up the login form.
        login = new JSONObject();
        volleySingleton = VolleySingleton.getsInstance();
        queue = volleySingleton.getmRequestQueue();
        mEmailView = (EditText) findViewById(R.id.phone);
        createAcct = (TextView) findViewById(R.id.createaccount);
        createAcct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUp.class);
                startActivity(i);
                finish();
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shop440), MODE_PRIVATE);
        editor = sharedPreferences.edit();


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    login.put("Phone", mEmailView.getText().toString());
                    login.put("Passcode", mPasswordView.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.BASE_URL + Urls.GET_TOKEN, login ,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.d("response", response.getString("Token"));
                            editor.putString("token", response.getString("Token"));
                            editor.apply();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
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
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonObjectRequest);
            }
        });

       // mLoginFormView = findViewById(R.id.login_form);

    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });


        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
           // mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

