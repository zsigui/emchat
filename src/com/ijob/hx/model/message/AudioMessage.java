package com.ijob.hx.model.message;

/**
 * 语音消息
 * 
 * @author zsigui
 *
 */
public class AudioMessage extends BaseMessage {

	private String mUrl;
	private String mFilename;
	private int mLength;

	public AudioMessage() {
		super();
		setMsgType("audio");
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getFilename() {
		return mFilename;
	}

	public void setFilename(String filename) {
		mFilename = filename;
	}

	public int getLength() {
		return mLength;
	}

	public void setLength(int length) {
		mLength = length;
	}

}
