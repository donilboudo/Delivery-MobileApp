package ca.beogotechnologies.deliverymanager_mobileapp.activities.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.activities.employee.EmployeeMenuActivity;
import ca.beogotechnologies.deliverymanager_mobileapp.activities.admin.AdminMenuActivity;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.LoginTask;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.User;
import ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil;
import ca.beogotechnologies.deliverymanager_mobileapp.util.PasswordHelper;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_EMAIL;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_FIRST_NAME;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_LAST_NAME;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_PASSWORD;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_PHONE;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_ROLE;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_USER_NAME;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.USER_SESSION_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class LoginActivity extends AppCompatActivity implements LoginTask.AsyncResponse {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    public static final String CONNEXION_REFUSED = "CONNEXION_REFUSED";

    @InjectView(R.id.input_userName)
    EditText mUserName;

    @InjectView(R.id.input_password)
    EditText mPassword;

    @InjectView(R.id.btn_login)
    Button mLoginButton;

    @InjectView(R.id.lbl_badConnexionInfo)
    TextView mBadInfo;

    private String userName;
    private String password;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

    }

    public void onLoginToApp(View view) {
        if (isOnline(this))
        {
            if (validate())
            {
                loginToApp();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    public void loginToApp() {
        Log.d(TAG, "Login");

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.DeliveryManager);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        if (isOnline(this))
        {
            //we need to hash the password in md5
            password = PasswordHelper.md5(password);

            LoginTask loginTask = new LoginTask(this);
            loginTask.execute(userName, password);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String result) {
        try
        {
            JSONObject jsonObject = new JSONObject(result);

            String userSessionId = jsonObject.getString("userSessionId");
            if (!userSessionId.isEmpty())
            {
                //save user session in shared preferences
                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString(USER_SESSION_ID, userSessionId);

                //save user in shared preferences
                User user = new Gson().fromJson(jsonObject.get("user").toString(), User.class);
                editor.putString(CURRENT_USER_ID, user.getId());
                editor.putString(CURRENT_USER_FIRST_NAME, user.getFirstName());
                editor.putString(CURRENT_USER_LAST_NAME, user.getLastName());
                editor.putString(CURRENT_USER_PHONE, user.getPhone());
                editor.putString(CURRENT_USER_EMAIL, user.getEmail());
                editor.putString(CURRENT_USER_USER_NAME, user.getUserName());
                editor.putString(CURRENT_USER_PASSWORD, user.getPassword());
                editor.putString(CURRENT_USER_ROLE, user.getRole());

                editor.apply();

                if (user.getRole() != null)
                {
                    if (LoginUtil.USER_ROLE_EMPLOYEE.equals(user.getRole()))
                    {
                        Intent intent = new Intent(getApplicationContext(), EmployeeMenuActivity.class);
                        startActivityForResult(intent, REQUEST_SIGNUP);
                    }
                    else if (LoginUtil.USER_ROLE_ADMIN.equals(user.getRole()))
                    {
                        Intent intent = new Intent(getApplicationContext(), AdminMenuActivity.class);
                        startActivityForResult(intent, REQUEST_SIGNUP);
                    }
                }
            }
            else
            {
                onLoginFailed();
            }
        }
        catch (JSONException e)
        {
            //TODO
        }

    }

    public void onLoginFailed() {
        mBadInfo.setVisibility(View.VISIBLE);
    }

    private boolean validate() {
        boolean isValid = false;
        userName = mUserName.getText().toString();
        password = mPassword.getText().toString();

        if (userName.isEmpty())
        {
            mUserName.setError("Champs vide");
            isValid = false;
        }
        if (password.isEmpty())
        {
            mPassword.setError("Champs vide");
            isValid = false;
        }

        if (!userName.isEmpty() && !password.isEmpty())
        {
            isValid = true;
        }

        return isValid;
    }


    @Override
    public void processFinish(String result) {
        progressDialog.dismiss();

        if (result != null && !result.isEmpty() && !result.equals(CONNEXION_REFUSED))
        {
            onLoginSuccess(result);
        }
        else
        {
            onLoginFailed();
        }
    }
}