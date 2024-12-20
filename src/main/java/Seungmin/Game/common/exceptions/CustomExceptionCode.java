package Seungmin.Game.common.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionCode {

    PostNotFoundException("Post not found."),
    PostSaveFailedException("Failed to save the post. Please try again."),
    PostUpdateFailedException("Failed to update the post. Please try again."),
    PostDeleteFailedException("Failed to delete the post. Please try again."),

    MemberSaveFailedException("Failed to save the member. Please try again."),
    UnauthenticatedUserException("Unauthenticated user."),

    CommentNotFoundException("Comment not found."),

    FileCreateFailedException("An error occurred while saving the file."),
    FileUpdateFailedException("An error occurred while updating the file."),
    FileDeleteFailedException("An error occurred while deleting the file."),
    FileNotFoundException("An error occurred while searching for the file."),

    ChatSaveFailedException("An error occurred while saving the chat."),

    ChatRoomNotFoundException("ChatRoom not found")
    ;

    private final String errorMessage;
}
