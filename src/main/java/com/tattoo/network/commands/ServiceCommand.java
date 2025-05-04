package com.tattoo.network.commands;

public class ServiceCommand extends Command {
    public enum Action { GET_ALL, GET_BY_ID, GET_BY_MASTER }
    private Action action;
    private Long id;
    private Long masterId;

    public ServiceCommand(Action action) {
        super(Type.SERVICE);
        this.action = action;
    }

    // Getters and Setters
    public Action getAction() { return action; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMasterId() { return masterId; }
    public void setMasterId(Long masterId) { this.masterId = masterId; }
}