package com.supcon.mes.module_score.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ScoreStaffPerformanceEntity extends BaseEntity {

    public int averageScore;

    public String category;//标题
    public String project;//大标题
    public String grade;//评分标准
    public String item;//事项

    public String isItemValue;
    public String noItemValue;
    public int itemScore;//单项分
    public boolean result;//选中结果

    public int fraction;//单项总分数
    public int score;//默认总分数

    @Expose
    public int viewType = 0;
    @Expose
    public int Index = 0;

    @Expose
    public ScoreStaffPerformanceEntity scoreEamPerformanceEntity;//标题

    @Expose
    private Integer totalHightScore;//单项最高总分数
    @Expose
    public Float scoreNum;//总分

    //子布局
    @Expose
    public Set<ScoreStaffPerformanceEntity> scorePerformanceEntities = new HashSet<>();

    public Integer getTotalHightScore() {
        if (totalHightScore == null) {
            totalHightScore = score;
        }
        return totalHightScore;
    }

    public void setTotalHightScore(Integer totalHightScore) {
        this.totalHightScore = totalHightScore;
    }
}
