package com.tattoo.network.commands;

import java.io.Serializable;

public class RegisterCommand extends Command implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String login;
    private final String password;
    private final String email;
    private final String fullName;
    private final String phone;

    public RegisterCommand(String login, String password, String email, String fullName, String phone) {
        super(Type.REGISTER);
        this.login = login;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
    }

    // Геттеры
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
}