package com.tele2.subscriptions.util;

import org.apache.commons.lang.exception.ExceptionUtils;

public class ThrowableDecorator {

    private final Throwable t;

    public ThrowableDecorator(Throwable t) {
        this.t = t;
    }

    public Boolean wasCausedBy(Class clazz) {
        Throwable rootCause = ExceptionUtils.getRootCause(t);
        return rootCause != null && rootCause.getClass().equals(clazz);
    }

}
