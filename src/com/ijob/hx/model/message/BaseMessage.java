package com.ijob.hx.model.message;

/**
 * 消息基类 <br />
 * <b> 注意：</b> <br />
 * 1.文字/透传消息可直接编辑发送。 <br />
 * 2.图片/语音/视频消息需要先上传，从接口返回值中获取到相应的参数，按照API要求编辑到消息体中然后的发送。
 * 
 * @author zsigui
 * 
 */
public abstract class BaseMessage {

	private String mTargetType;
	private String[] mTargets;
	private String mFrom;
	private String mExtend;
	private String mMsgType;

	public String getTargetType() {
		return mTargetType;
	}

	public void setTargetType(String targetType) {
		mTargetType = targetType;
	}

	public String[] getTargets() {
		return mTargets;
	}

	public void setTargets(String[] targets) {
		mTargets = targets;
	}

	public String getFrom() {
		return mFrom;
	}

	public void setFrom(String from) {
		mFrom = from;
	}

	public String getExtend() {
		return mExtend;
	}

	public void setExtend(String extend) {
		mExtend = extend;
	}

	public String getMsgType() {
		return mMsgType;
	}

	public void setMsgType(String msgType) {
		mMsgType = msgType;
	}
}
