package com.ijob.hx.model.message;

/**
 * 文本消息
 * 
 * @author zsigui
 *
 */
public class TextMessage extends BaseMessage {

	private String mMsgInfo;

	public TextMessage() {
		super();
		// 设置类型为文本消息
		setMsgType("txt");
	}

	public String getMsgInfo() {
		return mMsgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		mMsgInfo = msgInfo;
	}

}
