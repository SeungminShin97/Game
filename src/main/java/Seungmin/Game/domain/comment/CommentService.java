package Seungmin.Game.domain.comment;

import Seungmin.Game.common.exceptions.CustomException;
import Seungmin.Game.common.exceptions.CustomExceptionCode;
import Seungmin.Game.domain.comment.commentDto.Comment;
import Seungmin.Game.domain.comment.commentDto.CommentRequest;
import Seungmin.Game.domain.comment.commentDto.CommentResponse;
import Seungmin.Game.domain.member.MemberService;
import Seungmin.Game.domain.member.memberDto.Member;
import Seungmin.Game.domain.post.PostService;
import Seungmin.Game.domain.post.postDto.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MemberService memberService;

    @Transactional
    public List<CommentResponse> saveComment(CommentRequest commentRequest) {
        Post post = postService.findPostById(commentRequest.getPostId());
        Member member = getMemberByAuthentication();
        Comment parent = null;
        if(commentRequest.getParentId() != null)
                parent = commentRepository.findById(commentRequest.getParentId()).orElseThrow(() -> new CustomException(CustomExceptionCode.CommentNotFoundException));
        Comment comment = CommentRequest.toEntity(commentRequest, post, member, parent);
        commentRepository.save(comment);
        return getPaginateHierarchicallyComments(post.getId());
    }



    public List<CommentResponse> getPaginateHierarchicallyComments(final long postId) {
        List<Comment> commentList = commentRepository.findCommentsHierarchically(postId);
        if(commentList == null)
            return null;

        Map<Long, CommentResponse> commentMap = commentList.stream().map(comment -> {
            CommentResponse commentResponse = Comment.toDto(comment);
            commentResponse.checkDeleteYn();
            return commentResponse;
        }).collect(Collectors.toMap(CommentResponse::getId, commentResponse -> commentResponse));

        List<CommentResponse> convertedList = new ArrayList<>();

        commentMap.forEach((id, commentResponse) -> {
            Long parentId = commentResponse.getParentId();
            if(parentId != null)
                commentMap.get(parentId).setChildren(commentResponse);
            else
                convertedList.add(commentResponse);
        });

        return convertedList;
    }

    @Transactional
    public CommentResponse updateComment(final Long commentId, String text) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomExceptionCode.CommentNotFoundException));
        comment.updateComment(text);
        return Comment.toDto(comment);
    };

    @Transactional
    public CommentResponse deleteComment(final Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomExceptionCode.CommentNotFoundException));
        comment.deleteComment();
        return Comment.toDto(comment);
    };

    public boolean confirmCommentWriterAndUser(final Long commentId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = memberService.getMemberByAuthentication(authentication).getId();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomExceptionCode.CommentNotFoundException));
        Long commentMemberId = comment.getMember().getId();

        return memberId.equals(commentMemberId);
    }

    public CommentResponse getCommentById(final Long commentId) {
        return Comment.toDto(commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomExceptionCode.CommentNotFoundException)));
    }



    private Member getMemberByAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberService.getMemberByAuthentication(authentication);
    }

}
