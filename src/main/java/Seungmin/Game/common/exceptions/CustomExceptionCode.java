package Seungmin.Game.common.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionCode {
    PostNotFoundException("게시글을 찾을 수 없습니다.");

    private final String errorMessage;
}
