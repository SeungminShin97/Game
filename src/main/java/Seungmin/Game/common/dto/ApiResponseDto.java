package Seungmin.Game.common.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {

    private boolean successStatus;
    private String errorMessage;
    private Object data;
    private String redirectUri;
    private boolean postSaved;    // 저장이면 true, 수정이면 false
}
