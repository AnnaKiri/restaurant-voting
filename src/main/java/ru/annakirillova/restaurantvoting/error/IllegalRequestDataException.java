package ru.annakirillova.restaurantvoting.error;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}