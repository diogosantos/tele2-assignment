package com.tele2.subscriptions.util;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class ThrowableDecoratorTest {

    private RuntimeException re = new RuntimeException(new IllegalArgumentException(new OutOfMemoryError()));

    @Test
    public void testTrueWasCausedByWithTree() {
        ThrowableDecorator d = new ThrowableDecorator(re);

        assertTrue(d.wasCausedBy(OutOfMemoryError.class));
    }

    @Test
    public void testFalseWasCausedByWithTree() {
        ThrowableDecorator d = new ThrowableDecorator(re);

        assertFalse(d.wasCausedBy(FileNotFoundException.class));
    }

    @Test
    public void testFalseWasCausedByWithNode() {
        ThrowableDecorator d = new ThrowableDecorator(new RuntimeException());

        assertFalse(d.wasCausedBy(FileNotFoundException.class));
    }

    @Test
    public void testWasCausedByRecursiveReference() {
        RuntimeException re = new RuntimeException();
        IllegalArgumentException i = new IllegalArgumentException(re);
        re.initCause(i);

        ThrowableDecorator d = new ThrowableDecorator(re);

        assertFalse(d.wasCausedBy(OutOfMemoryError.class));
    }

}