package ca.beogotechnologies.deliverymanager_mobileapp.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;

public class DeliveryDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delivery_details);

        Intent intent = getIntent();
        Delivery delivery = (Delivery) intent.getSerializableExtra("selectedDelivery");

        initView(delivery);
    }

    private void initView(Delivery delivery) {
        EditText mSendDate = (EditText) findViewById(R.id.txtSendDate);

        EditText mSenderName = (EditText) findViewById(R.id.txtSenderName);
        TextView mSenderNameLabel = (TextView) findViewById(R.id.lblSenderName);

        EditText mSenderNumber = (EditText) findViewById(R.id.txtSenderNumber);
        EditText mSenderAddress = (EditText) findViewById(R.id.txtSenderAddress);
        EditText mReceiverName = (EditText) findViewById(R.id.txtReceiver);
        EditText mReceiverAddress = (EditText) findViewById(R.id.txtReceiverAddress);
        EditText mDeliverName = (EditText) findViewById(R.id.txtDeliverName);

        if ("COMPANY".equals(delivery.getClient().getClientType()))
        {
            mSenderNameLabel.setText(R.string.lblClient);
        }

        mSendDate.setText(delivery.getSendDate().toString());
        mSenderName.setText(delivery.getClient().getName());
        mSenderNumber.setText(delivery.getClient().getPhone());
        mSenderAddress.setText(delivery.getClient().getAddress());
        mReceiverName.setText(delivery.getReceiverName());
        mReceiverAddress.setText(delivery.getReceiverAddress());
        mDeliverName.setText(delivery.getUser().getFullName());
    }

    public void cancel(View view) {
        Intent intent = new Intent(getApplicationContext(), OpenedDeliveriesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), OpenedDeliveriesActivity.class);
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
