package com.zdy.biz.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.zdy.biz.sysuserrole.dao.ISysUserRoleDao;
import com.zdy.biz.user.dao.IUserDao;
import com.zdy.dubbo.api.service.user.IUserService;
import com.zdy.dubbo.common.util.BaseList;
import com.zdy.dubbo.common.util.ServiceResult;
import com.zdy.dubbo.dto.user.CreateUserReq;
import com.zdy.dubbo.dto.user.ModifyUserReq;
import com.zdy.dubbo.dto.user.UserReq;
import com.zdy.dubbo.dto.user.UserResp;
import com.zdy.dubbo.model.user.User;
import com.zdy.dubbo.model.userrole.SysUserRole;

import annotation.ReadDataSource;
import annotation.WriteDataSource;

@Service("userService")
public class IUserServiceImpl implements IUserService {

	@Resource
	private IUserDao userDao;

	@Resource
	private RedisTemplate redisTemplate;
	
	@Resource
	private ISysUserRoleDao sysUserRoleDao; 	

	//@WriteDataSource
	// @ReadDataSource
	@ReadDataSource
	@Override
	public ServiceResult<BaseList<UserResp>> findUserListByPageNo(UserReq req) {
		ServiceResult<BaseList<UserResp>> result = new ServiceResult<BaseList<UserResp>>();
		if (req == null) {
			return result.error("查询条件不能为空");
		}

		List<User> baseList = userDao.findList(req.toUser());
		int totalRows = userDao.count(req.toUser());
		int pageSize = req.getPageSize();
		int totalPage = totalRows % pageSize > 0 ? totalRows / pageSize + 1 : totalRows / pageSize + 1;

		List<UserResp> list = new ArrayList<UserResp>();
		if (!CollectionUtils.isEmpty(baseList)) {
			for (User temp : baseList) {
				if (temp == null) {
					continue;
				}
				list.add(new UserResp(temp));
			}
		}

		BaseList<UserResp> userList = new BaseList<UserResp>();
		userList.setList(list);
		userList.setCurPage(req.getPage());
		userList.setPageSize(pageSize);
		userList.setTotalPage(totalPage);
		userList.setTotalRows(totalRows);
		return result.success(userList);
	}
	
	@Transactional
	@WriteDataSource
	@Override
	public ServiceResult<UserResp> save(CreateUserReq req) {
		ServiceResult<UserResp> result = new ServiceResult<UserResp>();
		if (req == null) {
			return result.error("保存对象不能为空");
		}
		User d = req.toUser();

		int count = userDao.save(d);
		if (count == 0) {
			return result.error("保存失败");
		}
		return result.success();
	}
	@Transactional
	@WriteDataSource
	@Override
	public ServiceResult<UserResp> delete(UserReq req) {
		ServiceResult<UserResp> result = new ServiceResult<UserResp>();
		if (req == null || ObjectUtils.isEmpty(req.getIds())) {
			return result.error("删除条件不能为空");
		}
		Long[] userIdArray = req.getIds();
		for(Long ids : userIdArray){
			sysUserRoleDao.deleteByUserId(ids);
		}
		int count = userDao.delete(req.toUser());
		
		if (count == 0) {
			return result.error("删除失败");
		}
		return result.success();
	}
	
	@Transactional
	@WriteDataSource
	@Override
	public ServiceResult<UserResp> update(ModifyUserReq req) {

		ServiceResult<UserResp> result = new ServiceResult<UserResp>();
		if (req == null) {
			return result.error("修改条件不能为空");
		}
		int count = 0;
		count = userDao.update(req.toUser());
		if(req.getUserValidate() != null){
			// 用户审核
			SysUserRole userRole = new SysUserRole();
			userRole.setSysUserId(req.getId());
			userRole.setSysRoleId(18L);
			userRole.setCustId(1L);
			sysUserRoleDao.save(userRole);
		}
		if (count == 0) {
			return result.error("修改失败");
		}
		return result.success();
	}

	@Override
	public ServiceResult<UserResp> deleteById(UserReq req) {

		return null;
	}
	@ReadDataSource
	@Override
	public ServiceResult<UserResp> getById(UserReq req) {
		ServiceResult<UserResp> result = new ServiceResult<UserResp>();
		if (req == null || req.getId() == null) {
			return result.error("查询条件不能为空");
		}
		User user = userDao.getById(req.getId());
		if (user == null || user.getId() == null) {
			return result.error("获取失败");
		}
		return result.success(new UserResp(user));
	}
	@ReadDataSource
	@Override
	public ServiceResult<UserResp> login(UserReq req) {
		ServiceResult<UserResp> result = new ServiceResult<UserResp>();
		if (req == null) {
			return result.error("登录人信息不能为空");
		}
		User user = userDao.fetch(req.toUser());
		if (user == null || StringUtils.isEmpty(user.getId())) {
			return result.success(null);
		}
//		String key = "user_" + user.getId();
//		ValueOperations<String, UserResp> operations = redisTemplate.opsForValue();
//		// 缓存存在
//		boolean hasKey = redisTemplate.hasKey(key);
//		UserResp userResp = null;
//		if (hasKey) {
//			userResp = operations.get(key);
//		}else{
		UserResp userResp = new UserResp(user);
//			operations.set(key, userResp);
//		}
		return result.success(userResp);
	}
	@ReadDataSource
	@Override
	public ServiceResult<UserResp> fetch(UserReq req) {

		ServiceResult<UserResp> result = new ServiceResult<UserResp>();
		if (req == null) {
			return result.error("查询信息不能为空");
		}
		User user = userDao.fetch(req.toUser());
		if (user == null || StringUtils.isEmpty(user.getId())) {
			return result.success(null);
		}

		return result.success(new UserResp(user));

	}

}
