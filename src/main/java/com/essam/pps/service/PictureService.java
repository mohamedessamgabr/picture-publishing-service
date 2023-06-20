package com.essam.pps.service;

import com.essam.pps.entity.Picture;

import java.util.List;

public interface PictureService {

    void save(Picture picture);

    void delete(Picture picture);

    List<Picture> findAll();

    List<Picture> findAcceptedPictures();

    List<Picture> findUnprocessedPictures();

    Picture findById(Integer id);

    Picture findByAttachment(String attachment);


}
