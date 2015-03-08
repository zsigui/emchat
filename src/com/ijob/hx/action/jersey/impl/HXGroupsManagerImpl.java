package com.ijob.hx.action.jersey.impl;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ijob.hx.action.HXGroupsManager;
import com.ijob.hx.action.HXManager;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.constants.HXHTTPMethod;
import com.ijob.hx.jersy.JerseyWorker;
import com.ijob.hx.model.jersey.EndPoints;

public class HXGroupsManagerImpl extends HXGroupsManager {

	public static Logger L = LoggerFactory.getLogger(HXGroupsManagerImpl.class);

	@Override
	public ObjectNode getAllGroupIds() {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME).resolveTemplate(
					"app_name", HXConstants.APP_NAME);
			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode getGroupDetailsByGroupId(String[] groupIds) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (groupIds == null || groupIds.length == 0) {
			L.error("Property that named groupname must be provided .");
			objectNode.put("error", "Property that named groupname must be provided .");
			return objectNode;
		}

		StringBuilder groups = new StringBuilder("");
		for (String s : groupIds) {
			groups.append(s).append(",");
		}
		groups.deleteCharAt(groups.length() - 1);

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(groups.toString());
			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode createGroup(ObjectNode groupDetails) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (!groupDetails.has("groupname")) {
			L.error("Property that named groupname must be provided .");
			objectNode.put("error", "Property that named groupname must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("desc")) {
			L.error("Property that named desc must be provided .");
			objectNode.put("error", "Property that named desc must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("public")) {
			L.error("Property that named public must be provided .");
			objectNode.put("error", "Property that named public must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("approval")) {
			L.error("Property that named approval must be provided .");
			objectNode.put("error", "Property that named approval must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("owner")) {
			L.error("Property that named owner must be provided .");
			objectNode.put("error", "Property that named owner must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("members") || !groupDetails.path("members").isArray()) {
			L.error("Property that named members must be provided .");
			objectNode.put("error", "Property that named members must be provided .");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME).resolveTemplate(
					"app_name", HXConstants.APP_NAME);
			objectNode = JerseyWorker.sendRequest(webTarget, groupDetails, HXManager.sCredential,
					HXHTTPMethod.METHOD_POST, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode updateGroupInfo(String groupId, ObjectNode updateBody) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();
		// check properties that must be provided
		if (updateBody == null
				|| (!updateBody.has("description") && !updateBody.has("groupname") && !updateBody.has("maxusers"))) {
			L.error("Property that named description, groupname or maxusers must be provided at least one.");
			objectNode.put("error",
					"Property that named description, groupname or maxusers must be provided at least one.");
			return objectNode;
		}
		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(groupId);
			objectNode = JerseyWorker.sendRequest(webTarget, updateBody, HXManager.sCredential,
					HXHTTPMethod.METHOD_PUT, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode deleteGroup(String groupId) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(groupId);
			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_DELETE,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode getAllMembersByGroupId(String groupId) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(groupId).path("users");
			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode addUserToGroup(String groupId, String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(groupId).path("users").path(username);
			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_POST,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode addUsersToGroup(String groupId, ObjectNode usernames) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (!usernames.has("usernames")) {
			L.error("Property that named usernames must be provided .");
			objectNode.put("error", "Property that named usernames must be provided .");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(groupId).path("users");
			objectNode = JerseyWorker.sendRequest(webTarget, usernames, HXManager.sCredential,
					HXHTTPMethod.METHOD_POST, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode deleteUserFromGroup(String groupId, String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(groupId).path("users").path(username);
			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_DELETE,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode getAllGroupsByUser(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();
		if (StringUtils.isEmpty(username)) {
			L.error("Property that named username must be provided .");
			objectNode.put("error", "Property that named username must be provided .");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(username).path("joined_chatgroups");
			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	public static void main(String[] args) {

		/**
		 * 创建群组 curl示例 curl -X POST
		 * 'https://a1.easemob.com/easemob-playground/test1/chatgroups' -H
		 * 'Authorization: Bearer {token}' -d
		 * '{"groupname":"测试群组","desc":"测试群组","public":true,"approval":true,"owner":"xiaojianguo001","maxusers":333,"members":["xiaojianguo002","xiaojianguo0
		 * 0 3 " ] } '
		 */
		ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
		dataObjectNode.put("groupname", "测试群组");
		dataObjectNode.put("desc", "测试群组");
		dataObjectNode.put("approval", true);
		dataObjectNode.put("public", true);
		dataObjectNode.put("maxusers", 333);
		dataObjectNode.put("owner", "k2015030700049");
		ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
		arrayNode.add("kenshinnuser100");
		arrayNode.add("k2015030700027");
		dataObjectNode.put("members", arrayNode);
		ObjectNode creatChatGroupNode = HXManager.sGroupManager.createGroup(dataObjectNode);
		System.out.println("创建群组：" + creatChatGroupNode.toString());

		/**
		 * 修改群组信息 curl示例 curl -X POST
		 * 'https://a1.easemob.com/easemob-playground/test1/chatgroups' -H
		 * 'Authorization: Bearer {token}' -d
		 * '{"groupname":"测试群组-修改","desc":"测试群组-修改","maxusers":333} } '
		 */
		dataObjectNode.removeAll();
		// ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
		dataObjectNode.put("groupname", "测试群组-更新");
		dataObjectNode.put("description", "测试群组-更新");
		dataObjectNode.put("maxusers", 153);
		String groupid = creatChatGroupNode.get("data").get("groupid").asText();
		// String groupid = "1425746239521624";
		ObjectNode updateChatGroupNode = HXManager.sGroupManager.updateGroupInfo(groupid, dataObjectNode);
		System.out.println("修改群组：" + updateChatGroupNode.toString());

		/**
		 * 获取APP中所有的群组ID curl示例: curl -X GET -i
		 * "https://a1.easemob.com/easemob-playground/test1/chatgroups" -H
		 * "Authorization: Bearer {token}"
		 */
		ObjectNode chatgroupidsNode = HXManager.sGroupManager.getAllGroupIds();
		System.out.println("获取群组ID：" + chatgroupidsNode.toString());

		/**
		 * 获取一个或者多个群组的详情 curl示例 curl -X GET -i
		 * "https://a1.easemob.com/easemob-playground/test1/chatgroups/1414379474926191,1405735927133519"
		 * -H "Authorization: Bearer {token}"
		 */
		String[] chatgroupIDs = { groupid };
		ObjectNode groupDetailNode = HXManager.sGroupManager.getGroupDetailsByGroupId(chatgroupIDs);
		System.out.println("获取群组详情：" + groupDetailNode.toString());

		/**
		 * 在群组中添加一个人 curl示例 curl -X POST
		 * 'https://a1.easemob.com/easemob-playground/test1/chatgroups/1405735927133519/users/xiaojiangu
		 * o 0 0 2 ' -H 'Authorization: Bearer {token}'
		 */
		String toAddUsername = "k2015030600014";
		ObjectNode addUserToGroupNode = HXManager.sGroupManager.addUserToGroup(groupid, toAddUsername);
		System.out.println("添加一人到群组：" + addUserToGroupNode.toString());

		/**
		 * 群组批量添加成员 curl示例 curl -X POST -i
		 * 'https://a1.easemob.com/easemob-playground/test1/chatgroups/1405735927133519/u
		 * s e r s ' -H 'Authorization: Bearer {token}' -d
		 * '{"usernames":["xiaojianguo002","xiaojianguo003"]}'
		 */
		ArrayNode usernames = JsonNodeFactory.instance.arrayNode();
		usernames.add("k2015030700020");
		usernames.add("k2015030700016");
		usernames.add("k2015030600014");
		ObjectNode usernamesNode = JsonNodeFactory.instance.objectNode();
		usernamesNode.put("usernames", usernames);
		ObjectNode addUserToGroupBatchNode = HXManager.sGroupManager.addUsersToGroup(groupid, usernamesNode);
		System.out.println("添加多人到群组：" + addUserToGroupBatchNode.toString());

		/**
		 * 获取群组中的所有成员 curl示例 curl -X GET
		 * 'https://a1.easemob.com/easemob-playground/test1/chatgroups/1405735927133519/u
		 * s e r s ' -H 'Authorization: Bearer {token}'
		 */
		ObjectNode getAllMemberssByGroupIdNode = HXManager.sGroupManager.getAllMembersByGroupId(groupid);
		System.out.println("获得群组所有成员：" + getAllMemberssByGroupIdNode.toString());

		/**
		 * 获取一个用户参与的所有群组 curl示例 curl -X GET
		 * 'https://a1.easemob.com/easemob-playground/test1/users/xiaojianguo002/joined_chatgr
		 * o u p s ' -H 'Authorization: Bearer {token}'
		 */
		String username = "k2015030700020";
		ObjectNode getJoinedChatgroupsForIMUserNode = HXManager.sGroupManager.getAllGroupsByUser(username);
		System.out.println("获得一个用户所有群组：" + getJoinedChatgroupsForIMUserNode.toString());

		/**
		 * 在群组中减少一个人 curl示例 curl -X DELETE
		 * 'https://a1.easemob.com/easemob-playground/test1/chatgroups/1405735927133519/users/xiaojiangu
		 * o 0 0 2 ' -H 'Authorization: Bearer {token}'
		 */
		String toRemoveUsername = "k2015030600014";
		ObjectNode deleteUserFromGroupNode = HXManager.sGroupManager.deleteUserFromGroup(groupid, toRemoveUsername);
		System.out.println("从群组删除一人：" + deleteUserFromGroupNode.toString());

		/**
		 * 删除群组 curl示例 curl -X DELETE
		 * 'https://a1.easemob.com/easemob-playground/test1/chatgroups/140573592713
		 * 3 5 1 9 ' -H 'Authorization: Bearer {token}'
		 */
		 ObjectNode deleteChatGroupNode =
		 HXManager.sGroupManager.deleteGroup(groupid);
		 System.out.println("删除群组：" + deleteChatGroupNode.toString());
		 
	}
}
