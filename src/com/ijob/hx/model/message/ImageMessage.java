package com.ijob.hx.model.message;

/**
 * 图片消息
 * 
 * @author zsigui
 *
 */
public class ImageMessage extends BaseMessage {

	private String mUrl;
	private String mFilename;

	public ImageMessage() {
		super();
		setMsgType("img");
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

}
