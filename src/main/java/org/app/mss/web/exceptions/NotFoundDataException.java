package org.app.mss.web.exceptions;

import java.time.LocalDate;

public class NotFoundDataException extends RuntimeException {

    public NotFoundDataException(){super("Data sensor not found");}

    public NotFoundDataException(LocalDate date){super("Data sensor not found by date " + date);}

}