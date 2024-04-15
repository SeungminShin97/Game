package Seungmin.Game.domain.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //CREATE

    //UPDATE

    //READ

    /**
     * 카테고리 리스트 반환
     * @return List<Category>
     */
    public List<Category> showCategoryList() { return categoryRepository.findAll(); };

    /**
     * 카테고리 반환
     * @param category
     * @return Category
     */
    public Category showCategoryByCategory(final String category) { return categoryRepository.findByCategory(category); }


    //DELETE

}
