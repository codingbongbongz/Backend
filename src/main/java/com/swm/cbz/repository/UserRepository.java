package com.swm.cbz.repository;

import com.swm.cbz.domain.Users;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByPassword(String password);

    Optional<Users> findByNickname(String nickname);
}
