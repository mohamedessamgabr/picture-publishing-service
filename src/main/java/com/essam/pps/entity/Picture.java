package com.essam.pps.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "picture")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "status")
    private String status;


}
