package com.tattoo.network.commands;

import java.io.Serializable;

public class AuthCommand extends Command implements Serializable {
    private static final long serialVersionUID = 1L; // Должен совпадать на клиенте и сервере!
    private final String login;
    private final String password;

    public AuthCommand(String login, String password) {
        super(Type.AUTH);
        this.login = login;
        this.password = password;
    }

    // Геттеры
    public String getLogin() { return login; }
    public String getPassword() { return password; }
}