package ca.beogotechnologies.deliverymanager_mobileapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;

public class CompletedDeliveryDetailsActivity extends AppCompatActivity {
    private Delivery delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_delivery_details);

        //get selected delivery in extra data
        Intent intent = getIntent();
        delivery = (Delivery) intent.getSerializableExtra("selectedDelivery");

        initViews();
    }

    private void initViews() {
        EditText mSendDate = (EditText) findViewById(R.id.txtSendDate);
        EditText mReceiveDate = (EditText) findViewById(R.id.txtReceiveDate);
        EditText mClient = (EditText) findViewById(R.id.txtSender);
        EditText mReceiver = (EditText) findViewById(R.id.txtReceiver);
        EditText mReceiverAddress = (EditText) findViewById(R.id.txtReceiverAddress);

        if (delivery != null)
        {
            mSendDate.setText(delivery.getSendDate().toString());
            mReceiveDate.setText(delivery.getReceiveDate().toString());
            mClient.setText(delivery.getClient().getName());
            mReceiver.setText(delivery.getReceiver());
            mReceiverAddress.setText(delivery.getReceiverAddress());
        }
    }

    public void onCancel(View view){
        Intent intent = new Intent(getApplicationContext(), MyCompletedDeliveriesActivity.class);
        startActivity(intent);
    }
}
