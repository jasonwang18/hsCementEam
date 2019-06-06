package com.supcon.mes.module_score.ui.util;

import android.annotation.SuppressLint;

import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_score.model.bean.ScoreEntity;
import com.supcon.mes.module_score.model.bean.ScorePerformanceEntity;
import com.supcon.mes.module_score.model.dto.ScoreDto;

import org.reactivestreams.Publisher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class ScoreMapManager {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static Map<String, Object> createMap(ScoreEntity scoreEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("viewselect", "beamPerformanceEdit");
        map.put("datagridKey", "BEAM_scorePerformance_scoreHead_beamPerformanceEdit_datagrids");
        map.put("viewCode", "BEAM_1.0.0_scorePerformance_beamPerformanceEdit");
        map.put("modelName", "ScoreHead");

        map.put("scoreHead.beamId.id", Util.strFormat2(scoreEntity.getBeamId().id));
        map.put("scoreHead.scoreStaff.id", Util.strFormat2(scoreEntity.getScoreStaff().id));
        map.put("scoreHead.scoreTableNo", Util.strFormat2(scoreEntity.scoreTableNo));
        map.put("scoreHead.scoreTime", format.format(scoreEntity.scoreTime));

        map.put("scoreHead.scoreNum", Util.strFormat2(scoreEntity.scoreNum));
        map.put("scoreHead.operationRate", Util.strFormat2(scoreEntity.operationRate));
        map.put("scoreHead.highQualityOperation", Util.strFormat2(scoreEntity.highQualityOperation));
        map.put("scoreHead.security", Util.strFormat2(scoreEntity.security));
        map.put("scoreHead.appearanceLogo", Util.strFormat2(scoreEntity.appearanceLogo));
        map.put("scoreHead.beamHeath", Util.strFormat2(scoreEntity.beamHeath));
        map.put("scoreHead.beamEstimate", Util.strFormat2(scoreEntity.beamEstimate));

        map.put("bap_validate_user_id", EamApplication.getAccountInfo().userId);
        return map;
    }

    @SuppressLint("CheckResult")
    public static void dataChange(List<ScorePerformanceEntity> scorePerformanceEntities, ScoreEntity scoreEntity) {
        Flowable.fromIterable(scorePerformanceEntities)
                .filter(scorePerformanceEntity -> {
                    if (scorePerformanceEntity.viewType == ListType.TITLE.value()) {
                        return true;
                    }
                    return false;
                })
                .subscribe(scorePerformanceEntity -> {
                    if (scorePerformanceEntity.scoreStandard.contains("设备运转率")) {
                        scoreEntity.operationRate = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("高质量运行")) {
                        scoreEntity.highQualityOperation = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("安全防护")) {
                        scoreEntity.security = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("外观标识")) {
                        scoreEntity.appearanceLogo = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("设备卫生")) {
                        scoreEntity.beamHeath = scorePerformanceEntity.getTotalScore();
                    } else if (scorePerformanceEntity.scoreStandard.contains("档案管理")) {
                        scoreEntity.beamEstimate = scorePerformanceEntity.getTotalScore();
                    }
                });
    }

    @SuppressLint("CheckResult")
    public static List<ScoreDto> dataChange(List<ScorePerformanceEntity> scorePerformanceEntities, String contains) {
        List<ScoreDto> scorePerformanceDto = new ArrayList<>();
        Flowable.fromIterable(scorePerformanceEntities)
                .filter(scorePerformanceEntity -> {
                    if (scorePerformanceEntity.viewType == ListType.CONTENT.value() || scorePerformanceEntity.viewType == ListType.HEADER.value()) {
                        if (scorePerformanceEntity.scoreStandard.contains(contains)) {
                            Map<String, ScorePerformanceEntity> scorePerformanceEntityMap = scorePerformanceEntity.scorePerformanceEntityMap;
                            Map<String, Boolean> marksState = scorePerformanceEntity.marksState;
                            for (String key : scorePerformanceEntityMap.keySet()) {
                                ScorePerformanceEntity scoreEntity = scorePerformanceEntityMap.get(key);
                                scoreEntity.result = marksState.get(key);
                            }
                            return true;
                        }
                    }
                    return false;
                })
                .flatMap((Function<ScorePerformanceEntity, Publisher<ScorePerformanceEntity>>) scorePerformanceEntity -> {
                    if (scorePerformanceEntity.scorePerformanceEntityMap.size() > 0) {
                        return Flowable.fromIterable(scorePerformanceEntity.scorePerformanceEntityMap.values());
                    } else {
                        return Flowable.just(scorePerformanceEntity);
                    }
                })
                .subscribe(scorePerformanceEntity -> {
                    ScoreDto scoreDto = new ScoreDto();
                    scoreDto.item = scorePerformanceEntity.item;
                    scoreDto.result = Util.strFormat2(scorePerformanceEntity.result);
                    scoreDto.score = Util.strFormat2(scorePerformanceEntity.score);
                    scoreDto.itemDetail = scorePerformanceEntity.itemDetail;
                    scoreDto.isItemValue = scorePerformanceEntity.isItemValue;
                    scoreDto.noItemValue = scorePerformanceEntity.noItemValue;
                    scoreDto.scoreItem = scorePerformanceEntity.scoreItem;
                    scoreDto.scoreStandard = scorePerformanceEntity.scoreStandard;
                    scoreDto.resultValue = Util.strFormat2(scorePerformanceEntity.resultValue);
                    scoreDto.accidentStopTime = Util.strFormat2(scorePerformanceEntity.accidentStopTime);
                    scoreDto.totalRunTime = Util.strFormat2(scorePerformanceEntity.totalRunTime);
                    scorePerformanceDto.add(scoreDto);
                });
        return scorePerformanceDto;
    }
}
