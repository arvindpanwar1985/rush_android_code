package com.hoffmans.rush.http;

/**
 * Created by devesh on 16/12/16.
 */

public class APIError {

    private int statusCode;
    private String message;
    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
