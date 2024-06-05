package Seungmin.Game.common.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionCode {
    PostNotFoundException("게시글을 찾을 수 없습니다."),
    SavePostFailedException("게시글 저장 실패, 다시 시도해 주세요"),
    UpdatePostFailedException("게시글 수정 실패, 다시 시도해 주세요"),
    DeletePostFailedException("게시글 삭제 실패, 다시 시도해 주세요"),
    CommentNotFoundException("댓글을 찾을 수 없습니다.");

    private final String errorMessage;
}
