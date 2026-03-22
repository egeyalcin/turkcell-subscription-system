package com.turkcell.subscriptionservice.domain.exception;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;


public class LocalizedException extends RuntimeException {
    private final String code;
    private final Object[] params;

    public LocalizedException(String code, Object... params) {
        super();
        this.code = code;
        this.params = params;
    }

    @Override
    public String getMessage() {
        String message = "Error code: " + code;
        if (params != null && params.length > 0) {
            message += " Parameters: " + Arrays.toString(params);
        }
        return message;
    }

    public String getMessage(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("exception", locale);
        String message = bundle.getString(code);
        if (params != null && params.length > 0) {
            message = MessageFormat.format(message, params);
        }
        return message;
    }

    public String getCode() {
        return code;
    }

    public Object[] getParams() {
        return params;
    }
}