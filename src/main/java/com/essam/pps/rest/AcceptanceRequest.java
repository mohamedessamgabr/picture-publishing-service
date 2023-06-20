package com.essam.pps.rest;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AcceptanceRequest {
    private Integer pictureId;
    private boolean accepted;
}
