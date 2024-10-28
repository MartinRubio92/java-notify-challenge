package com.sondeos.javanotifychallenge.providers.dto;

public class NotifyPayloadDto {

    String destination;
    String message;

    public NotifyPayloadDto(String destination, String message) {
        this.destination = destination;
        this.message = message;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "NotifyPayload: {" +
                "destination='" + destination + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
