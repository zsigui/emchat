package com.ijob.hx.action;

import java.io.File;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class HXFileManager {

	/**
	 * 图片/语音文件上传
	 * 
	 * @param uploadFile
	 * @return
	 */
	public abstract ObjectNode mediaUpload(File uploadFile);

	/**
	 * 图片/语音文件下载
	 * 
	 * 
	 * @param fileUUID
	 *            文件在DB的UUID
	 * @param shareSecret
	 *            文件在DB中保存的shareSecret
	 * @param localPath
	 *            下载后文件存放地址
	 * @param isThumbnail
	 *            是否下载缩略图 true:缩略图 false:非缩略图
	 * @return
	 */
	public abstract ObjectNode mediaDownload(String fileUUID,
			String shareSecret, File localPath, Boolean isThumbnail);
}
