package com.swm.cbz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTO {
    private Long transcriptId;
    private Long evaluationId;
    private Long overall;
    private Long pronunciation;
    private Long fluency;
    private Long integrity;
    private Long rhythm;
    private Long speed;
    private Date createdAt;
}
