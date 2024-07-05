package Seungmin.Game.common.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionCode {

    PostNotFoundException("게시글을 찾을 수 없습니다."),
    PostSaveFailedException("게시글 저장 실패, 다시 시도해 주세요"),
    PostUpdateFailedException("게시글 수정 실패, 다시 시도해 주세요"),
    PostDeleteFailedException("게시글 삭제 실패, 다시 시도해 주세요"),

    CommentNotFoundException("댓글을 찾을 수 없습니다."),

    FileCreateFailedException("파일 저장 중 오류가 발생했습니다."),
    FileUpdateFailedException("파일 업데이트 중 오류가 발생했습니다."),
    FileDeleteFailedException("파일 삭제 중 오류가 발생했습니다."),
    FileNotFoundException("파일 검색 중 오류가 발생"),
    ;

    private final String errorMessage;
}
