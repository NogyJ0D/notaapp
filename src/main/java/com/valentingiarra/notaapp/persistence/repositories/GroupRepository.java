package com.valentingiarra.notaapp.persistence.repositories;

import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findAllByUserId(Long userID);

    List<Group> findAllByUserIdOrderByPosition(Long userId);

    @Query("SELECT MAX(g.position) FROM Group g WHERE g.user.id = :userId")
    Integer findMaxPositionByUserId(@Param("userId") Long userId);

    @Query("SELECT g FROM Group g WHERE g.user.id = :userId AND g.name = 'Without group'")
    Group findWithoutGroupByUserId(@Param("userId") Long userId);

    Group findByUserIdAndPosition(Long userId, Integer position);
}