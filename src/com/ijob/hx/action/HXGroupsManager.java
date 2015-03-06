package com.ijob.hx.action;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class HXGroupsManager {

	/**
	 * 获取所有群组ID
	 * 
	 * @return
	 */
	public abstract ObjectNode getAllGroupIds();

	/**
	 * 获取所有群组基本信息
	 * 
	 * @return
	 */
	public abstract ObjectNode getAllGroupInfo();

	/**
	 * 获取一个或多个群组详情信息
	 * 
	 * @param groupIds
	 * @return
	 */
	public abstract ObjectNode getGroupDetailsByGroupId(String[] groupIds);

	/**
	 * 创建一个群组
	 * 
	 * @param groupDetails
	 * @return
	 */
	public abstract ObjectNode createGroup(ObjectNode groupDetails);

	/**
	 * 修改群组信息 <br />
	 * body只接收gorupname, description, maxusers
	 * 
	 * @param updateBody
	 * @return
	 */
	public abstract ObjectNode updateGroupInfo(ObjectNode updateBody);

	/**
	 * 删除群组
	 * 
	 * @param groupId
	 * @return
	 */
	public abstract ObjectNode deleteGroup(ObjectNode groupId);

	/**
	 * 获取群组中所有成员
	 * 
	 * @param groupId
	 * @return
	 */
	public abstract ObjectNode getAllMembersByGroupId(String groupId);

	/**
	 * 群组加入[单个]
	 * 
	 * @param groupid
	 * @param username
	 * @return
	 */
	public abstract ObjectNode addUserToGroup(String groupId, String username);

	/**
	 * 群组加人[批量]
	 * 
	 * @param groupId
	 * @param usernames
	 * @return
	 */
	public abstract ObjectNode addUsersToGroup(String groupId,
			ObjectNode usernames);

	/**
	 * 群组减人[单个]
	 * 
	 * @param groupId
	 * @param username
	 * @return
	 */
	public abstract ObjectNode deleteUserFromGroup(String groupId,
			String username);

	/**
	 * 获取一个用户参与的所有群组
	 * 
	 * @param username
	 * @return
	 */
	public abstract ObjectNode getAllGroupsByUser(String username);
}
