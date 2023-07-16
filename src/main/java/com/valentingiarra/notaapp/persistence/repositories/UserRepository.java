package com.valentingiarra.notaapp.persistence.repositories;

import com.valentingiarra.notaapp.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findFirstByEmail(String email);
    public User findFirstByUsername(String username);
}
