package com.valentingiarra.notaapp.persistence.repositories;

import com.valentingiarra.notaapp.persistence.entities.Group;
import com.valentingiarra.notaapp.persistence.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserIdOrderByGroupPositionAscPositionAsc(Long userId);

    List<Note> findAllByUserIdAndGroupIdOrderByPosition(Long userId, Long groupId);

    @Query("SELECT MAX(n.position) FROM Note n WHERE n.user.id = :userId AND n.group.id = :groupId")
    Integer findMaxPositionByUserIdAndGroupId(@Param("userId") Long userId, @Param("groupId") Long groupId);

    Note findByUserIdAndGroupIdAndPosition(Long userId, Long groupId, Integer position);

}