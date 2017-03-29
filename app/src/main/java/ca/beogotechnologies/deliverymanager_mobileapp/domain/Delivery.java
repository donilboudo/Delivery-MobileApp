package ca.beogotechnologies.deliverymanager_mobileapp.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fabrice on 2017-03-22.
 */

public class Delivery implements Serializable{
    private String id;
    private User user;
    private Client client;
    private Date sendDate;
    private Date receiveDate;
    private String senderComments;
    private String receiver;
    private String receiverComments;
    private String senderReferences;
    private String receiverReferences;
    private String status;
    private String receiverAddress;

    public Delivery() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getSenderComments() {
        return senderComments;
    }

    public void setSenderComments(String senderComments) {
        this.senderComments = senderComments;
    }

    public String getReceiverComments() {
        return receiverComments;
    }

    public void setReceiverComments(String receiverComments) {
        this.receiverComments = receiverComments;
    }

    public String getSenderReferences() {
        return senderReferences;
    }

    public void setSenderReferences(String senderReferences) {
        this.senderReferences = senderReferences;
    }

    public String getReceiverReferences() {
        return receiverReferences;
    }

    public void setReceiverReferences(String receiverReferences) {
        this.receiverReferences = receiverReferences;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
