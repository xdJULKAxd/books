package com.example.books.repository;
        import com.example.books.model.Book;
        import com.example.books.model.Category;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;
        import com.example.books.model.User;

        import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
        Optional<Category> findByName(String name);
}