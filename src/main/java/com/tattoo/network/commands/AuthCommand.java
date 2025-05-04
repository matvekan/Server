package com.tattoo.network.commands;

public class AuthCommand extends Command {
    private final String login;
    private final String password;

    public AuthCommand(String login, String password) {
        super(Type.AUTH);
        this.login = login;
        this.password = password;
    }

    public String getLogin() { return login; }
    public String getPassword() { return password; }
}