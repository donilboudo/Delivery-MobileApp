package ca.beogotechnologies.deliverymanager_mobileapp.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fabrice on 2017-03-22.
 */

public class DeliveryRequest implements Serializable{
    private String id;
    private Date requestDate;
    private String senderName;
    private String senderNumber;
    private String senderAddress;
    private String receiverName;
    private String receiverNumber;
    private String receiverAddress;
    private String senderComments;
    private String status;
    private String packageType;
    private String deliverId;
    private String deliverName;
    private String deliverNumber;

    public DeliveryRequest() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
    }

    public String getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(String deliverId) {
        this.deliverId = deliverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getSenderComments() {
        return senderComments;
    }

    public void setSenderComments(String senderComments) {
        this.senderComments = senderComments;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String type) {
        this.packageType = type;
    }

    public String getDeliverNumber() {
        return deliverNumber;
    }

    public void setDeliverNumber(String deliverNumber) {
        this.deliverNumber = deliverNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }
}
