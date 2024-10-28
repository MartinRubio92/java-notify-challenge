package com.sondeos.javanotifychallenge.utils;

import com.sondeos.javanotifychallenge.exceptions.MaxRetriesExceededException;
import java.util.function.Supplier;

public class RetryHandler {

    public static <T> T executeWithRetry(Supplier<T> action, int maxRetries, long initialDelay) throws Exception {
        int attempt = 0;
        long retryDelay = initialDelay;

        while (attempt < maxRetries) {
            try {
                return action.get();
            } catch (Exception e) {
                attempt++;

                if (attempt >= maxRetries) {
                    throw new MaxRetriesExceededException("Max retries reached " + e.getMessage());
                }
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedException("Retry interrupted " + interruptedException.getMessage());
                }
                retryDelay *= 2; // Aumento exponencial del tiempo de espera
            }
        }
        throw new MaxRetriesExceededException("Failed to execute action after retries");
    }
}
