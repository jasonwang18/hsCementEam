package com.supcon.mes.module_sbda_online.presenter;

import com.supcon.mes.mbap.view.CustomFilterView;
import com.supcon.mes.module_sbda_online.model.bean.ScreenEntity;
import com.supcon.mes.module_sbda_online.model.bean.ScreenListEntity;
import com.supcon.mes.module_sbda_online.model.contract.ScreenContract;
import com.supcon.mes.module_sbda_online.model.network.SBDAOnlineHttpClient;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yangfei.cao
 * @ClassName hongShiCementEam
 * @date 2019/4/1
 * ------------- Description -------------
 */
public class ScreenPresenter extends ScreenContract.Presenter {
    public static int AREA = 0x01;
    public static int TYPE = 0x02;
    private String url;

    @Override
    public void screenPart(CustomFilterView customFilterView, int type) {

        ScreenEntity screenEntity = new ScreenEntity();
        if (type == TYPE) {
            screenEntity.name = "类型不限";
        } else if (type == AREA) {
            screenEntity.name = "区域不限";
        }
        List<ScreenEntity> screenEntities = new LinkedList<>();
        screenEntities.add(screenEntity);

        if (type == AREA) {
            url = "/BEAM/area/area/areaList-query.action";
        } else if (type == TYPE) {
            url = "/BEAM/eamType/eamType/typePart-query.action";
        }
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
