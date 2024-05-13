package Seungmin.Game.common.exceptions;

public class CustomException extends RuntimeException{

    public CustomException(CustomExceptionCode code) {
        super(code.getErrorMessage());
        String message = code.getErrorMessage();
    }

}
