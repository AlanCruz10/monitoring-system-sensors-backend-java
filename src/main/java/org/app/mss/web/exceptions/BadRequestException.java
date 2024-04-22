package org.app.mss.web.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(){super("Incorrect password");}

    public BadRequestException(String email){super("User " + email + " already exists");}

}