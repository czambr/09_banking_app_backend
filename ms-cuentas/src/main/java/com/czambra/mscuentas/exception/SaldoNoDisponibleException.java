package com.czambra.mscuentas.exception;

public class SaldoNoDisponibleException extends RuntimeException {

    public SaldoNoDisponibleException(String message) {
        super(message);
    }
}
