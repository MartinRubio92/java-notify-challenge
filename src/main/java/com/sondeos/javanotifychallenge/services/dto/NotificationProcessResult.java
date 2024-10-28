package com.sondeos.javanotifychallenge.services.dto;

public class NotificationProcessResult {

    int processed;
    int sent;
    long duration;

    public NotificationProcessResult(int processed, int sent, long duration) {
        this.processed = processed;
        this.sent = sent;
        this.duration = duration;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String toString(){
        return "NotificationProcessResult: {" +
                "processed='" + processed + '\'' +
                ", sent='" + sent + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
