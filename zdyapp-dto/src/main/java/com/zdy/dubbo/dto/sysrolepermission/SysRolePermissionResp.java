package com.zdy.dubbo.dto.sysrolepermission;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.zdy.dubbo.model.sysrolepermission.SysRolePermission;

public class SysRolePermissionResp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * id       db_column: id 
     */	
	private Long id;
    /**
     * 角色id       db_column: sys_role_id 
     */	
	private Long sysRoleId;
    /**
     * 权限id       db_column: sys_permission_id 
     */	
	private Long sysPermissionId;

	public SysRolePermissionResp(){
		
	}
	
	public SysRolePermissionResp(SysRolePermissionResp sysRolePermissionResp){
		if(null != sysRolePermissionResp){
				this.setId(sysRolePermissionResp.getId());
				this.setSysRoleId(sysRolePermissionResp.getSysRoleId());
				this.setSysPermissionId(sysRolePermissionResp.getSysPermissionId());
		}
	}
	
	public SysRolePermissionResp(SysRolePermission sysRolePermission){
		if(null != sysRolePermission){
				this.id = sysRolePermission.getId();
				this.sysRoleId = sysRolePermission.getSysRoleId();
				this.sysPermissionId = sysRolePermission.getSysPermissionId();
		}
	}
	
	public SysRolePermission toSysRolePermission(){
		SysRolePermission  sysRolePermission = new SysRolePermission();
		sysRolePermission.setId(this.id);
		sysRolePermission.setSysRoleId(this.sysRoleId);
		sysRolePermission.setSysPermissionId(this.sysPermissionId);
		return sysRolePermission;
	}
		
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
		
	public void setSysRoleId(Long value) {
		this.sysRoleId = value;
	}
	
	public Long getSysRoleId() {
		return this.sysRoleId;
	}
		
	public void setSysPermissionId(Long value) {
		this.sysPermissionId = value;
	}
	
	public Long getSysPermissionId() {
		return this.sysPermissionId;
	}

 
}

