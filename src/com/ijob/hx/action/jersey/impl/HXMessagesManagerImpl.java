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
import com.ijob.hx.model.message.TextMessage;

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
		
		if (target == null || target.size() == 0) {
			L.error("send targets must be provided.");
			objectNode.put("error", "send targets must be provided.");
			return objectNode;
		}
		
		if (StringUtils.isEmpty(from)) {
			L.error("from must be provided.");
			objectNode.put("error", "from must be provided.");
			return objectNode;
		}
		
		if (msg == null) {
			L.error("msg must be provided.");
			objectNode.put("error", "msg must be provided.");
			return objectNode;
		}

		try {
			// 构造消息体
			dataNode.put("target_type", targetType);
			dataNode.put("target", target);
			dataNode.put("msg", msg);
			dataNode.put("from", from);
			if (ext != null && ext.size() != 0) {
				dataNode.put("ext", ext);
			}
			System.out.println(dataNode);
			JerseyWebTarget webTarget = EndPoints.MESSAGES_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME);

			objectNode = JerseyWorker.sendRequest(webTarget, dataNode, HXManager.sCredential, HXHTTPMethod.METHOD_POST, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode obtainChatMessage(ObjectNode queryStrNode) {
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
	
	public static void main(String[] args) {
		TextMessage msg = new TextMessage();
		msg.setFrom("test__15030809245513.837394459629648");
		msg.setTargetType("users"); // or chatgroups
		msg.setExtend(null);
		msg.setMsgInfo("this is a text");
		msg.setTargets(new String[]{"test__15030809245539.5547858787524", "test__15030809245597.39145182886534"});
		ObjectNode objectNode = HXManager.sMessageManager.sendChatMessage(msg);
		if (objectNode != null) {
			System.out.println("发送文本信息：" + objectNode.toString());
		}
		
		// 聊天消息 获取最新的20条记录
        ObjectNode queryStrNode = HXManager.sJsonFactory.objectNode();
        queryStrNode.put("limit", "20");
        ObjectNode messages = HXManager.sMessageManager.obtainChatMessage(queryStrNode);
        if (messages != null) {
			System.out.println("获取最新的20条记录：" + messages.toString());
		}
	}

}
