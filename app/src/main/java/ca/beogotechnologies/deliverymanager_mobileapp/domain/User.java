package ca.beogotechnologies.deliverymanager_mobileapp.domain;

import java.io.Serializable;

import ca.beogotechnologies.deliverymanager_mobileapp.util.PasswordHelper;
import ca.beogotechnologies.deliverymanager_mobileapp.util.RandomGUIDGenerator;

/**
 * Created by fabrice on 2017-03-18.
 */

public class User implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String userName;
    private String password;
    private String role;

    public User() {

    }

    public User(String firstName, String lastName, String phone, String email, String userName, String password, String role) {
        this.id = RandomGUIDGenerator.generateRandomGUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.userName = userName;
        this.password = PasswordHelper.md5(password);
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
