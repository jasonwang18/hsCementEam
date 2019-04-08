package com.supcon.mes.module_wxgd.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.ResultEntity;
import com.supcon.mes.module_wxgd.model.bean.SparePartJsonEntity;
import com.supcon.mes.module_wxgd.model.bean.StandingCropResultEntity;
import com.supcon.mes.module_wxgd.model.dto.SparePartJsonEntityDto;

import java.util.List;
import java.util.Map;

@ContractFactory(entites = {CommonListEntity.class,ResultEntity.class})
public interface SparePartListAPI {

    /**
     * @description 更新现存量
     * @param productCode 备件编码字符串
     * @return
     * @author zhangwenshuai1 2018/10/10
     *
     */
    void updateStandingCrop(String productCode);

    /**
     * @description 领用出库单
     * @param sparePartJsonEntityDto
     * @return
     * @author zhangwenshuai1 2018/10/24
     *
     */
    void generateSparePartApply(String listStr);

}
