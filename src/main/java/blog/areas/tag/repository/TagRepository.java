package blog.areas.tag.repository;

import blog.areas.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByName(String name);
}
