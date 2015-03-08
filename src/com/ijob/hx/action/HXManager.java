package com.ijob.hx.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.ijob.hx.action.jersey.impl.HXFileManagerImpl;
import com.ijob.hx.action.jersey.impl.HXGroupsManagerImpl;
import com.ijob.hx.action.jersey.impl.HXIMUserManagerImpl;
import com.ijob.hx.action.jersey.impl.HXMessagesManagerImpl;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.model.jersey.ClientSecretCredential;
import com.ijob.hx.model.jersey.Credential;

public abstract class HXManager {

	public static final JsonNodeFactory sJsonFactory = new JsonNodeFactory(false);
	// 通过app的client_id和client_secret来获取app管理员token
	public static final Credential sCredential = new ClientSecretCredential(HXConstants.APP_CLIENT_ID,
			HXConstants.APP_CLIENT_SECRET, HXConstants.USER_ROLE_APPADMIN);
	public static final ObjectMapper sObjectMapper = new ObjectMapper();
	public static final HXIMUserManager sUserManager = new HXIMUserManagerImpl();
	public static final HXFileManager sFileManager = new HXFileManagerImpl();
	public static final HXGroupsManager sGroupManager = new HXGroupsManagerImpl();
	public static final HXMessagesManager sMessageManager = new HXMessagesManagerImpl();
}
