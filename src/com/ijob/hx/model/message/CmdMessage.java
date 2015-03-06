package com.ijob.hx.model.message;

/**
 * 透传消息：不会在客户端提示（铃声，震动，通知栏等），但可以在客户端监听到的消息推送
 * 
 * @author zsigui
 *
 */
public class CmdMessage extends BaseMessage {

	private String mAction;

	public CmdMessage() {
		super();
		setMsgType("cmd");
	}

	public String getAction() {
		return mAction;
	}

	public void setAction(String action) {
		mAction = action;
	}

}
