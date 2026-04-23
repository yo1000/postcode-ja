package com.yo1000.postcode.domain.vo;

public record WaitTime(long millis) {
    public void sleep() {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
