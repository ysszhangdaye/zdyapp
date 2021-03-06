package com.zdy.dubbo.dto.userrole;

import java.io.Serializable;

import com.zdy.dubbo.model.userrole.SysUserRole;

public class SysUserRoleResp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * id       db_column: id 
     */	
	private Long id;
    /**
     * sysUserId       db_column: sys_user_id 
     */	
	private Long sysUserId;
    /**
     * sysRoleId       db_column: sys_role_id 
     */	
	private Long sysRoleId;
	
	private Long custId;

	public SysUserRoleResp(){
		
	}
	
	public SysUserRoleResp(SysUserRoleResp sysUserRoleResp){
		if(null != sysUserRoleResp){
				this.setId(sysUserRoleResp.getId());
				this.setSysUserId(sysUserRoleResp.getSysUserId());
				this.setSysRoleId(sysUserRoleResp.getSysRoleId());
				this.setCustId(sysUserRoleResp.getCustId());
		}
	}
	
	public SysUserRoleResp(SysUserRole sysUserRole){
		if(null != sysUserRole){
				this.id = sysUserRole.getId();
				this.sysUserId = sysUserRole.getSysUserId();
				this.sysRoleId = sysUserRole.getSysRoleId();
				this.custId = sysUserRole.getCustId();
		}
	}
	
	public SysUserRole toSysUserRole(){
		SysUserRole  sysUserRole = new SysUserRole();
		sysUserRole.setId(this.id);
		sysUserRole.setSysUserId(this.sysUserId);
		sysUserRole.setSysRoleId(this.sysRoleId);
		sysUserRole.setCustId(this.custId);
		return sysUserRole;
	}
	
		
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
		
	public void setSysUserId(Long value) {
		this.sysUserId = value;
	}
	
	public Long getSysUserId() {
		return this.sysUserId;
	}
		
	public void setSysRoleId(Long value) {
		this.sysRoleId = value;
	}
	
	public Long getSysRoleId() {
		return this.sysRoleId;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

 
}

