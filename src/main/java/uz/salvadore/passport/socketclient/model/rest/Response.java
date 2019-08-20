package uz.salvadore.passport.socketclient.model.rest;

import lombok.Data;

@Data
public class Response<T> {

    private int code;
    private String message;
    private T response;

}
