package com.tattoo.network.commands;

import java.io.Serializable;

public abstract class Command implements Serializable {
    public enum Type { AUTH, BOOK_APPOINTMENT, MASTER, GET_MASTERS,SERVICE, REGISTER  }
    private final Type type;

    protected Command(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}