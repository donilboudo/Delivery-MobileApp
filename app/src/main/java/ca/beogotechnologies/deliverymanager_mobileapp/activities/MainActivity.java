package ca.beogotechnologies.deliverymanager_mobileapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ca.beogotechnologies.deliverymanager_mobileapp.R.layout.activity_main);

    }

    public void newDelivery(View view) {
        if (isOnline(this))
        {
            Intent intent = new Intent(getApplicationContext(), NewDeliveryActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    public void myOpenedDeliveries(View view) {
        if (isOnline(this))
        {
            Intent intent = new Intent(getApplicationContext(), MyOpenedDeliveriesActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    public void myDeliveriesHistory(View view) {
        if (isOnline(this))
        {
            Intent intent = new Intent(getApplicationContext(), MyCompletedDeliveriesActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    public void logout(View view) {
        LoginUtil.logout(this);
    }
}
