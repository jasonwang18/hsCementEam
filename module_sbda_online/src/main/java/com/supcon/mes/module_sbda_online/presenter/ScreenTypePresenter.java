package com.supcon.mes.module_sbda_online.presenter;

import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.module_sbda_online.model.bean.ScreenEntity;
import com.supcon.mes.module_sbda_online.model.bean.ScreenListEntity;
import com.supcon.mes.module_sbda_online.model.contract.ScreenTypeContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class ScreenTypePresenter extends ScreenTypeContract.Presenter {

    private String url;

    @Override
    public void screenPart(CustomFilterView customFilterView) {

        ScreenEntity screenEntity = new ScreenEntity();
        screenEntity.name = "类型不限";
        List<ScreenEntity> screenEntities = new LinkedList<>();
        screenEntities.add(screenEntity);
        url = "/BEAM/eamType/eamType/typePart-query.action?page.pageSize=500";

        mCompositeSubscription.add(SBDAOnlineHttpClient.screenPart(url)
                .onErrorReturn(throwable -> {
                    ScreenListEntity screenListEntity = new ScreenListEntity();
                    screenListEntity.errMsg = throwable.toString();
                    return screenListEntity;
                }).subscribe(screenListEntity -> {
                    if (screenListEntity.result != null && screenListEntity.result.size() > 0) {
                        screenEntities.addAll(screenListEntity.result);
                        customFilterView.setData(screenEntities);
                    } else {
                        customFilterView.setData(screenEntities);
                    }
                }));
    }
}
