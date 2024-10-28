package com.sondeos.javanotifychallenge;

import com.sondeos.javanotifychallenge.exceptions.MaxRetriesExceededException;
import com.sondeos.javanotifychallenge.utils.RetryHandler;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RetryHandlerTests {

    @Test
    void executeWithRetrySuccessfulAfterOneFailure() throws MaxRetriesExceededException {
        AtomicInteger attempts = new AtomicInteger(0);

        // Simula una operación que falla la primera vez pero tiene éxito en el segundo intento
        String result = RetryHandler.executeWithRetry("1", () -> {
            if (attempts.incrementAndGet() < 2) {
                throw new RuntimeException("Failure");
            }
            return "Success";
        }, 3, 1000);

        assertEquals("Success", result);
        assertEquals(2, attempts.get());  // Verifica que hubo un reintento
    }

    @Test
    void executeWithRetryMaxRetriesExceeded() {
        assertThrows(MaxRetriesExceededException.class, () ->
                RetryHandler.executeWithRetry("1", () -> {
                    throw new RuntimeException("Always fails");
                }, 3, 1000)
        );
    }

}
