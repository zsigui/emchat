package com.ijob.hx.action.jersey.impl;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyWebTarget;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ijob.hx.action.HXManager;
import com.ijob.hx.action.HXMessagesManager;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.constants.HXHTTPMethod;
import com.ijob.hx.jersy.JerseyWorker;
import com.ijob.hx.model.jersey.EndPoints;

public class HXMessagesManagerImpl extends HXMessagesManager {

	@Override
	public ObjectNode sendMessages(String targetType, ArrayNode target, ObjectNode msg, String from, ObjectNode ext) {

		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();
		ObjectNode dataNode = HXManager.sJsonFactory.objectNode();

		// check properties that must be provided
		if (!("users".equals(targetType) || "chatgroups".equals(targetType))) {
			L.error("TargetType must be users or chatgroups .");

			objectNode.put("error", "TargetType must be users or chatgroups .");

			return objectNode;
		}

		try {
			// 构造消息体
			dataNode.put("target_type", targetType);
			dataNode.put("target", target.toString());
			dataNode.put("msg", msg);
			dataNode.put("from", from);
			dataNode.put("ext", ext);

			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME);

			objectNode = JerseyWorker.sendRequest(webTarget, dataNode, HXManager.sCredential, HXHTTPMethod.METHOD_POST, null);

			objectNode = (ObjectNode) objectNode.get("data");
			for (int i = 0; i < target.size(); i++) {
				String resultStr = objectNode.path(target.path(i).asText()).asText();
				if ("success".equals(resultStr)) {
					L.error(String.format("Message has been send to user[%s] successfully .", target.path(i).asText()));
				} else if (!"success".equals(resultStr)) {
					L.error(String.format("Message has been send to user[%s] failed .", target.path(i).asText()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public Object obtainChatMessage(ObjectNode queryStrNode) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		try {
			JerseyWebTarget webTarget = EndPoints.CHATMESSAGES_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME);
			if (null != queryStrNode && null != queryStrNode.get("ql")
					&& !StringUtils.isEmpty(queryStrNode.get("ql").asText())) {
				webTarget = webTarget.queryParam("ql", queryStrNode.get("ql").asText());
			}
			if (null != queryStrNode && null != queryStrNode.get("limit")
					&& !StringUtils.isEmpty(queryStrNode.get("limit").asText())) {
				webTarget = webTarget.queryParam("limit", queryStrNode.get("limit").asText());
			}
			if (null != queryStrNode && null != queryStrNode.get("cursor")
					&& !StringUtils.isEmpty(queryStrNode.get("cursor").asText())) {
				webTarget = webTarget.queryParam("cursor", queryStrNode.get("cursor").asText());
			}
			objectNode = JerseyWorker.sendRequest(webTarget, null, HXManager.sCredential, HXHTTPMethod.METHOD_GET, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectNode;
	}

}
