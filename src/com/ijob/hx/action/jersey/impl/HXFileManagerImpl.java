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

}
