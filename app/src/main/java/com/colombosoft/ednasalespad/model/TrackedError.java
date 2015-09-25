package com.colombosoft.ednasalespad.model;

/**
 * Created by thahzan on 1/16/15.
 */
public class TrackedError {

    private long time;
    private String function, stackTrace, response;

    public TrackedError() {}

    public TrackedError(long time, String function, String stackTrace, String response) {
        this.time = time;
        this.function = function;
        this.stackTrace = stackTrace;
        this.response = response;
    }

    public TrackedError(long time, String function, String stackTrace) {
        this.time = time;
        this.function = function;
        this.stackTrace = stackTrace;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
