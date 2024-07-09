package Seungmin.Game.domain.comment;

import Seungmin.Game.common.dto.ApiResponseDto;
import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.domain.comment.commentDto.CommentRequest;
import Seungmin.Game.domain.comment.commentDto.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;


    // 댓글 저장
    @PostMapping("/comment/{postId}")
    public ApiResponseDto saveComment(@RequestBody final CommentRequest commentRequest, @PathVariable final Long postId) {
        try{
            commentRequest.setPostId(postId);
            commentService.saveComment(commentRequest);
//            List<CommentResponse> list = commentService.saveComment(commentRequest);
            List<CommentResponse> list = commentService.getPaginateHierarchicallyComments(postId);
            return ApiResponseDto.builder()
                    .successStatus(true)
                    .errorMessage(null)
                    .data(list).build();
        } catch (Exception e) {
            return commentExceptionHandler(e, "댓글 저장 실패. 재시도 바랍니다.");
        }
    }

    @GetMapping("/comment/{commentId}")
    public ApiResponseDto getComment(@PathVariable final Long commentId){
        try {
            CommentResponse comment = commentService.getCommentById(commentId);
            return ApiResponseDto.builder()
                    .successStatus(true)
                    .data(comment).build();
        } catch (Exception e) {
            return commentExceptionHandler(e, "댓글 불러오기 실패, 재시도 바랍니다.");
        }
    }

    // 댓글 리스트 조회
    @GetMapping("/comment/list/{postId}")
    public ApiResponseDto getCommentList(@PathVariable final Long postId) {
        try{
            List<CommentResponse> list = commentService.getPaginateHierarchicallyComments(postId);
            return ApiResponseDto.builder()
                    .successStatus(true)
                    .errorMessage(null)
                    .data(list).build();
        } catch (Exception e) {
            return commentExceptionHandler(e, "댓글 불러오기 실패, 재시도 바랍니다.");
        }
    }

    // 댓글 수정시 유저 확인
    @PostMapping("/comment/user/{commentId}")
    public ApiResponseDto confirmCommentUser(@PathVariable final Long commentId) {
        try {
            if(commentService.confirmCommentWriterAndUser(commentId)) {
                CommentResponse commentResponse = commentService.getCommentById(commentId);
                return ApiResponseDto.builder()
                        .successStatus(true)
                        .errorMessage(null)
                        .data(commentResponse).build();
            } else
                return ApiResponseDto.builder()
                        .successStatus(false)
                        .errorMessage("댓글 작성자만 수정 가능합니다.")
                        .data(null).build();
        } catch (CustomException e) {
            return commentExceptionHandler(e, e.getMessage());
        } catch (Exception e) {
            return commentExceptionHandler(e, "알수 없는 이유로 실패했습니다. 다시 시도해 주세요");
        }
    }

    // 댓글 수정
    @PutMapping("/comment/{commentId}")
    public ApiResponseDto updateComment(@PathVariable final Long commentId, @RequestParam String text) {
        try {
            CommentResponse comment = commentService.updateComment(commentId, text);
            return ApiResponseDto.builder()
                    .successStatus(true)
                    .errorMessage(null)
                    .data(comment).build();
        } catch (CustomException e) {
            return commentExceptionHandler(e, e.getMessage());
        } catch (Exception e) {
            return commentExceptionHandler(e, "알수 없는 이유로 실패했습니다. 다시 시도해 주세요");
        }
    }


    // 댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ApiResponseDto deleteComment(@PathVariable final Long commentId) {
        try {
            CommentResponse comment = commentService.deleteComment(commentId);
            return ApiResponseDto.builder()
                    .successStatus(true)
                    .errorMessage(null)
                    .data(comment).build();
        } catch (CustomException e) {
            return commentExceptionHandler(e, e.getMessage());
        } catch (Exception e) {
            return commentExceptionHandler(e, "알수 없는 이유로 실패했습니다. 다시 시도해 주세요");
        }
    }


    // 로그 출력 후 apiDto 반환
    private ApiResponseDto commentExceptionHandler(Exception e, String message) {
        logger.error(e.getMessage());
        return ApiResponseDto.builder()
                .successStatus(false)
                .errorMessage(message)
                .data(null).build();
    }
}
