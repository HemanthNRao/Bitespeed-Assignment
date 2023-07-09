package com.bitespeed.backendtask.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Utils {
    public static void handleNotFound(String message) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }
}
