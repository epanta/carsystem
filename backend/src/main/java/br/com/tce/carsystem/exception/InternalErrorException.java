package br.com.tce.carsystem.exception;

public class InternalErrorException extends Exception {
    public InternalErrorException() {

    }

    public InternalErrorException(String message) {
        super (message);
    }

    public InternalErrorException(Throwable cause) {
        super (cause);
    }

    public InternalErrorException(String message, Throwable cause) {
        super (message, cause);
    }
}
