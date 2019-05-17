package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.RefLubricateListEntity;

import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/5
 * ------------- Description -------------
 */
@ContractFactory(entites = RefLubricateListEntity.class)
public interface RefLubricateAPI {

    void listRefLubricate(int pageNum, Long eamID, Map<String, Object> queryParam);
}
