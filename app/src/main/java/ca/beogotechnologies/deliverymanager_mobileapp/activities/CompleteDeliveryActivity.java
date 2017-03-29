package ca.beogotechnologies.deliverymanager_mobileapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.CompleteDeliveryTask;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.USER_SESSION_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class CompleteDeliveryActivity extends AppCompatActivity implements CompleteDeliveryTask.AsyncResponse {
    private Delivery delivery = null;

    private EditText mReceiverComments;
    private EditText mReceiveDate;
    private EditText mReceiverReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_delivery);

        //get selected delivery in extra data
        Intent intent = getIntent();
        delivery = (Delivery) intent.getSerializableExtra("selectedDelivery");

        initViews();
    }

    private void initViews() {
        EditText mSender = (EditText) findViewById(R.id.txtSender);
        EditText mReceiver = (EditText) findViewById(R.id.txtReceiver);
        EditText mReceiverAddress = (EditText) findViewById(R.id.txtReceiverAddress);
        mReceiverComments = (EditText) findViewById(R.id.txtReceiverComments);
        mReceiveDate = (EditText) findViewById(R.id.txReceiveDate);
        mReceiverReferences = (EditText) findViewById(R.id.txtReceiverReferences);

        if (delivery != null)
        {
            mSender.setText(delivery.getClient().getName());
            mReceiver.setText(delivery.getReceiver());
            mReceiverAddress.setText(delivery.getReceiverAddress());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            mReceiveDate.setText(sdf.format(new Date()));

        }
    }

    public void completeDelivery(View view) {
        if (isOnline(this))
        {
            String receiverComments = mReceiverComments.getText().toString();
            String receiverReferences = mReceiverReferences.getText().toString();
            String receiveDate = mReceiveDate.getText().toString();

            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");

            CompleteDeliveryTask task = new CompleteDeliveryTask(this);
            task.execute(userSessionId, delivery.getId(), receiveDate, receiverReferences, receiverComments);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }

    }

    public void cancel(View view) {
        Intent intent = new Intent(getApplicationContext(), MyOpenedDeliveriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(getApplicationContext(), "Livraison terminee", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MyOpenedDeliveriesActivity.class);
        startActivity(intent);
    }
}
