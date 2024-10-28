package com.sondeos.javanotifychallenge.utils;

import com.sondeos.javanotifychallenge.exceptions.MaxRetriesExceededException;
import java.util.function.Supplier;

public class RetryHandler {

    public static <T> T executeWithRetry(String contactId, Supplier<T> action, int maxRetries, long initialDelay) throws MaxRetriesExceededException {
        int attempt = 0;
        long retryDelay = initialDelay;

        while (attempt < maxRetries) {
            try {
                return action.get();
            } catch (Exception e) {
                attempt++;

                if (attempt >= maxRetries) {
                    throw new MaxRetriesExceededException(contactId);
                }
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new MaxRetriesExceededException(contactId);
                }
                retryDelay *= 2; // Aumento exponencial del tiempo de espera
            }
        }
        throw new MaxRetriesExceededException(contactId);
    }
}
