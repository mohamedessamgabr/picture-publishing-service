package com.essam.pps.entity;

public enum Category {

    LIVING_THING("Living thing"),
    MACHINE("Machine"),
    NATURE("Nature");

    private final String category;
    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
