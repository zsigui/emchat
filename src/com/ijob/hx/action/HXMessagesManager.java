package com.ijob.hx.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ijob.hx.model.message.BaseMessage;

public abstract class HXMessagesManager {

	/**
	 * 发送消息
	 * 
	 * @param message
	 * @return
	 */
	public abstract ObjectNode sendChatMessage(BaseMessage message);

	/**
	 * 获取(导出)聊天消息
	 * 
	 * @return
	 */
	public abstract Object obtainChatMessage(ObjectNode queryStrNode);
}
