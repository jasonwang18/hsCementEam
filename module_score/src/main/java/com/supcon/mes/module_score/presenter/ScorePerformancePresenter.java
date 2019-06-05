package com.supcon.mes.module_score.presenter;

import android.text.TextUtils;

import com.supcon.mes.mbap.constant.ListType;
import com.supcon.mes.module_score.model.bean.ScorePerformanceEntity;
import com.supcon.mes.module_score.model.bean.ScorePerformanceListEntity;
import com.supcon.mes.module_score.model.contract.ScorePerformanceContract;
import com.supcon.mes.module_score.model.network.ScoreHttpClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class ScorePerformancePresenter extends ScorePerformanceContract.Presenter {

    private int position = 0;
    private String category = "";//评分标题
    private ScorePerformanceEntity scorePerformanceTitleEntity;

    @Override
    public void getScoreList(int scoreId) {
        List<String> urls = new ArrayList<>();
        //设备运转率
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559647635248.action");
        //高质量运行
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559291491380.action");
        //安全防护
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559560003995.action");
        //外观标识
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559619724464.action");
        //设备卫生
        urls.add("/BEAM/scorePerformance/scoreHead/data-dg1559631064719.action");
        LinkedHashMap<String, ScorePerformanceEntity> scoreMap = new LinkedHashMap();
        mCompositeSubscription.add(Flowable.fromIterable(urls)
                .concatMap((Function<String, Flowable<ScorePerformanceListEntity>>) url -> ScoreHttpClient.getScore(url, scoreId))
                .onErrorReturn(throwable -> {
                    ScorePerformanceListEntity scorePerformanceListEntity = new ScorePerformanceListEntity();
                    scorePerformanceListEntity.errMsg = throwable.toString();
                    return scorePerformanceListEntity;
                })
                .concatMap((Function<ScorePerformanceListEntity, Flowable<ScorePerformanceEntity>>) scorePerformanceListEntity -> {
                    if (scorePerformanceListEntity.result != null) {
                        return Flowable.fromIterable(scorePerformanceListEntity.result);
                    }
                    return Flowable.fromIterable(new ArrayList<>());
                })
                .subscribe(scorePerformanceEntity -> {
                    if (!TextUtils.isEmpty(scorePerformanceEntity.scoreStandard) && !scorePerformanceEntity.scoreStandard.equals(category)) {
                        position = 0;
                        category = scorePerformanceEntity.scoreStandard;
                        scorePerformanceTitleEntity = new ScorePerformanceEntity();
                        scorePerformanceTitleEntity.scoreStandard = scorePerformanceEntity.scoreStandard;
                        scorePerformanceTitleEntity.defaultTotalScore = scorePerformanceEntity.defaultTotalScore;
                        scorePerformanceTitleEntity.viewType = ListType.TITLE.value();
                        scoreMap.put(scorePerformanceTitleEntity.scoreStandard, scorePerformanceTitleEntity);
                    }

                    ScorePerformanceEntity scorePerformanceOldTitleEntity;
                    if (scoreMap.containsKey(scorePerformanceEntity.itemDetail)) {
                        scorePerformanceOldTitleEntity = scoreMap.get(scorePerformanceEntity.itemDetail);
                    } else {
                        position++;
                        scorePerformanceOldTitleEntity = scorePerformanceEntity;
                    }
                    if (TextUtils.isEmpty(scorePerformanceOldTitleEntity.isItemValue)) {
                        scorePerformanceOldTitleEntity.marks.put(scorePerformanceEntity.scoreItem + "(" + scorePerformanceEntity.score + ")"
                                , scorePerformanceEntity.score);
                        scorePerformanceOldTitleEntity.marksState.put(scorePerformanceEntity.scoreItem + "(" + scorePerformanceEntity.score + ")", scorePerformanceEntity.result);
                    }
                    scorePerformanceOldTitleEntity.Index = position;

                    if (scorePerformanceOldTitleEntity.resultValue != 0) {
                        scorePerformanceOldTitleEntity.viewType = ListType.HEADER.value();
                    } else {
                        scorePerformanceOldTitleEntity.viewType = ListType.CONTENT.value();
                    }
                    if (scorePerformanceTitleEntity != null) {
                        scorePerformanceTitleEntity.scorePerformanceEntities.add(scorePerformanceOldTitleEntity);
                        scorePerformanceOldTitleEntity.scorePerformanceEntity = scorePerformanceTitleEntity;
                    }
                    scoreMap.put(scorePerformanceOldTitleEntity.itemDetail, scorePerformanceOldTitleEntity);
                }, throwable -> {
                    getView().getScoreListFailed(throwable.toString());
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        category = "";
                        getView().getScoreListSuccess(new ArrayList<>(scoreMap.values()));
                    }
                }));
    }
}
