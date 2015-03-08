package com.ijob.hx.action.jersey.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ijob.hx.action.HXFileManager;
import com.ijob.hx.action.HXManager;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.jersy.JerseyWorker;
import com.ijob.hx.model.jersey.EndPoints;

public class HXFileManagerImpl extends HXFileManager {

	private static Logger L = LoggerFactory.getLogger(HXFileManagerImpl.class);

	@Override
	public ObjectNode mediaUpload(File uploadFile) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();

		if (!uploadFile.exists()) {
			L.error("file: " + uploadFile.toString() + " is not exist!");
			objectNode.put("error", "File or directory not found");
			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = EndPoints.CHATFILES_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME);
			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(new BasicNameValuePair("restrict-access", "true"));
			objectNode = JerseyWorker.uploadFile(webTarget, uploadFile, HXManager.sCredential, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	@Override
	public ObjectNode mediaDownload(String fileUUID, String shareSecret, File localPath, Boolean isThumbnail) {
		ObjectNode objectNode = HXManager.sJsonFactory.objectNode();
		File downLoadedFile = null;

		try {
			JerseyWebTarget webTarget = EndPoints.CHATFILES_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME).path(fileUUID);
			List<NameValuePair> headers = new ArrayList<NameValuePair>();
			headers.add(new BasicNameValuePair("share-secret", shareSecret));
			headers.add(new BasicNameValuePair("Accept", "application/octet-stream"));
			if (isThumbnail != null && isThumbnail) {
				headers.add(new BasicNameValuePair("thumbnail", String.valueOf(isThumbnail)));
			}
			downLoadedFile = JerseyWorker.downLoadFile(webTarget, HXManager.sCredential, headers, localPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		L.error("File download successfully，file path : " + downLoadedFile.getAbsolutePath() + ".");
		objectNode.put("error", "File download successfully .");
		return objectNode;
	}

	public static void main(String[] args) {
		/**
		 * 上传图片文件 curl示例 curl --verbose --header "Authorization: Bearer {token}"
		 * --header "restrict-access:true" --form file=@/Users/stliu/a.jpg
		 * https://a1.easemob.com/easemob-playground/test1/chatfiles
		 */
		File uploadImgFile = new File("./uploadFile/bg1.jpg");
		ObjectNode imgDataNode = HXManager.sFileManager.mediaUpload(uploadImgFile);
		if (null != imgDataNode) {
			L.info("上传图片文件: " + imgDataNode.toString());
		}
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * 下载图片文件 curl示例 curl -O -H "share-secret: " --header
		 * "Authorization: Bearer {token}" -H "Accept: application/octet-stream"
		 * http
		 * ://a1.easemob.com/easemob-playground/test1/chatfiles/0c0f5f3a-e66b
		 * -11e3-8863-f1c202c2b3ae
		 */
		String imgFileUUID = imgDataNode.path("entities").get(0).path("uuid").asText();
		String shareSecret = imgDataNode.path("entities").get(0).path("share-secret").asText();
		System.out.println(uploadImgFile.getPath());
		File downloadedImgFileLocalPath = new File(uploadImgFile.getPath().substring(0,
				uploadImgFile.getPath().lastIndexOf("."))
				+ "-1.jpg");
		boolean isThumbnail = false;
		ObjectNode downloadImgDataNode = HXManager.sFileManager.mediaDownload(imgFileUUID, shareSecret,
				downloadedImgFileLocalPath, isThumbnail);
		if (null != downloadImgDataNode) {
			L.info("下载图片文件: " + downloadImgDataNode.toString());
		}
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * 下载缩略图 curl示例 curl -O -H "thumbnail: true" -H "share-secret: {secret}"
		 * -H "Authorization: Bearer {token}" -H
		 * "Accept: application/octet-stream"
		 * http://a1.easemob.com/easemob-playground
		 * /test1/chatfiles/0c0f5f3a-e66b-11e3-8863-f1c202c2b3ae
		 */
		File downloadedLocalPathThumnailImg = new File(uploadImgFile.getPath().substring(0,
				uploadImgFile.getPath().lastIndexOf("."))
				+ "-2.jpg");
		isThumbnail = true;
		ObjectNode downloadThumnailImgDataNode = HXManager.sFileManager.mediaDownload(imgFileUUID, shareSecret,
				downloadedLocalPathThumnailImg, isThumbnail);
		if (null != downloadThumnailImgDataNode) {
			L.info("下载缩略图: " + downloadThumnailImgDataNode.toString());
		}

		/**
		 * 上传语音文件 curl示例 curl --verbose --header "Authorization: Bearer {token}"
		 * --header "restrict-access:true" --form file=@/Users/stliu/music.MP3
		 * https://a1.easemob.com/easemob-playground/test1/chatfiles
		 *//*
		File uploadAudioFile = new File("/home/lynch/Music/music.MP3");
		ObjectNode audioDataNode = HXManager.sFileManager.mediaUpload(uploadAudioFile);
		if (null != audioDataNode) {
			L.info("上传语音文件: " + audioDataNode.toString());
		}

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		*//**
		 * 下载语音文件 curl示例 curl -O -H "share-secret: {secret}" --header
		 * "Authorization: Bearer {token}" -H "Accept: application/octet-stream"
		 * http
		 * ://a1.easemob.com/easemob-playground/test1/chatfiles/0c0f5f3a-e66b
		 * -11e3-8863-f1c202c2b3ae
		 *//*
		String audioFileUUID = audioDataNode.path("entities").get(0).path("uuid").asText();
		String audioFileShareSecret = audioDataNode.path("entities").get(0).path("share-secret").asText();
		File audioFileLocalPath = new File(uploadAudioFile.getPath().substring(0,
				uploadAudioFile.getPath().lastIndexOf("."))
				+ "-1.MP3");
		ObjectNode downloadAudioDataNode = HXManager.sFileManager.mediaDownload(audioFileUUID, audioFileShareSecret,
				audioFileLocalPath, null);
		if (null != downloadAudioDataNode) {
			L.info("下载语音文件: " + downloadAudioDataNode.toString());
		}*/
	}

}
