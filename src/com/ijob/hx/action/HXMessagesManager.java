package com.ijob.hx.action;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ijob.hx.action.jersey.impl.HXMessagesManagerImpl;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.model.jersey.EndPoints;
import com.ijob.hx.model.message.AudioMessage;
import com.ijob.hx.model.message.BaseMessage;
import com.ijob.hx.model.message.CmdMessage;
import com.ijob.hx.model.message.ImageMessage;
import com.ijob.hx.model.message.TextMessage;
import com.ijob.hx.model.message.VideoMessage;

public abstract class HXMessagesManager {

	protected final Logger L = LoggerFactory.getLogger(HXMessagesManagerImpl.class);

	/**
	 * 发送消息
	 * 
	 * @param message
	 * @return
	 */
	public ObjectNode sendChatMessage(BaseMessage message) {
		ObjectNode result = HXManager.sJsonFactory.objectNode();
		if (message != null) {
			try {
				ArrayNode targets = HXManager.sJsonFactory.arrayNode();
				for (String s : message.getTargets()) {
					if (!StringUtils.isEmpty(s)) {
						targets.add(s);
					}
				}
				ObjectNode extNode = HXManager.sJsonFactory.objectNode();
				extNode.put("ext", HXManager.sObjectMapper.readValue(message.getExtend(), ObjectNode.class));

				if (message instanceof TextMessage) {
					TextMessage txtMsg = (TextMessage) message;
					ObjectNode msgNode = HXManager.sJsonFactory.objectNode();
					msgNode.put("msg", txtMsg.getMsgInfo());
					msgNode.put("type", txtMsg.getMsgType());
					result = sendMessages(txtMsg.getTargetType(), targets, msgNode, txtMsg.getFrom(), extNode);
				} else if (message instanceof AudioMessage) {
					AudioMessage audioMsg = (AudioMessage) message;
					// 语音需要先上传文件
					File uploadAudioFile = new File(audioMsg.getUrl());
					ObjectNode audioDataNode = HXManager.sFileManager.mediaUpload(uploadAudioFile);
					String audioFileUUID = "", audioFileShareSecret = "";
					if (null != audioDataNode && !audioDataNode.has("error")) {
						L.info("上传语音文件: " + audioDataNode.toString());
						audioFileUUID = audioDataNode.path("entities").get(0).path("uuid").asText();
						audioFileShareSecret = audioDataNode.path("entities").get(0).path("share-secret").asText();
					} else {
						L.info("上传语音文件失败，语音消息发送不成功");
						result.put("error", "Failed to upload audio file");
						return result;
					}
					ObjectNode msgNode = HXManager.sJsonFactory.objectNode();
					msgNode.put("type", audioMsg.getMsgType());
					msgNode.put("url", EndPoints.CHATFILES_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
							.resolveTemplate("app_name", HXConstants.APP_NAME).getUri().toString()
							+ audioFileUUID);
					msgNode.put("filename", audioMsg.getFilename());
					msgNode.put("length", audioMsg.getLength());
					msgNode.put("secret", audioFileShareSecret);
					result = sendMessages(audioMsg.getTargetType(), targets, msgNode, audioMsg.getFrom(), extNode);
				} else if (message instanceof CmdMessage) {
					// 发送透传消息
					CmdMessage cmdMsg = (CmdMessage) message;
					ObjectNode msgNode = HXManager.sJsonFactory.objectNode();
					msgNode.put("action", cmdMsg.getAction());
					msgNode.put("type", cmdMsg.getMsgType());
					result = sendMessages(cmdMsg.getTargetType(), targets, msgNode, cmdMsg.getFrom(), extNode);
				} else if (message instanceof ImageMessage) {
					ImageMessage imageMsg = (ImageMessage) message;
					// 给用户发一条图片消息
					File uploadImgFile = new File("/home/lynch/Pictures/24849.jpg");
					ObjectNode imgDataNode = HXManager.sFileManager.mediaUpload(uploadImgFile);
					String imgFileUUID = "", shareSecret = "";
					if (null != imgDataNode && !imgDataNode.has("error")) {
						L.info("上传图片文件: " + imgDataNode.toString());
						imgFileUUID = imgDataNode.path("entities").get(0).path("uuid").asText();
						shareSecret = imgDataNode.path("entities").get(0).path("share-secret").asText();
					} else {
						L.info("上传图片文件失败，图片消息发送不成功");
						result.put("error", "Failed to upload image file");
						return result;
					}
					ObjectNode msgNode = HXManager.sJsonFactory.objectNode();
					msgNode.put("type", imageMsg.getMsgType());
					msgNode.put("url", EndPoints.CHATFILES_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
							.resolveTemplate("app_name", HXConstants.APP_NAME).getUri().toString()
							+ imgFileUUID);
					msgNode.put("filename", imageMsg.getFilename());
					msgNode.put("secret", shareSecret);
					result = sendMessages(imageMsg.getTargetType(), targets, msgNode, imageMsg.getFrom(), extNode);
				} else if (message instanceof VideoMessage) {
					// 发送视频消息
					VideoMessage videoMsg = (VideoMessage) message;

					// 上传视频文件
					ObjectNode videoDataNode = HXManager.sFileManager.mediaUpload(new File(videoMsg.getUrl()));
					String videoFileUUID = "", videoFileShareSecret = "", videoThumbUUID = "", videoThumbSecret = "";
					if (null != videoDataNode && !videoDataNode.has("error")) {
						L.info("上传视频文件: " + videoDataNode.toString());
						videoFileUUID = videoDataNode.path("entities").get(0).path("uuid").asText();
						videoFileShareSecret = videoDataNode.path("entities").get(0).path("share-secret").asText();
					} else {
						L.info("上传视频文件失败，视频消息发送不成功");
						result.put("error", "Failed to upload video file");
						return result;
					}

					// 上传视频缩略图
					ObjectNode videoThumbDataNode = HXManager.sFileManager.mediaUpload(new File(videoMsg.getThumb()));
					ObjectNode msgNode = HXManager.sJsonFactory.objectNode();
					if (null != videoThumbDataNode && !videoThumbDataNode.has("error")) {
						L.info("上传视频文件: " + videoDataNode.toString());
						videoThumbUUID = videoThumbDataNode.path("entities").get(0).path("uuid").asText();
						videoThumbSecret = videoThumbDataNode.path("entities").get(0).path("share-secret").asText();
					}
					msgNode.put("type", videoMsg.getMsgType());
					msgNode.put("url", EndPoints.CHATFILES_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
							.resolveTemplate("app_name", HXConstants.APP_NAME).getUri().toString()
							+ videoFileUUID);
					msgNode.put("filename", videoMsg.getFilename());
					msgNode.put("length", videoMsg.getLength());
					msgNode.put("secret", videoFileShareSecret);
					msgNode.put("file_length", videoMsg.getFileLength());
					msgNode.put("thumb", EndPoints.CHATFILES_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
							.resolveTemplate("app_name", HXConstants.APP_NAME).getUri().toString()
							+ videoThumbUUID);
					msgNode.put("thumb_secret", videoThumbSecret);
					result = sendMessages(videoMsg.getTargetType(), targets, msgNode, videoMsg.getFrom(), extNode);
				} else {
					// this can't happen
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			result.put("error", "The message is null");
		}
		return result;
	};

	/**
	 * 发送消息
	 * 
	 * @param targetType
	 *            消息投递者类型：users 用户, chatgroups 群组
	 * @param target
	 *            接收者ID 必须是数组,数组元素为用户ID或者群组ID
	 * @param msg
	 *            消息内容
	 * @param from
	 *            发送者
	 * @param ext
	 *            扩展字段
	 * 
	 * @return 请求响应
	 */
	public abstract ObjectNode sendMessages(String targetType, ArrayNode target, ObjectNode msg, String from,
			ObjectNode ext);

	/**
	 * 获取(导出)聊天消息
	 * 
	 * @return
	 */
	public abstract Object obtainChatMessage(ObjectNode queryStrNode);
}
