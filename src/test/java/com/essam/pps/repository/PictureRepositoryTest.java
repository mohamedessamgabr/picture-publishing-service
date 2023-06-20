package com.essam.pps.repository;

import com.essam.pps.entity.Category;
import com.essam.pps.entity.Picture;
import com.essam.pps.entity.PictureStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
class PictureRepositoryTest {

    @Autowired
    private PictureRepository pictureRepository;

    @Test
    void CheckIfGetPicturesByStatusReturnTheCorrectValue() {
        Picture picture = createPicture();
        Picture save = pictureRepository.save(picture);
        assertTrue(pictureRepository.getPicturesByStatus(PictureStatus.WAITING.name()).contains(save));
    }

    @Test
    void CheckIfGetPicturesByAttachmentReturnTheCorrectValue() {
        Picture picture = createPicture();
        pictureRepository.save(picture);
        assertEquals(picture, pictureRepository.getPicturesByAttachment("a.png").get());
    }

    Picture createPicture () {
        return Picture
                .builder()
                .description("aaaaaa")
                .category(Category.NATURE)
                .status(PictureStatus.WAITING.name())
                .attachment("a.png")
                .build();
    }
}