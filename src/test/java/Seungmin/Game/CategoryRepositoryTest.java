package Seungmin.Game;

import Seungmin.Game.domain.category.Category;
import Seungmin.Game.domain.category.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void saveCategoryTest() {
        for(int i = 1; i <= 10; i++) {
            Category category = Category.builder().category("test" + i).build();
            categoryRepository.save(category);
        }
    }

    @Test
    public void showAllCategoryTest() {
        List<Category> list = categoryRepository.findAll();
        list.forEach(System.out::println);
    }
}
