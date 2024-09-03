package com.radovan.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.NoteEntity;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Integer> {

}
