package com.colombosoft.ednasalespad;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.colombosoft.ednasalespad.helpers.DatabaseHandler;
import com.colombosoft.ednasalespad.helpers.NetworkFunctions;
import com.colombosoft.ednasalespad.helpers.SharedPref;
import com.colombosoft.ednasalespad.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UserProfileActivity extends ActionBarActivity {

    private static final String LOG_TAG = UserProfileActivity.class.getSimpleName();

    User user;
    EditText et_userName, et_current_password, et_new_password, et_new_password_confirm;
    TextView message_text;
    Button btn_update;
    NetworkFunctions networkFunctions;
    private DatabaseHandler dbHandler;
    private SharedPref pref;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        dbHandler = DatabaseHandler.getDbHandler(UserProfileActivity.this);
        pref = SharedPref.getInstance(UserProfileActivity.this);
        networkFunctions = new NetworkFunctions();

        user = pref.getLoginUser();

        et_userName = (EditText) findViewById(R.id.et_name);
        et_current_password = (EditText) findViewById(R.id.et_current_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_password_confirm = (EditText) findViewById(R.id.et_new_password_confirm);
        btn_update = (Button) findViewById(R.id.update);

        et_userName.setText(user.getName());

        dialog = new Dialog(UserProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notify);
        dialog.setCanceledOnTouchOutside(false);
        message_text = (TextView) dialog.findViewById(R.id.notify_message_text);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_userName.getText().toString();
                String current_password = et_current_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                String new_password_confirm = et_new_password_confirm.getText().toString();

                if (!userName.equals(user.getName())) {
                    change_name(userName);
                    if(new_password.isEmpty()){
                        message_text.setText("Name changed successfully.");
                        Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
                        btn_OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
                                finish();
                            }
                        });
                        dialog.show();
                    }
                }

                if (!new_password.isEmpty()) {
                    if (new_password.equals(new_password_confirm)) {
                        new Authenticate().execute(user.getUsername(), current_password, new_password);
                    } else {
                        message_text.setText("New passwords do not match.");
                        Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
                        btn_OK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });

    }

    private class Authenticate extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                String loginResponse = networkFunctions.authenticate(params[0], params[1]);
                Log.i(LOG_TAG,loginResponse);
                JSONObject loginJSON = new JSONObject(loginResponse);
                if (loginJSON.getBoolean("result")) {
                    return params[2];
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String password) {
            super.onPostExecute(password);
            if(password!=null) {
                change_password(password);

                message_text.setText("Password changed successfully");
                Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
                btn_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
                        finish();
                    }
                });
                dialog.show();
            }else{
                message_text.setText("Could not authenticate you. Check the current password.");
                Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
                btn_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserProfileActivity.this, DashboardActivity.class));
        finish();
//        super.onBackPressed();
    }

    private void change_name(String name) {
        user.setName(name);
        pref.storeLoginUser(user);
        dbHandler.storeUser(user);
        //TODO:update server
    }

    private void change_password(String password) {
        //TODO:update server
        Log.i(LOG_TAG, "Changing password");

    }




}
