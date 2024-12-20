package Seungmin.Game.config.handlers;

import Seungmin.Game.common.dto.ApiResponseDto;
import Seungmin.Game.common.exceptions.ApiUserNotLoggedInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(ApiUserNotLoggedInException.class)
    public ResponseEntity<ApiResponseDto> handleApiUserNotLoggedInException(ApiUserNotLoggedInException apiUserNotLoggedInException) {
        ApiResponseDto apiResponseDto = ApiResponseDto.builder()
                .successStatus(false)
                .errorMessage(apiUserNotLoggedInException.getMessage())
                .data(null).build();
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }
}
