package com.ijob.hx.action;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class HXIMUserManager {

	/**
	 * 注册IM用户[单个]
	 * 
	 * @param userData
	 * @return
	 */
	public abstract ObjectNode createNewIMUser(ObjectNode userData);

	/**
	 * 注册IM用户[批量]
	 * 
	 * @param usersData
	 * @return
	 */
	public abstract ObjectNode createNewIMUsers(ArrayNode usersData);

	/**
	 * 定义用户名前缀，批量注册IM用户
	 * 
	 * @param usernamePrefix
	 *            用户名前缀
	 * @param perNumber
	 *            批量注册时一次注册数量，建议20，最大不超过50
	 * @param totalNumber
	 *            总的注册用户数
	 * @return
	 */
	public abstract ObjectNode createNewIMUsersByPrefix(String usernamePrefix, Long perNumber, Long totalNumber);

	/**
	 * 获取IM用户[单个]
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode getIMUser(String username);

	/**
	 * 获取最近注册的IM用户[批量]
	 * 
	 * @param limit
	 *            指定获取数量
	 * @param cursor
	 *            下一页指针
	 * @return
	 */
	public abstract ObjectNode getIMUsersRecently(int limit, String cursor);

	/**
	 * 删除IM用户[单个]
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode deleteIMUser(String username);

	/**
	 * 删除指定时间段注册的IM用户[批量]
	 * 
	 * @param limit
	 * @param queryStr
	 *            时间段查询语句：ql=created> {起始时间戳} and created < {结束时间戳}
	 * @return
	 */
	public abstract ObjectNode deleteIMUser(int limit, String queryStr);

	/**
	 * 重置IM用户密码
	 * 
	 * @param username
	 * @param newPwd
	 */
	public abstract ObjectNode modifyIMUserPwd(String username, ObjectNode newPwd);

	/**
	 * 修改用户昵称
	 * 
	 * @param username
	 * @param newNickName
	 * @return
	 */
	public abstract ObjectNode modifyUserNickname(String username, ObjectNode newNickName);

	/**
	 * 添加好友
	 * 
	 * @param owneruser
	 * @param frienduser
	 */
	public abstract ObjectNode addIMFriend(String owneruser, String frienduser);

	/**
	 * 删除好友
	 * 
	 * @param owneruser
	 * @param frienduser
	 * @return
	 */
	public abstract ObjectNode deleteIMFriend(String owneruser, String frienduser);

	/**
	 * 查看好友信息
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode getIMFriends(String username);

	/**
	 * 获取黑名单信息
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode getBlockIMFriends(String username);

	/**
	 * 往黑名单中添加用户
	 * 
	 * @param owneruser
	 * @param blockusers
	 * @return
	 */
	public abstract ObjectNode addBlockIMFriend(String owneruser, ObjectNode blockusers);
	
	/**
	 * 从黑名单删除用户[单个]
	 * 
	 * @param owneruser
	 * @param blockuser
	 * @return
	 */
	public abstract ObjectNode deleteBlockIMFriend(String owneruser, String blockuser);

	/**
	 * 用户登陆
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public abstract ObjectNode userLogin(String username, String password);

	/**
	 * 查看用户在线状态
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode getUserStatus(String username);

	/**
	 * 查询离线消息数量
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode getOfflineMsgCount(String username);

	/**
	 * 查询某条离线消息状态
	 * 
	 * @param username
	 * @param msgId
	 * @return
	 */
	public abstract ObjectNode getOfflineMsgStatus(String username, String msgId);

	/**
	 * 用户账号禁用
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode deactivateIMUser(String username);

	/**
	 * 用户账号解禁
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode activateIMUser(String username);
}
