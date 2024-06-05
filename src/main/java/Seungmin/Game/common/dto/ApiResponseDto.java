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
}
