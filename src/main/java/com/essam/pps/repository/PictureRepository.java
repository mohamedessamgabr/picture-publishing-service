package com.essam.pps.repository;

import com.essam.pps.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends JpaRepository<Picture, Integer> {


    @Query("SELECT p FROM Picture p WHERE p.status = :status")
    List<Picture> getPicturesByStatus(String status);


    @Query("SELECT p FROM Picture p WHERE p.attachment = :attachment")
    Optional<Picture> getPicturesByAttachment(String attachment);


}
