package com.supcon.mes.module_score.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.EamType;
import com.supcon.mes.middleware.model.bean.Staff;

public class ScoreEntity extends BaseEntity {

    public EamType beamId;
    public int id = -1;

    public Staff scoreStaff;//评分人
    public String scoreTableNo;//单据编号
    public Long scoreTime;//评分时间

    //得分
    public int scoreNum;//得分
    public int operationRate;//设备运转率
    public int highQualityOperation;//高质量运行
    public int security;//安全防护
    public int appearanceLogo;//外观标识
    public int beamHeath;//设备卫生
    public int beamEstimate;//档案管理

    public EamType getBeamId() {
        if (beamId == null) {
            beamId = new EamType();
        }
        return beamId;
    }

    public Staff getScoreStaff() {
        if (scoreStaff == null) {
            scoreStaff = new Staff();
        }
        return scoreStaff;
    }
}
