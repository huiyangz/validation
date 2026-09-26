package com.dcits.validation.service.components;

import com.dcits.validation.enums.AcctCcy;
import com.dcits.validation.enums.AcctNatureNo;
import com.dcits.validation.enums.AcctRiskLevel;
import com.dcits.validation.enums.AcctStatus;
import com.dcits.validation.enums.AcctVerifyFlag;
import com.dcits.validation.enums.AcctVerifyResult;
import com.dcits.validation.enums.AllDepInd;
import com.dcits.validation.enums.AllDraInd;
import com.dcits.validation.enums.AllDraRange;
import com.dcits.validation.enums.AnnualStatus;
import com.dcits.validation.enums.AutoRenewInd;
import com.dcits.validation.enums.BalType;
import com.dcits.validation.enums.CheckCertificateType;
import com.dcits.validation.enums.ClientType;
import com.dcits.validation.enums.DepositNature;
import com.dcits.validation.enums.FarmerFlag;
import com.dcits.validation.enums.FixedCall;
import com.dcits.validation.enums.IntIndFlag;
import com.dcits.validation.enums.ManageType;
import com.dcits.validation.enums.OsaFlag;
import com.dcits.validation.enums.RbAcctType;
import com.dcits.validation.enums.RbBusAcctPurpose;
import com.dcits.validation.enums.RenewMethod;
import com.dcits.validation.enums.SimpleAcct;
import com.dcits.validation.enums.SourceType;
import com.dcits.validation.enums.SpecAcctFlag;
import com.dcits.validation.enums.TermType;
import com.dcits.validation.enums.TranBranch;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.dcits.validation.entity.RbBusAcct;
import com.dcits.validation.entity.RbBusAcctExample;
import com.dcits.validation.facade.components.IRbBusAcctBcc;
import com.dcits.validation.facade.eo.RbBusAcctEO;
import com.dcits.validation.repo.RbBusAcctMapper;
import com.dcits.validation.service.utils.RbBusAcctValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RbBusAcctBasisCpnt implements IRbBusAcctBcc {
    @Autowired
    RbBusAcctMapper rbBusAcctMapper;

    @Override
    public long countByEo(RbBusAcctEO eo) {
        RbBusAcctExample example = RbBusAcctValueUtil.eoToEntityExample(eo);
        return rbBusAcctMapper.countByExample(example);
    }

    @Override
    public int removeByEo(RbBusAcctEO eo) {
        RbBusAcctExample example = RbBusAcctValueUtil.eoToEntityExample(eo);
        return rbBusAcctMapper.deleteByExample(example);
    }

    @Override
    public int removeByPrimaryKey(Integer internalKey) {
        return rbBusAcctMapper.deleteByPrimaryKey(internalKey);
    }

    @Override
    public int create(RbBusAcctEO eo) {
        RbBusAcct row = RbBusAcctValueUtil.eoToEntity(eo);
        return rbBusAcctMapper.insert(row);
    }

    @Override
    public int createSelective(RbBusAcctEO eo) {
        RbBusAcct row = RbBusAcctValueUtil.eoToEntity(eo);
        return rbBusAcctMapper.insertSelective(row);
    }

    @Override
    public List<RbBusAcctEO> findByEo(RbBusAcctEO eo) {
        RbBusAcctExample example = RbBusAcctValueUtil.eoToEntityExample(eo);
        List<RbBusAcctEO> result = new ArrayList<>();
        List<RbBusAcct> dbResult = rbBusAcctMapper.selectByExample(example);
        for (RbBusAcct item : dbResult) {
            result.add(RbBusAcctValueUtil.entityToEo(item));
        }
        return result;
    }

    @Override
    public RbBusAcctEO findByPrimaryKey(Integer internalKey) {
        return RbBusAcctValueUtil.entityToEo(rbBusAcctMapper.selectByPrimaryKey(internalKey));
    }

    @Override
    public int modifyByPrimaryKeySelective(RbBusAcctEO eo) {
        RbBusAcct row = RbBusAcctValueUtil.eoToEntity(eo);
        return rbBusAcctMapper.updateByPrimaryKeySelective(row);
    }

    @Override
    public int modifyByPrimaryKey(RbBusAcctEO eo) {
        RbBusAcct row = RbBusAcctValueUtil.eoToEntity(eo);
        return rbBusAcctMapper.updateByPrimaryKey(row);
    }
}