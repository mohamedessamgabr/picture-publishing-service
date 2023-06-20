package com.essam.pps.rest;


import com.essam.pps.entity.Category;
import com.essam.pps.entity.Picture;
import com.essam.pps.entity.PictureStatus;
import com.essam.pps.exceptionhandling.ImageNotFoundException;
import com.essam.pps.service.PictureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "Picture Operations")
@RestController
@RequestMapping("/pps/pictures")
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @Value("${Pictures.location}")
    private String location;



    @ApiOperation(value = "Get the list of the accepted pictures which available for all people")
    @GetMapping("/landing")
    @PreAuthorize("permitAll")
    public ResponseEntity<List<String>> getAcceptedPictures() {
        return ResponseEntity.ok(pictureService.findAcceptedPictures().stream()
                .map(picture -> "/pps/pictures/landing/"  + picture.getAttachment())
                .collect(Collectors.toList()));
    }


    @ApiOperation(value = "Get the list of the unprocessed pictures which available for administrators only")
    @GetMapping("/unprocessed")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> getUnprocessedPictures() {
        return ResponseEntity.ok(pictureService.findUnprocessedPictures().stream()
                .map(picture  ->"/pps/pictures/unprocessed/" + picture.getAttachment())
                .collect(Collectors.toList()));
    }

    @ApiOperation(value = "Upload Picture by any logged-in user")
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> uploadPicture(
            @ApiParam(name = "description", value = "The description of the picture") @RequestParam("description") String description,
            @ApiParam(name = "category", value = "The category of the picture") @RequestParam("category") String category,
            @ApiParam(name = "picture", value = "The picture to be uploaded") @RequestParam("picture") MultipartFile picture) throws IOException {

        if (description == null) {
            throw new IllegalArgumentException("You must insert the description");
        }
        List<String> categories = Arrays.asList(Category.MACHINE.getCategory(),
                Category.NATURE.getCategory(),
                Category.LIVING_THING.getCategory());

        if (!categories.contains(category)) {
            throw new IllegalArgumentException("You must insert category from (Living thing, Nature, Machine)");
        }

        if (picture == null) {
            throw new IllegalArgumentException("You must insert the picture");
        }

        long size = picture.getSize();
        String contentType = picture.getContentType();
        List<String> validImageTypes = Arrays.asList("image/jpeg", "image/png", "image/gif");

        if (size > 2L * 1024 * 1024) {
            throw new IllegalArgumentException("Images with size greater that 2MB are not allowed");
        }

        if (!validImageTypes.contains(contentType)) {
            throw new IllegalArgumentException("jpeg, png, and gif are only supported");
        }
        Category cat;
        if(category.equals("Nature")) {
            cat = Category.NATURE;
        } else if (category.equals("Living thing")) {
            cat = Category.LIVING_THING;
        } else {
            cat = Category.MACHINE;
        }
        String fileName = picture.getOriginalFilename();
        final String finalFileName = fileName;
        List<Picture> pictureList = pictureService.findAll().stream()
                .filter(picture1 -> picture1.getAttachment().equals(finalFileName))
                .collect(Collectors.toList());


        if(pictureList.size() > 0) {
            fileName = System.currentTimeMillis() + '_' + fileName;
        }

        Path uploadDir = Paths.get(location);
        Files.write(Paths.get(uploadDir.toString(), fileName), picture.getBytes());
        Picture pic = Picture.builder()
                .description(description)
                .category(cat)
                .attachment(fileName)
                .status(PictureStatus.WAITING.toString())
                .build();
        pictureService.save(pic);

        return ResponseEntity.ok("Picture has been uploaded successfully");
    }



    @ApiOperation(value = "Admin accepts or rejects the pictures")
    @PostMapping("/acceptance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> acceptOrReject(
            @ApiParam(name = "Accept or reject request", value = "the id of the picture and boolean value of acceptance")
            @RequestBody AcceptanceRequest acceptanceRequest) throws ImageNotFoundException {

        Picture picture = pictureService.findById(acceptanceRequest.getPictureId());
        if (picture == null) {
            throw new ImageNotFoundException("Image Not Found");
        }
        if(picture.getStatus().equals(PictureStatus.REJECTED.name())) {
            throw new ImageNotFoundException("The picture has been rejected and deleted as well");
        }

        if(picture.getStatus().equals(PictureStatus.ACCEPTED.name())) {
            throw new IllegalArgumentException("Image has been accepted before. Hence, it cannot be deleted");
        }

        if(acceptanceRequest.isAccepted()) {
            picture.setStatus(PictureStatus.ACCEPTED.toString());
            pictureService.save(picture);
            return ResponseEntity.ok("Picture has been accepted");
        } else {
            try {
                Files.delete(Paths.get(Objects.requireNonNull(location), picture.getAttachment()));
                picture.setStatus(PictureStatus.REJECTED.toString());
                pictureService.save(picture);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return ResponseEntity.ok("Picture has been Rejected");
        }
    }


    @ApiOperation(value = "Display the accepted pictures for all people in the browser")
    @GetMapping(value = "/landing/{image}")
    @PreAuthorize("permitAll")
    public ResponseEntity<byte[]> viewImage(
            @ApiParam(name = "image", value = "the name of the image with its extension")
            @PathVariable String image) throws IOException {

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(getImage(image));

    }


    @ApiOperation(value = "Display the unprocessed picture for Admins only")
    @GetMapping(value = "/unprocessed/{image}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<byte[]> viewUnProcessedImage(
            @ApiParam(name = "image", value = "the name of the image with its extension")
            @PathVariable String image) throws IOException {

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(getImage(image));

    }

    @ApiOperation(value = "Display the unprocessed picture details for Admins only")
    @GetMapping(value = "/unprocessed/details/{image}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UnprocessedImageResponse> viewUnProcessedImageDetails(
            @ApiParam(name = "image", value = "the name of the image with its extension")
            @PathVariable String image) throws IOException {
        Picture picture = pictureService.findByAttachment(image);
        return ResponseEntity.ok(
                UnprocessedImageResponse.builder()
                        .id(picture.getId())
                        .description(picture.getDescription())
                        .category(picture.getCategory().name())
                        .width(getPictureWidth(getImage(picture.getAttachment())))
                        .height(getPictureHeight(getImage(picture.getAttachment())))
                        .image("/pps/pictures/unprocessed/"+image)
                        .build()
        );

    }


    private byte[] getImage(String image) throws IOException {
        byte [] imageBytes;
        File imageFile = new File(location +'\\'+ image);
        if(!imageFile.exists()) {
            throw new ImageNotFoundException("Image is Not found");
        }
        imageBytes = FileUtils.readFileToByteArray(imageFile);
        return imageBytes;
    }

    private int getPictureWidth(byte[] picture) throws IOException {
        InputStream in = new ByteArrayInputStream(picture);
        BufferedImage image = ImageIO.read(in);
        return image.getWidth();
    }


    private int getPictureHeight(byte[] picture) throws IOException{
        InputStream in = new ByteArrayInputStream(picture);
        BufferedImage image = ImageIO.read(in);
        return image.getHeight();
    }


}
