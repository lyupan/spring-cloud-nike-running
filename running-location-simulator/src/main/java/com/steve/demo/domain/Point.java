package com.steve.demo.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Point {
    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return "Point[lat/long:" +latitude + "," + longitude + "]";
    }
}
