package dev.backendintegratedproject.userManage;

import dev.backendintegratedproject.userManage.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {


    UserEntity findByUsernameAndPassword(String username, String password);

    Optional<UserEntity> findByUsername(String oid);
}
