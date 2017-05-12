package ca.beogotechnologies.deliverymanager_mobileapp.activities.employee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;

public class MyDeliveryHistoryDetailsActivity extends AppCompatActivity {
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
        EditText mReceiverReferences = (EditText) findViewById(R.id.txtReceiverReferences);

        if (delivery != null)
        {
            mSendDate.setText(delivery.getSendDate().toString());
            mReceiveDate.setText(delivery.getReceiveDate().toString());
            mClient.setText(delivery.getClient().getName());
            mReceiver.setText(delivery.getReceiverName());
            mReceiverAddress.setText(delivery.getReceiverAddress());
            mReceiverReferences.setText(delivery.getReceiverReferences());
        }
    }

    public void onCancel(View view) {
        Intent intent = new Intent(getApplicationContext(), MyDeliveryiesHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MyDeliveryiesHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            onBackPressed();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
