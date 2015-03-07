package com.ijob.hx.action.jersey.impl;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		try {
			JerseyWebTarget webTarget = null;
			webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(groupIds.toString());
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
			objectNode.put("message", "Property that named groupname must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("desc")) {
			L.error("Property that named desc must be provided .");
			objectNode.put("message", "Property that named desc must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("public")) {
			L.error("Property that named public must be provided .");
			objectNode.put("message", "Property that named public must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("approval")) {
			L.error("Property that named approval must be provided .");
			objectNode.put("message", "Property that named approval must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("owner")) {
			L.error("Property that named owner must be provided .");
			objectNode.put("message", "Property that named owner must be provided .");
			return objectNode;
		}
		if (!groupDetails.has("members") || !groupDetails.path("members").isArray()) {
			L.error("Property that named members must be provided .");
			objectNode.put("message", "Property that named members must be provided .");
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
		if (updateBody == null || !updateBody.has("description") || !updateBody.has("groupname")
				|| !updateBody.has("maxusers")) {
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
			objectNode.put("message", "Property that named usernames must be provided .");
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
			webTarget = EndPoints.USERS_TARGET.path(username).path("joined_chatgroups");
			objectNode = JerseyWorker
					.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

}
