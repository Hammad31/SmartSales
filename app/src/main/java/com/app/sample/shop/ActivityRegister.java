package com.app.sample.shop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.shop.JSON.JSONParser;
import com.app.sample.shop.data.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityRegister extends AppCompatActivity {
    JSONParser jParser = new JSONParser();
    JSONObject json;
    private static String url_login = "http://hamoha.com/Project/Registration";
    private EditText inputEmail, inputPassword, inputPhone, inputName;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword, inputLayoutPhone, inputLayoutName;
    private Button btnRegister;
    private TextView errorMsg;
    private SessionManager sessionManager;
    String email;
    String password;
    String name;
    String phoneNumber;
    boolean checkTransfer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        errorMsg = (TextView) findViewById(R.id.errorMassage);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);

        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputPassword.setTransformationMethod(new PasswordTransformationMethod());
        inputPhone = (EditText) findViewById(R.id.input_phone);
        inputName = (EditText) findViewById(R.id.input_name);

        btnRegister = (Button) findViewById(R.id.register);

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        sessionManager = new SessionManager(getApplicationContext());
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        name = inputName.getText().toString().trim();
        phoneNumber = inputPhone.getText().toString().trim();

        new Registration().execute();
    }

    private void RegistrationDONE(String CID) {
        sessionManager.logoutUser();
        sessionManager.createLoginSession(name, email, CID);
        Toast.makeText(getApplicationContext(), "Registration DONE...", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), ActivityMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }



    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError("Please Enter A Valid Email");
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Please Enter A Valid Password");
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_phone:
//                    validatePassword();
                    break;
                case R.id.input_name:
//                    validatePassword();
                    break;
            }
        }
    }

    /////////////////////////////
    private class Registration extends AsyncTask<Void, Void, Void> {
        String CID;

        @Override
        protected Void doInBackground(Void... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("u", email));
            params.add(new BasicNameValuePair("p", password));
            params.add(new BasicNameValuePair("phone", phoneNumber));
            params.add(new BasicNameValuePair("name", name));
            json = jParser.makeHttpRequest(url_login, "GET", params);
            String s = null;

            try {
                s = json.getString("info");
                Log.d("Msg", json.getString("info"));
                if (s.equals("success")) {
                    checkTransfer = true;
                    CID = json.getString("CustomerID");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (checkTransfer) {
                checkTransfer = false;
                errorMsg.setText("");
                RegistrationDONE( CID);
            } else{
                errorMsg.setText("Wrong Data !!!");
                inputEmail.setText("");
                inputPassword.setText("");
                inputPhone.setText("");
                inputName.setText("");
            }

        }

    }
    //////////////////////////////////////////////
}
