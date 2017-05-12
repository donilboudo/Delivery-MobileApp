package ca.beogotechnologies.deliverymanager_mobileapp.activities.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;
import ca.beogotechnologies.deliverymanager_mobileapp.util.DeliveryConstants;

public class DeliveriesAdapter extends ArrayAdapter<Delivery> {
    private final Context context;
    private final List<Delivery> deliveries;

    public DeliveriesAdapter(Context context, List<Delivery> deliveries) {
        super(context, R.layout.activity_my_deliveries_item, deliveries);
        this.context = context;
        this.deliveries = deliveries;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_my_deliveries_item, parent, false);

        Delivery delivery = deliveries.get(position);

        TextView mSender = (TextView) rowView.findViewById(R.id.lblSender);
        String sender = mSender.getText().toString() + " " + delivery.getClient().getName();
        mSender.setText(sender);

        TextView mReceiver = (TextView) rowView.findViewById(R.id.lblReceiver);
        String receiver = mReceiver.getText().toString() + " " + delivery.getReceiverName();
        mReceiver.setText(receiver);

        ImageView mStatus = (ImageView) rowView.findViewById(R.id.imgStatus);
        if (DeliveryConstants.STARTED.equals(delivery.getStatus()))
        {
            mStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pending_delivery));
        }
        else if (DeliveryConstants.COMPLETED.equals(delivery.getStatus()))
        {
            mStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check ));
        }

        return rowView;
    }
}