package com.supcon.mes.module_wxgd.presenter;

import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_wxgd.model.bean.LubricateListEntity;
import com.supcon.mes.module_wxgd.model.contract.LubricateContract;
import com.supcon.mes.module_wxgd.model.network.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 */
public class LubricatePresenter extends LubricateContract.Presenter {
    @Override
    public void listLubricate(int pageNum,Map<String, Object> queryParam) {
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryParam);
        fastQueryCondEntity.modelAlias = "lubricateOil";
        Map<String, Object> pageQueryParam = new HashMap<>();
        pageQueryParam.put("page.pageSize", 20);
        pageQueryParam.put("page.maxPageSize", 500);
        pageQueryParam.put("page.pageNo", pageNum);

        mCompositeSubscription.add(HttpClient.listLubricate(fastQueryCondEntity, pageQueryParam).onErrorReturn(throwable -> {
            LubricateListEntity lubricateListEntity = new LubricateListEntity();
            lubricateListEntity.errMsg = throwable.toString();
            return lubricateListEntity;
        }).subscribe(lubricateListEntity -> {
            if (lubricateListEntity.errMsg == null) {
                getView().listLubricateSuccess(lubricateListEntity);
            } else {
                getView().listLubricateFailed(lubricateListEntity.errMsg);
            }
        }));
    }
}
