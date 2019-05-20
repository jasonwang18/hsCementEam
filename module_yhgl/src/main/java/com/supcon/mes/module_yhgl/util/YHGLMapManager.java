package com.supcon.mes.module_yhgl.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.model.bean.AcceptanceCheckEntity;
import com.supcon.mes.middleware.model.bean.LubricateOilsEntity;
import com.supcon.mes.middleware.model.bean.MaintainEntity;
import com.supcon.mes.middleware.model.bean.RepairStaffEntity;
import com.supcon.mes.middleware.model.bean.SparePartEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.middleware.model.bean.WXGDEntity;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_yhgl.model.dto.GoodDto;
import com.supcon.mes.module_yhgl.model.dto.LubricateOilDto;
import com.supcon.mes.module_yhgl.model.dto.LubricateOilsEntityDto;
import com.supcon.mes.module_yhgl.model.dto.MaintainDto;
import com.supcon.mes.module_yhgl.model.dto.RepairStaffDto;
import com.supcon.mes.module_yhgl.model.dto.SparePartEntityDto;
import com.supcon.mes.module_yhgl.model.dto.StaffDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author yangfei.cao
 * @ClassName eam
 * @date 2018/9/4
 * ------------- Description -------------
 * 提交接口参数
 */
public class YHGLMapManager {

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public static Map<String, Object> createMap(WXGDEntity mWXGDEntity) {
        Map<String, Object> map = new HashMap<>();
        map.put("workRecord.createPositionId", EamApplication.getAccountInfo().positionId);
        map.put("modelName", "WorkRecord");
        map.put("bap_validate_user_id", EamApplication.getAccountInfo().userId);
        map.put("workRecord.createStaffId", EamApplication.getAccountInfo().staffId);
        map.put("workRecord.createTime", format.format(mWXGDEntity.createTime));
        map.put("id", mWXGDEntity.id);
        map.put("workRecord.version", mWXGDEntity.version);
        map.put("deploymentId", (mWXGDEntity.pending != null && mWXGDEntity.pending.deploymentId != null) ? mWXGDEntity.pending.deploymentId : "");
        map.put("webSignetFlag", false);
        map.put("pendingId", (mWXGDEntity.pending != null && mWXGDEntity.pending.id != null) ? mWXGDEntity.pending.id : "");
        map.put("workRecord.repairGroup.id", mWXGDEntity.repairGroup != null && mWXGDEntity.repairGroup.id != null ? mWXGDEntity.repairGroup.id : "");
        map.put("workRecord.faultInfo.id", (mWXGDEntity.faultInfo != null && mWXGDEntity.faultInfo.id != null) ? mWXGDEntity.faultInfo.id : "");
        map.put("workRecord.id", mWXGDEntity.id);
        map.put("workRecord.chargeStaff.id", (mWXGDEntity.chargeStaff != null && mWXGDEntity.chargeStaff.id != null) ? mWXGDEntity.chargeStaff.id : "");
        map.put("workRecord.eamID.id", (mWXGDEntity.eamID != null && mWXGDEntity.eamID.id != null) ? mWXGDEntity.eamID.id : "");
        map.put("workRecord.planStartDate", mWXGDEntity.planStartDate == null ? "" : format.format(mWXGDEntity.planStartDate));
        map.put("workRecord.planEndDate", mWXGDEntity.planEndDate == null ? "" : format.format(mWXGDEntity.planEndDate));
        map.put("workRecord.workSource.id", (mWXGDEntity.workSource != null && mWXGDEntity.workSource.id != null) ? mWXGDEntity.workSource.id : "");
        map.put("workRecord.workSource.value", mWXGDEntity.workSource != null ? mWXGDEntity.workSource.value : "");
        map.put("workRecord.content", TextUtils.isEmpty(mWXGDEntity.content) ? "" : mWXGDEntity.content);
        map.put("workRecord.claim", TextUtils.isEmpty(mWXGDEntity.claim) ? "" : mWXGDEntity.claim);
        map.put("workRecord.period", mWXGDEntity.period == null ? "" : mWXGDEntity.period);
        map.put("workRecord.thisDuration", mWXGDEntity.thisDuration == null ? "" : mWXGDEntity.thisDuration);
        map.put("workRecord.totalDuration", mWXGDEntity.totalDuration == null ? "" : mWXGDEntity.totalDuration);
        map.put("workRecord.lastDuration", mWXGDEntity.lastDuration == null ? "" : mWXGDEntity.lastDuration);
        map.put("workRecord.lastTime", mWXGDEntity.lastTime != null ? format.format(mWXGDEntity.lastTime) : "");
//        map.put("workRecord.nextTime", mWXGDEntity.nextTime != null ? sdf.format(Long.valueOf(mWXGDEntity.nextTime)) : "");
        map.put("workRecord.realEndDate", mWXGDEntity.realEndDate == null ? "" : format.format(mWXGDEntity.realEndDate));
        map.put("workRecord.periodUnit.id", mWXGDEntity.periodUnit != null ? mWXGDEntity.periodUnit.id : "");
        map.put("workRecord.periodUnit.value", mWXGDEntity.periodUnit != null ? mWXGDEntity.periodUnit.value : "");

        map.put("__file_upload", true);
        return map;
    }

    //维修人员转提交需要
    public static LinkedList<RepairStaffDto> translateStaffDto(List<RepairStaffEntity> staffs) {
        LinkedList<RepairStaffDto> staffSubmitEntities = new LinkedList<>();
        StaffDto staff;
        for (int i = 0; i < staffs.size(); i++) {
            RepairStaffDto repairStaffDto = new RepairStaffDto();
            repairStaffDto.id = staffs.get(i).id == null ? "" : String.valueOf(staffs.get(i).id);
            repairStaffDto.version = staffs.get(i).version == null ? "" : staffs.get(i).version;
            repairStaffDto.rowIndex = String.valueOf(i);

            staff = new StaffDto();
//            if (staffs.get(i).repairStaff == null) {
//                return null;
//            }

            staff.id = staffs.get(i).repairStaff == null ? "" : String.valueOf(staffs.get(i).repairStaff.id);
//            staff.name = staffs.get(i).repairStaff != null ? staffs.get(i).repairStaff.name : "";
            repairStaffDto.repairStaff = staff;
            repairStaffDto.startTime = staffs.get(i).startTime != null ? format.format(staffs.get(i).startTime) : "";
            repairStaffDto.endTime = staffs.get(i).endTime != null ? format.format(staffs.get(i).endTime) : "";
            repairStaffDto.workHour = staffs.get(i).workHour == null ? "" : String.valueOf(staffs.get(i).workHour);
            repairStaffDto.sort = staffs.get(i).sort == null ? "" : String.valueOf(staffs.get(i).sort);
            repairStaffDto.rowIndex = String.valueOf(i);
            repairStaffDto.remark = staffs.get(i).remark;
            staffSubmitEntities.add(repairStaffDto);
        }
        return staffSubmitEntities;
    }

    /**
     * @param
     * @return
     * @description 转化传输备件
     * @author zhangwenshuai1 2018/9/5
     */
    public static LinkedList<SparePartEntityDto> translateSparePartDto(List<SparePartEntity> list) {
        LinkedList<SparePartEntityDto> sparePartEntityDtos = new LinkedList<>();
        SparePartEntityDto sparePartEntityDto;
        GoodDto goodDto;
        SystemCodeEntity useState;
        String index;
        for (SparePartEntity sparePartEntity : list) {
            sparePartEntityDto = new SparePartEntityDto();
            sparePartEntityDto.id = sparePartEntity.id == null ? "" : String.valueOf(sparePartEntity.id);

//            if (sparePartEntity.productID == null) {
//                return null;
//            }
            goodDto = new GoodDto();
            goodDto.id = sparePartEntity.productID == null ? "" : String.valueOf(sparePartEntity.productID.id);
            sparePartEntityDto.productID = goodDto;
            sparePartEntityDto.checkbox = "true";
            sparePartEntityDto.version = sparePartEntity.version != null ? sparePartEntity.version : "";
            sparePartEntityDto.sum = sparePartEntity.sum == null ? "" : String.valueOf(sparePartEntity.sum);
            index = String.valueOf(list.indexOf(sparePartEntity));
            sparePartEntityDto.sort = index;
            sparePartEntityDto.rowIndex = index;
            sparePartEntityDto.remark = sparePartEntity.remark;
//            sparePartEntityDto.standingCrop = sparePartEntity.standingCrop == null ? "" : String.valueOf(sparePartEntity.standingCrop);
            sparePartEntityDto.useQuantity = sparePartEntity.useQuantity == null ? "" : String.valueOf(sparePartEntity.useQuantity);
            sparePartEntityDto.sparePartId = sparePartEntity.sparePartId == null ? "" : String.valueOf(sparePartEntity.sparePartId);
            sparePartEntityDto.actualQuantity = sparePartEntity.actualQuantity != null && sparePartEntity.actualQuantity.intValue() != 0 ? String.valueOf(sparePartEntity.actualQuantity)
                    : "";
            useState = new SystemCodeEntity();
            useState.id = sparePartEntity.useState == null ? "" : sparePartEntity.useState.id;
            sparePartEntityDto.useState = useState;


            sparePartEntityDtos.add(sparePartEntityDto);
        }

        return sparePartEntityDtos;
    }

    /**
     * @param
     * @return
     * @description 转化传输润滑油
     * @author zhangwenshuai1 2018/9/5
     */
    public static LinkedList<LubricateOilsEntityDto> translateLubricateOilsDto(List<LubricateOilsEntity> list) {
        LinkedList<LubricateOilsEntityDto> lubricateOilsEntityDtos = new LinkedList<>();
        LubricateOilsEntityDto lubricateOilsEntityDto;
        LubricateOilDto lubricateOilDto;
        SystemCodeEntity oilType;
        for (LubricateOilsEntity lubricateOilsEntity : list) {
            lubricateOilsEntityDto = new LubricateOilsEntityDto();
            lubricateOilsEntityDto.id = lubricateOilsEntity.id == null ? "" : String.valueOf(lubricateOilsEntity.id);
            lubricateOilsEntityDto.version = lubricateOilsEntity.version == null ? "" : lubricateOilsEntity.version;

//            if (lubricateOilsEntity.lubricate == null) {
//                return null;
//            }
            lubricateOilDto = new LubricateOilDto();
            lubricateOilDto.id = lubricateOilsEntity.lubricate == null ? "" : String.valueOf(lubricateOilsEntity.lubricate.id);
            lubricateOilsEntityDto.lubricate = lubricateOilDto;

            oilType = new SystemCodeEntity();
            oilType.id = lubricateOilsEntity.oilType == null ? "" : lubricateOilsEntity.oilType.id;
            lubricateOilsEntityDto.oilType = oilType;

            lubricateOilsEntityDto.oilQuantity = lubricateOilsEntity.oilQuantity == null ? "" : String.valueOf(lubricateOilsEntity.oilQuantity);
            String index = String.valueOf(list.indexOf(lubricateOilsEntity));
            lubricateOilsEntityDto.sort = index;
            lubricateOilsEntityDto.rowIndex = index;
            lubricateOilsEntityDto.remark = lubricateOilsEntity.remark;

            lubricateOilsEntityDtos.add(lubricateOilsEntityDto);
        }
        return lubricateOilsEntityDtos;
    }

    /**
     * @param
     * @return
     * @description 转化传输润滑油
     * @author zhangwenshuai1 2018/9/5
     */
    public static LinkedList<MaintainDto> translateMaintainDto(List<MaintainEntity> list) {
        LinkedList<MaintainDto> maintainDtos = new LinkedList<>();
        for (MaintainEntity maintainEntity : list) {
            MaintainDto maintainDto = new MaintainDto();

            ValueEntity jwxItemID = new ValueEntity();
            jwxItemID.id = Util.strFormat2(maintainEntity.getJwxItem().id);
            maintainDto.jwxItemID = jwxItemID;
            maintainDto.claim = maintainEntity.claim;
            maintainDto.sparePartName = maintainEntity.sparePartName;
            maintainDto.lastTime = maintainEntity.lastTime != null ? DateUtil.dateFormat(maintainEntity.lastTime) : "";
            maintainDto.nextTime = maintainEntity.nextTime != null ? DateUtil.dateFormat(maintainEntity.nextTime) : "";
            maintainDtos.add(maintainDto);
        }
        return maintainDtos;
    }

    /**
     * @param
     * @return
     * @description dataGrid删除数据id参数封装
     * @author zhangwenshuai1 2018/9/18
     */
    public static Map<String, Object> dgDeleted(Map<String, Object> map, List<Long> list, String dg) {
        if (list.size() <= 0)
            return map;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(",");
            map.put(dg + "DeletedIds[" + i + "]", list.get(i).toString());
        }
        map.put("dgDeletedIds['" + dg + "']", sb.substring(0, sb.length() - 1));
        return map;
    }
}

