package com.sondeos.javanotifychallenge.providers;

import com.sondeos.javanotifychallenge.providers.dto.NotifyResultDto;

public interface NotificationProvider {
    NotifyResultDto notify(String destination, String message);
}
