package ca.beogotechnologies.deliverymanager_mobileapp.activities.employee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.StartDeliveryTask;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.USER_SESSION_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class StartDeliveryActivity extends AppCompatActivity implements StartDeliveryTask.AsyncResponse {
    private Delivery delivery = null;

    private EditText mSenderComments;
    private EditText mSenderReferences;
    private EditText mReceiverName;
    private EditText mReceiverAddress;
    private EditText mReceiverNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_delivery);

        //get selected delivery in extra data
        Intent intent = getIntent();
        delivery = (Delivery) intent.getSerializableExtra("selectedDelivery");

        initViews();
    }

    private void initViews() {
        EditText mSenderName = (EditText) findViewById(R.id.txtSenderName);
        EditText mSenderNumber = (EditText) findViewById(R.id.txtSenderNumber);
        EditText mSenderAddress = (EditText) findViewById(R.id.txtSenderAddress);
        mReceiverName = (EditText) findViewById(R.id.txtReceiver);
        mReceiverAddress = (EditText) findViewById(R.id.txtReceiverAddress);
        mSenderComments = (EditText) findViewById(R.id.txtSenderComments);
        EditText mSendDate = (EditText) findViewById(R.id.txtSendDate);
        mSenderReferences = (EditText) findViewById(R.id.txtSenderReferences);
        mReceiverNumber = (EditText) findViewById(R.id.txtReceiverNumber);

        if (delivery != null)
        {
            mSenderName.setText(delivery.getClient().getName());
            mSenderNumber.setText(delivery.getClient().getPhone());
            mSenderAddress.setText(delivery.getClient().getAddress());
            mReceiverName.setText(delivery.getReceiverName());
            mReceiverAddress.setText(delivery.getReceiverAddress());
            mReceiverNumber.setText(delivery.getReceiverNumber());

            if (delivery.getSenderReferences() != null)
            {
                mSenderReferences.setText(delivery.getSenderReferences());
            }
            if (delivery.getSenderComments() != null)
            {
                mSenderComments.setText(delivery.getSenderComments());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.CANADA_FRENCH);
            mSendDate.setText(sdf.format(new Date()));

        }
    }

    public void startDelivery(View view) {
        if (isOnline(this))
        {
            if (isValidData())
            {
                Date sendDate = new Date();
                String senderComments = mSenderComments.getText().toString();
                String senderReferences = mSenderReferences.getText().toString();
                String receiverName = mReceiverName.getText().toString();
                String receiverAddress = mReceiverAddress.getText().toString();
                String receiverNumber = mReceiverNumber.getText().toString();

                SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");

                progressDialog = new ProgressDialog(this, R.style.DeliveryManager);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Un moment...");
                progressDialog.show();

                StartDeliveryTask task = new StartDeliveryTask(this);
                task.execute(userSessionId,
                        delivery.getId(),
                        sendDate.toString(),
                        senderComments,
                        senderReferences,
                        receiverName,
                        receiverAddress,
                        receiverNumber);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidData() {
        return true;//TODO
    }

    public void cancel(View view) {
        Intent intent = new Intent(getApplicationContext(), MyOpenedDeliveriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String output) {
        progressDialog.dismiss();

        Toast.makeText(getApplicationContext(), "Livraison démarrée", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MyOpenedDeliveriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MyOpenedDeliveriesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            onBackPressed();
            //kill activity
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
