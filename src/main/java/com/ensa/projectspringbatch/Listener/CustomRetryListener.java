package com.ensa.projectspringbatch.Listener;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class CustomRetryListener implements RetryListener {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        // This is called before the first attempt.
        System.out.println("Starting retry operation...");
        return true; // Return false to prevent retries.
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        // This is called after the final attempt.
        if (throwable == null) {
            System.out.println("Retry operation completed successfully.");
        } else {
            System.out.println("Retry operation failed after " + context.getRetryCount() + " attempts.");
        }
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        // This is called on each failed attempt.
        System.out.println("Retrying... Attempt: " + context.getRetryCount() + ", Error: " + throwable.getMessage());
    }
}

