package com.ijob.hx.model.message;

/**
 * 音频消息
 * 
 * @author zsigui
 *
 */
public class VideoMessage extends BaseMessage {
	private String mFilename;
	private String mThumb;
	private int mLength;
	private String mSecret;
	private int mFileLength;
	private String mThumbSecret;
	private String mUrl;

	public VideoMessage() {
		super();
		setMsgType("video");
	}

	public String getFilename() {
		return mFilename;
	}

	public void setFilename(String filename) {
		mFilename = filename;
	}

	public String getThumb() {
		return mThumb;
	}

	public void setThumb(String thumb) {
		mThumb = thumb;
	}

	public int getLength() {
		return mLength;
	}

	public void setLength(int length) {
		mLength = length;
	}

	public String getSecret() {
		return mSecret;
	}

	public void setSecret(String secret) {
		mSecret = secret;
	}

	public int getFileLength() {
		return mFileLength;
	}

	public void setFileLength(int fileLength) {
		mFileLength = fileLength;
	}

	public String getThumbSecret() {
		return mThumbSecret;
	}

	public void setThumbSecret(String thumbSecret) {
		mThumbSecret = thumbSecret;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

}
