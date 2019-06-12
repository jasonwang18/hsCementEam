package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;

import java.util.List;

@ContractFactory(entites = {List.class, CommonListEntity.class})
public interface ScoreStaffPerformanceAPI {

    void getStaffScore(int scoreId);

    void getDutyEam(long staffId);

}
