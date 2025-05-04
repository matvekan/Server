package com.tattoo.network.commands;

public class MasterCommand extends Command {
    public enum Action { GET_ALL, GET_BY_ID, GET_AVAILABLE_TIME }
    private Action action;
    private Long masterId;
    private String date;

    public MasterCommand(Action action) {
        super(Type.MASTER);
        this.action = action;
    }

    // Getters and Setters
    public Action getAction() { return action; }
    public Long getMasterId() { return masterId; }
    public void setMasterId(Long masterId) { this.masterId = masterId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}