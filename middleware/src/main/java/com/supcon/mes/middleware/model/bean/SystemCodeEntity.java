package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.EamApplication;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wangshizhan on 2018/3/24.
 * Email:wangshizhan@supcon.com
 */
@Entity
public class SystemCodeEntity extends BaseEntity {

    @Id
    public String id;

    public String value;

    public String entityCode;

    public String code;

    public String layRec;

    public String ip = EamApplication.getIp();

    @Generated(hash = 265982534)
    public SystemCodeEntity(String id, String value, String entityCode, String code,
            String layRec, String ip) {
        this.id = id;
        this.value = value;
        this.entityCode = entityCode;
        this.code = code;
        this.layRec = layRec;
        this.ip = ip;
    }

    @Generated(hash = 155349121)
    public SystemCodeEntity() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEntityCode() {
        return this.entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLayRec() {
        return this.layRec;
    }

    public void setLayRec(String layRec) {
        this.layRec = layRec;
    }

}