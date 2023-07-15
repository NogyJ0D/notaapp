package com.valentingiarra.notaapp.persistence.repositories;

import com.valentingiarra.notaapp.persistence.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
