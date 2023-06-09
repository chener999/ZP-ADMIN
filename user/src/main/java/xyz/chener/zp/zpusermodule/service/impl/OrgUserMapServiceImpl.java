package xyz.chener.zp.zpusermodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import xyz.chener.zp.common.utils.AssertUrils;
import xyz.chener.zp.zpusermodule.dao.OrgUserMapDao;
import xyz.chener.zp.zpusermodule.entity.OrgBase;
import xyz.chener.zp.zpusermodule.entity.OrgUserMap;
import xyz.chener.zp.zpusermodule.entity.UserBase;
import xyz.chener.zp.zpusermodule.entity.UserLoginEventRecord;
import xyz.chener.zp.zpusermodule.entity.dto.OrgExtendInfoDto;
import xyz.chener.zp.zpusermodule.entity.dto.OrgUserDto;
import xyz.chener.zp.zpusermodule.error.org.OrgNotFoundError;
import xyz.chener.zp.zpusermodule.service.OrgUserMapService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (OrgUserMap)表服务实现类
 *
 * @author makejava
 * @since 2023-02-21 11:55:56
 */
@Service
public class OrgUserMapServiceImpl extends ServiceImpl<OrgUserMapDao, OrgUserMap> implements OrgUserMapService {

    private final UserLoginEventRecordServiceImpl userLoginEventRecordService;
    private final OrgBaseServiceImpl orgBaseService;
    private final UserBaseServiceImpl userBaseService;

    public OrgUserMapServiceImpl(UserLoginEventRecordServiceImpl userLoginEventRecordService, OrgBaseServiceImpl orgBaseService, UserBaseServiceImpl userBaseService) {
        this.userLoginEventRecordService = userLoginEventRecordService;
        this.orgBaseService = orgBaseService;
        this.userBaseService = userBaseService;
    }

    @Override
    public OrgExtendInfoDto getOrgExtendInfo(Long id) {
        List<OrgUserMap> users = lambdaQuery().eq(OrgUserMap::getOrgId, id)
                .select(OrgUserMap::getUserId).list();
        OrgExtendInfoDto res = new OrgExtendInfoDto();
        res.setUserCount(users.size());
        res.setActiveUserCount(0);
        if (users.size()>0){
            Long count = userLoginEventRecordService.lambdaQuery()
                    .in(UserLoginEventRecord::getUserId, users)
                    .ge(UserLoginEventRecord::getTime, new Date())
                    .count();
            res.setActiveUserCount(count.intValue());
        }
        Long count1 = orgBaseService.lambdaQuery()
                .eq(OrgBase::getParentId, id).count();
        res.setSubOrgCount(count1.intValue());
        return res;
    }

    @Override
    public PageInfo<OrgUserDto> getOrgUsers(Long id, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<OrgUserDto> orgUserDtos = baseMapper.getOrgUsers(id);
        return new PageInfo<>(orgUserDtos);
    }

    @Override
    public Boolean addOrgUser(Long orgId, List<Long> userIds) {
        ArrayList<OrgUserMap> ous = new ArrayList<>();
        userIds.forEach(e->{
            OrgUserMap ou = new OrgUserMap();
            ou.setOrgId(orgId);
            ou.setUserId(e);
            ou.setAuthDisable(false);
            ou.setBindTime(new Date());
            ous.add(ou);
        });
        this.saveBatch(ous);
        return true;
    }

    @Override
    public Boolean deleteOrgUser(Long orgId, List<Long> userIds) {
        userIds.forEach(e->{
            userBaseService.lambdaUpdate().set(UserBase::getRoleId, null)
                    .eq(UserBase::getId, e).update();
            this.lambdaUpdate().eq(OrgUserMap::getOrgId, orgId)
                    .eq(OrgUserMap::getUserId, e).remove();
        });
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean flushOrgUserAuth(Long orgId) {
        OrgBase orgBase = orgBaseService.lambdaQuery().select(OrgBase::getRoleId, OrgBase::getId)
                .eq(OrgBase::getId, orgId).one();
        AssertUrils.state(orgBase!=null, OrgNotFoundError.class);
        List<Long> users = this.lambdaQuery().select(OrgUserMap::getUserId)
                .eq(OrgUserMap::getOrgId, orgId).eq(OrgUserMap::getAuthDisable, false)
                .list().stream().map(OrgUserMap::getUserId).toList();
        return userBaseService.lambdaUpdate().set(UserBase::getRoleId, orgBase.getRoleId())
                .in(UserBase::getId, users)
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean disableUserAuth(Long orgId, Long userIds,Boolean disable) {
        OrgBase orgBase = orgBaseService.lambdaQuery().select(OrgBase::getRoleId, OrgBase::getId)
                .eq(OrgBase::getId, orgId).one();
        AssertUrils.state(orgBase!=null, OrgNotFoundError.class);
        this.lambdaUpdate().eq(OrgUserMap::getOrgId, orgId)
                .eq(OrgUserMap::getUserId, userIds)
                .set(OrgUserMap::getAuthDisable, disable).update();
        userBaseService.lambdaUpdate().set(UserBase::getRoleId, disable?null:orgBase.getRoleId())
                .eq(UserBase::getId, userIds)
                .update();
        return true;
    }
}

