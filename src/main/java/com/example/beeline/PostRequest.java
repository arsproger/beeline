package com.example.beeline;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class PostRequest {
    private Student student;
    private Result result;
}
