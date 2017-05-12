package ca.beogotechnologies.deliverymanager_mobileapp.activities.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.activities.common.UserInfoActivity;
import ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_FIRST_NAME;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_LAST_NAME;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class AdminMenuActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        //say welcome to user!
        sayWelcomeToUser();

    }

    private void sayWelcomeToUser() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userFirstName = sharedpreferences.getString(CURRENT_USER_FIRST_NAME, "");
        String userLastName = sharedpreferences.getString(CURRENT_USER_LAST_NAME, "");

        TextView mWelcomeMessage = (TextView) findViewById(R.id.txtUserWelcome);

        String welcomeMessage = mWelcomeMessage.getText().toString();
        welcomeMessage = welcomeMessage + " " + userFirstName + " " + userLastName;
        mWelcomeMessage.setText(welcomeMessage);

    }

    public void newDelivery(View view) {
        if (isOnline(this))
        {
            Intent intent = new Intent(getApplicationContext(), NewDeliveryActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion à internet", Toast.LENGTH_LONG).show();
        }
    }

    public void myInfo(View view) {
        if (isOnline(this))
        {
            Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion à internet", Toast.LENGTH_LONG).show();
        }
    }

    public void logout(View view) {
        LoginUtil.logout(this);
    }

    public void openedDeliveries(View view) {
        if (isOnline(this))
        {
            Intent intent = new Intent(getApplicationContext(), OpenedDeliveriesActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion à internet", Toast.LENGTH_LONG).show();
        }
    }

    public void deliveryRequests(View view) {
        if (isOnline(this))
        {
            Intent intent = new Intent(getApplicationContext(), DeliveryRequestsActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion à internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            LoginUtil.logout(getApplicationContext());
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
