package xyz.chener.zp.zpusermodule.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import xyz.chener.zp.common.config.query.entity.FieldQuery;
import xyz.chener.zp.common.config.query.annotation.QueryTableName;
import xyz.chener.zp.common.config.unifiedReturn.annotation.EncryField;
import xyz.chener.zp.common.config.unifiedReturn.encry.encryProcess.impl.DefaultDesensitizationEncry;
import xyz.chener.zp.common.error.HttpErrorException;
import xyz.chener.zp.common.utils.AssertUrils;
import xyz.chener.zp.zpusermodule.entity.Role;
import xyz.chener.zp.zpusermodule.service.RoleService;

import java.util.Date;

public class UserAllInfoDto extends FieldQuery {

    @QueryTableName("user_base")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @QueryTableName("user_base")
    private String username;


    @QueryTableName("user_base")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    @QueryTableName("user_base")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTimeEnd;

    @QueryTableName("user_base")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @QueryTableName("user_base")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;

    //0 is false
    @QueryTableName("user_base")
    private Integer disable;

    @QueryTableName("user_base")
    private String lastLoginIp;

    @QueryTableName("user_base")
    private String lastLoginOs;

    @QueryTableName("user_base")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    @QueryTableName("user_base")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;


    @QueryTableName("user_extend")
    private String email;

    @QueryTableName("user_extend")
    private String phone;
    //上级userid
    @QueryTableName("user_extend")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long superUserId;
    //职务
    @QueryTableName("user_extend")
    private String post;
    //签名
    @QueryTableName("user_extend")
    private String autograph;
    //简介
    @QueryTableName("user_extend")
    private String introduce;

    @QueryTableName("user_extend")
    private String avatarId;

    @QueryTableName("user_extend")
    private String nameCn;

    private String roleName;

    public UserAllInfoDto addRoleName(RoleService roleService)
    {
        AssertUrils.state(this.id != null
                ,new HttpErrorException("Add role, id cannot be null! Tips:addRoleName should be after addUserBase"));
        Role role = roleService.lambdaQuery().eq(Role::getId, this.roleId).one();
        if (role != null)
            this.roleName = role.getRoleName();
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public Date getExpireTimeEnd() {
        return expireTimeEnd;
    }

    public void setExpireTimeEnd(Date expireTimeEnd) {
        this.expireTimeEnd = expireTimeEnd;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDisable() {
        return disable;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginOs() {
        return lastLoginOs;
    }

    public void setLastLoginOs(String lastLoginOs) {
        this.lastLoginOs = lastLoginOs;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getSuperUserId() {
        return superUserId;
    }

    public void setSuperUserId(Long superUserId) {
        this.superUserId = superUserId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }
}
