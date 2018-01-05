package com.steve.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Leg {
    private int id;
    private Point startPosition;
    private Point endPosition;
    private double length;
    private double heading;
}
