package com.itgirl.account_service_a.exception;

public class UnsufficientBalanceException extends RuntimeException {
    public UnsufficientBalanceException() {
        super("Insufficient balance");
    }
}