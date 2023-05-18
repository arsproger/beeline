package com.example.beeline;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
class Result {
    private List<CityCount> cityCounts;
}
