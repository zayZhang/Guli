package com.guli.edu.vo;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectVO2 {
    private String id ;
    private String title;
    private List<SubjectVO2> children = new ArrayList<>();
}
