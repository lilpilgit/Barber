package model.exception;

public class NoCustomerCreatedException extends Exception {
    public NoCustomerCreatedException(String errorMessage) {
        super(errorMessage);
    }

    public NoCustomerCreatedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
