package com.vwsaigon.repository;

import com.vwsaigon.entity.HandoverPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HandoverPhotoRepository extends JpaRepository<HandoverPhoto, Long> {

    @Query("SELECT p FROM HandoverPhoto p WHERE p.active = true ORDER BY p.sortOrder ASC, p.createdAt ASC")
    List<HandoverPhoto> findActiveOrdered();

    @Query("SELECT p FROM HandoverPhoto p ORDER BY p.sortOrder ASC, p.createdAt ASC")
    List<HandoverPhoto> findAllOrdered();
}
