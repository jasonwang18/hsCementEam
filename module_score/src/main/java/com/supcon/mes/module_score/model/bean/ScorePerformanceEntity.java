package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ScorePerformanceEntity extends BaseEntity {

    public String isItemValue;//是
    public String noItemValue;//否
    public String item;
    public String itemDetail;//评分详情
    public boolean result;//选中结果
    public int score;//分数
    public String scoreStandard;//标题
    public String scoreItem;//选项内容

    public String accidentStopTime;//停机时长
    public String totalRunTime;//累计运行时长
    public float resultValue;//结果

    @Expose
    public int viewType = 0;
    @Expose
    public int Index = 0;
    @Expose
    public Map<String, Integer> marks = new LinkedHashMap<>();//多选项
    @Expose
    public Map<String, Boolean> marksState = new LinkedHashMap<>();//多选项状态
    @Expose
    public ScorePerformanceEntity scorePerformanceEntity;
    @Expose
    public Integer totalScore;//总分数

    public int defaultTotalScore;//默认总分数
    //子布局
    @Expose
    public Set<ScorePerformanceEntity> scorePerformanceEntities = new HashSet<>();

    public int getTotalScore() {
        if (totalScore == null) {
            totalScore = defaultTotalScore;
        }
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
