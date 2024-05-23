package Seungmin.Game.common.exceptions;

public class ApiUserNotLoggedInException extends RuntimeException{

    public ApiUserNotLoggedInException(String message) {
        super(message);
    }
}
