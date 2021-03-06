package cis350.group6.fridgemanager;

/**
 * Created by David on 4/10/2015.
 */
public class User {
    private String username, firstName, lastName, emailAddress, password;
    public User() {
        username = "";
        firstName = "";
        lastName = "";
        emailAddress = "";
        password = "";
    }

    public User(String username, String firstName, String lastName, String emailAddress, String password) {
        this.username = username;
        this.firstName = firstName;
        this. lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
