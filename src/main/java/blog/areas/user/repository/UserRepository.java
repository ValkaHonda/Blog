package blog.areas.user.repository;

import blog.areas.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByEmail(String email);
//    @Query(value = "SELECT * FROM users " +
//            "WHERE user.username = :username",nativeQuery = true)
//    User findByEmail(@Param("username") String username);
}
