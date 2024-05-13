package Seungmin.Game.common.dto;

import Seungmin.Game.common.enums.SearchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {
    private SearchType searchType = SearchType.all;      // all, title, content, writer
    private String categoryType;
    private String keyword;


}
