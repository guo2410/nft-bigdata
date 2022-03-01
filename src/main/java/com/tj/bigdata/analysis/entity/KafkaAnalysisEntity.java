package com.tj.bigdata.analysis.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guoch
 */
@Data
public class KafkaAnalysisEntity {
    List<AnalysisTxEntity> analysisTxEntities = new ArrayList<>();
}
