package com.essam.pps.service;

import com.essam.pps.entity.Picture;
import com.essam.pps.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService{

    private final PictureRepository pictureRepository;
    @Override
    public void save(Picture picture) {
        pictureRepository.save(picture);
    }

    @Override
    public void delete(Picture picture) {

    }

    @Override
    public List<Picture> findAll() {
        return pictureRepository.findAll();
    }

    @Override
    public List<Picture> findAcceptedPictures() {
        return pictureRepository.getPicturesByStatus("ACCEPTED");
    }

    @Override
    public List<Picture> findUnprocessedPictures() {
        return pictureRepository.getPicturesByStatus("WAITING");
    }

    @Override
    public Picture findById(Integer id) {
        return pictureRepository.findById(id).orElse(null);
    }

    @Override
    public Picture findByAttachment(String attachment) {
        return pictureRepository.getPicturesByAttachment(attachment).orElse(null);
    }
}
