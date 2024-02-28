package ru.skillbox.homework4.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.homework4.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
