package com.supcon.mes.module_score.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_score.model.bean.ScoreListEntity;

import java.util.Map;

@ContractFactory(entites = {ScoreListEntity.class})
public interface ScoreListAPI {
    void getScoreList(Map<String, Object> param,int page);
}
