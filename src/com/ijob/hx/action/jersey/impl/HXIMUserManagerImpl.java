package com.ijob.hx.action.jersey.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ijob.hx.action.HXIMUserManager;
import com.ijob.hx.action.HXManager;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.constants.HXHTTPMethod;
import com.ijob.hx.jersy.JerseyWorker;
import com.ijob.hx.model.jersey.EndPoints;

/**
 * 环信用户体系集成Jersey2.9实现
 * 
 * @author zsigui
 * 
 */
public class HXIMUserManagerImpl extends HXIMUserManager {

	public static Logger L = LoggerFactory.getLogger(HXIMUserManagerImpl.class);

	@Override
	public ObjectNode createNewIMUser(ObjectNode userData) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (null != userData && !userData.has("username")) {
			L.error("Property that named username must be provided .");
			objectNode.put("error", "Property that named username must be provided .");
			return objectNode;
		}
		if (null != userData && !userData.has("password")) {
			L.error("Property that named password must be provided .");
			objectNode.put("error", "Property that named password must be provided .");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME).resolveTemplate(
					"app_name", HXConstants.APP_NAME);
			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(new BasicNameValuePair("Content-Type", "application/json"));
			objectNode = JerseyWorker.sendRequest(webTarget, userData, HXManager.sCredential, HXHTTPMethod.METHOD_POST,
					headers);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode createNewIMUsers(ArrayNode usersData) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (usersData.isArray()) {
			for (JsonNode jsonNode : usersData) {
				if (null != jsonNode && !jsonNode.has("username")) {
					L.error("Property that named username must be provided .");
					objectNode.put("error", "Property that named username must be provided .");
					return objectNode;
				}

				if (null != jsonNode && !jsonNode.has("password")) {
					L.error("Property that named password must be provided .");
					objectNode.put("error", "Property that named password must be provided .");
					return objectNode;
				}
			}
		}

		try {

			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME).resolveTemplate(
					"app_name", HXConstants.APP_NAME);
			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(new BasicNameValuePair("Content-Type", "application/json"));
			objectNode = JerseyWorker.sendRequest(webTarget, usersData, HXManager.sCredential,
					HXHTTPMethod.METHOD_POST, headers);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	public ObjectNode createNewIMUsersByPrefix(String usernamePrefix, Long perNumber, Long totalNumber) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (totalNumber == 0 || perNumber == 0) {
			return objectNode;
		}

		System.out.println("你即将注册" + totalNumber + "个用户，如果大于" + perNumber + "了,会分批注册,每次注册" + perNumber + "个");

		ArrayNode genericArrayNode = genericArrayNode(usernamePrefix, totalNumber);
		if (totalNumber <= perNumber) {
			objectNode = createNewIMUsers(genericArrayNode);
		} else {

			for (int i = 0; i < genericArrayNode.size(); i++) {
				ArrayNode tmpArrayNode = HXManager.sJsonFactory.arrayNode();
				tmpArrayNode.add(genericArrayNode.get(i));
				// 300 records on one migration
				if ((i + 1) % perNumber == 0) {
					objectNode = createNewIMUsers(genericArrayNode);
					tmpArrayNode.removeAll();
					continue;
				}

				// the rest records that less than the times of 300
				if (i > (genericArrayNode.size() / perNumber * perNumber - 1)) {
					objectNode = createNewIMUsers(genericArrayNode);
					tmpArrayNode.removeAll();
				}
			}
		}

		return objectNode;
	}

	@Override
	public ObjectNode getIMUser(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (StringUtils.isEmpty(username)) {
			L.error("The primaryKey that will be useed to query must be provided .");

			objectNode.put("error", "The primaryKey that will be useed to query must be provided .");

			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(username);

			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode getIMUsersRecently(int limit, String cursor) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {

			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).queryParam("limit", String.valueOf(limit));
			if (!StringUtils.isEmpty(cursor)) {
				webTarget.queryParam("cursor", cursor);
			}

			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_DELETE,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode deleteIMUser(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(username);

			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_DELETE,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode deleteIMUser(int limit, String queryStr) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {

			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).queryParam("limit", String.valueOf(limit));
			if (!StringUtils.isEmpty(queryStr)) {
				webTarget.queryParam("ql", queryStr);
			}
			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_DELETE,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode modifyIMUserPwd(String username, ObjectNode newPwd) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(username)) {
			L.error("Property that named userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("error",
					"Property that named userPrimaryKey must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		if (null != newPwd && !newPwd.has("newpassword")) {
			L.error("Property that named newpassword must be provided .");
			objectNode.put("error", "Property that named newpassword must be provided .");
			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(username).path("password");

			objectNode = JerseyWorker.sendRequest(webTarget, newPwd, HXManager.sCredential, HXHTTPMethod.METHOD_PUT,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode modifyUserNickname(String username, ObjectNode newNickName) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(username)) {
			L.error("Property that named userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("error",
					"Property that named userPrimaryKey must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		if (null != newNickName && !newNickName.has("nickname")) {
			L.error("Property that named nickname must be provided .");
			objectNode.put("error", "Property that named nickname must be provided .");
			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(username);

			objectNode = JerseyWorker.sendRequest(webTarget, newNickName, HXManager.sCredential,
					HXHTTPMethod.METHOD_PUT, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode addIMFriend(String owneruser, String frienduser) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(owneruser)) {
			L.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("error", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		if (StringUtils.isEmpty(frienduser)) {
			L.error("The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");
			objectNode.put("error",
					"The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_ADDFRIENDS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME)
					.resolveTemplate("ownerUserPrimaryKey", owneruser)
					.resolveTemplate("friendUserPrimaryKey", frienduser);

			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_POST,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode deleteIMFriend(String owneruser, String frienduser) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(owneruser)) {
			L.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("error", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		if (StringUtils.isEmpty(frienduser)) {
			L.error("The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");
			objectNode.put("error",
					"The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = EndPoints.USERS_ADDFRIENDS_TARGET
					.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME)
					.resolveTemplate("ownerUserPrimaryKey", owneruser)
					.resolveTemplate("friendUserPrimaryKey", frienduser);

			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_DELETE,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode getIMFriends(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(username)) {
			L.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("error", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = EndPoints.USERS_ADDFRIENDS_TARGET
					.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).resolveTemplate("ownerUserPrimaryKey", username)
					.resolveTemplate("friendUserPrimaryKey", "");

			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode getBlockIMFriends(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(username)) {
			L.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("error", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = EndPoints.USERS_BLOCKFRIEND_TARGET
					.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).resolveTemplate("ownerUserPrimaryKey", username)
					.resolveTemplate("blockUserPrimaryKey", "");

			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode addBlockIMFriend(String owneruser, ObjectNode blockuser) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(owneruser)) {
			L.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("error", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		if (null != blockuser && !blockuser.has("usernames")) {
			L.error("Property that named useranmes must be provided .");
			objectNode.put("error", "Property that named useranmes must be provided .");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = EndPoints.USERS_BLOCKFRIEND_TARGET
					.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME)
					.resolveTemplate("ownerUserPrimaryKey", owneruser).resolveTemplate("blockUserPrimaryKey", "");

			objectNode = JerseyWorker.sendRequest(webTarget, blockuser, HXManager.sCredential,
					HXHTTPMethod.METHOD_POST, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	public ObjectNode deleteBlockIMFriend(String owneruser, String blockuser) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(owneruser)) {
			L.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("error", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		if (StringUtils.isEmpty(blockuser)) {
			L.error("The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");
			objectNode.put("error",
					"The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");
			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = EndPoints.USERS_ADDFRIENDS_TARGET
					.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME)
					.resolveTemplate("ownerUserPrimaryKey", owneruser)
					.resolveTemplate("blockUserPrimaryKey", blockuser);

			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_DELETE,
					null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode userLogin(String username, String password) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (StringUtils.isEmpty(username)) {
			L.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");

			objectNode.put("error", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");

			return objectNode;
		}
		if (StringUtils.isEmpty(password)) {
			L.error("Your password must be provided，the value is username or uuid of imuser.");

			objectNode.put("error", "Your password must be provided，the value is username or uuid of imuser.");

			return objectNode;
		}

		try {
			ObjectNode dataNode = HXManager.sJsonFactory.objectNode();
			dataNode.put("grant_type", "password");
			dataNode.put("username", username);
			dataNode.put("password", password);

			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(new BasicNameValuePair("Content-Type", "application/json"));

			objectNode = JerseyWorker.sendRequest(
					EndPoints.TOKEN_APP_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME).resolveTemplate(
							"app_name", HXConstants.APP_NAME), dataNode, null, HXHTTPMethod.METHOD_POST, headers);

		} catch (Exception e) {
			throw new RuntimeException("Some errors ocuured while fetching a token by usename and passowrd .");
		}

		return objectNode;
	}

	@Override
	public ObjectNode getUserStatus(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (StringUtils.isEmpty(username)) {
			L.error("You must provided a targetUserPrimaryKey .");
			objectNode.put("error", "You must provided a targetUserPrimaryKey .");
			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(username).path("status");
			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);
			String userStatus = objectNode.get("data").path(username).asText();
			if ("online".equals(userStatus)) {
				L.error(String.format("The status of user[%s] is : [%s] .", username, userStatus));
			} else if ("offline".equals(userStatus)) {
				L.error(String.format("The status of user[%s] is : [%s] .", username, userStatus));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode getOfflineMsgCount(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (StringUtils.isEmpty(username)) {
			L.error("The primaryKey that will be useed to query must be provided .");
			objectNode.put("error", "The primaryKey that will be useed to query must be provided .");
			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path("users").path(username)
					.path("offline_msg_count");
			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode getOfflineMsgStatus(String username, String msgId) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (StringUtils.isEmpty(username)) {
			L.error("The primaryKey that will be useed to query must be provided .");
			objectNode.put("error", "The primaryKey that will be useed to query must be provided .");
			return objectNode;
		}
		if (StringUtils.isEmpty(msgId)) {
			L.error("The msg_id that will be useed to query must be provided .");
			objectNode.put("error", "The msg_id that will be useed to query must be provided .");
			return objectNode;
		}

		try {

			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(new BasicNameValuePair("Content-Type", "application/json"));

			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path("users").path(username)
					.path("offline_msg_status").path(msgId);
			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET,
					headers);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode deactivateIMUser(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (StringUtils.isEmpty(username)) {
			L.error("The primaryKey that will be useed to query must be provided .");
			objectNode.put("error", "The primaryKey that will be useed to query must be provided .");
			return objectNode;
		}

		try {

			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(new BasicNameValuePair("Content-Type", "application/json"));

			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path("users").path(username).path("deactivate");
			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_POST,
					headers);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode activateIMUser(String username) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (StringUtils.isEmpty(username)) {
			L.error("The primaryKey that will be useed to query must be provided .");
			objectNode.put("error", "The primaryKey that will be useed to query must be provided .");
			return objectNode;
		}

		try {

			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(new BasicNameValuePair("Content-Type", "application/json"));

			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path("users").path(username).path("active");
			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_POST,
					headers);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 指定前缀和数量生成用户基本数据
	 * 
	 * @param usernamePrefix
	 * @param number
	 * @return
	 */
	private ArrayNode genericArrayNode(String usernamePrefix, Long number) {
		ArrayNode arrayNode = HXManager.sJsonFactory.arrayNode();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddsssss");
		for (int i = 0; i < number; i++) {
			ObjectNode userNode = HXManager.sJsonFactory.objectNode();
			userNode.put("username", usernamePrefix + "_" + sdf.format(System.currentTimeMillis()));
			userNode.put("password", HXConstants.DEFAULT_PASSWORD);

			arrayNode.add(userNode);
		}

		return arrayNode;
	}

	public static void main(String[] args) {
		/**
		 * 注册IM用户[单个]
		 */
		ObjectNode datanode = JsonNodeFactory.instance.objectNode();
		System.out.println(HXConstants.API_SERVER_HOST + ", " + HXConstants.APP_CLIENT_ID + " , "
				+ HXConstants.APP_CLIENT_SECRET);
		datanode.put("username", "k" + new SimpleDateFormat("yyyyMMddsssss").format(System.currentTimeMillis()));
		datanode.put("password", HXConstants.DEFAULT_PASSWORD);
		ObjectNode createNewIMUserSingleNode = HXManager.sUserManager.createNewIMUser(datanode);
		if (null != createNewIMUserSingleNode) {
			L.info("注册IM用户[单个]: " + createNewIMUserSingleNode.toString());
		}
	}

}
