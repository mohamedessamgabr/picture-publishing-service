package com.essam.pps.rest;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UnprocessedImageResponse {

    private Integer id;

    private String description;

    private String category;

    private Integer width;

    private Integer height;

    private String image;

}
