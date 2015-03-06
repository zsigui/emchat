package com.ijob.hx.model.jersey;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;

import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.jersy.JerseyWorker;

/**
 * JerseyWebTarget EndPoints
 * 
 * @author Lynch 2014-09-15
 * 
 */
public interface EndPoints {

	public final JerseyClient CLIENT = JerseyWorker.getJerseyClient(true);

	public final JerseyWebTarget ROOT_TARGET = CLIENT
			.target(HXConstants.API_HTTP_SCHEMA + "://"
					+ HXConstants.API_SERVER_HOST + "/");

	public JerseyWebTarget MANAGEMENT_TARGET = ROOT_TARGET.path("management");

	public JerseyWebTarget TOKEN_ORG_TARGET = MANAGEMENT_TARGET.path("token");

	public JerseyWebTarget APPLICATION_TEMPLATE = ROOT_TARGET
			.path("{org_name}").path("{app_name}");

	public JerseyWebTarget TOKEN_APP_TARGET = APPLICATION_TEMPLATE
			.path("token");

	public JerseyWebTarget USERS_TARGET = APPLICATION_TEMPLATE.path("users");

	public JerseyWebTarget USERS_ADDFRIENDS_TARGET = APPLICATION_TEMPLATE
			.path("users").path("{ownerUserPrimaryKey}").path("contacts")
			.path("users").path("{friendUserPrimaryKey}");
	
	public JerseyWebTarget USERS_BLOCKFRIEND_TARGET = APPLICATION_TEMPLATE
			.path("users").path("{ownerUserPrimaryKey}").path("block")
			.path("users").path("{blockUserPrimaryKey}");

	public JerseyWebTarget MESSAGES_TARGET = APPLICATION_TEMPLATE
			.path("messages");

	public JerseyWebTarget CHATMESSAGES_TARGET = APPLICATION_TEMPLATE
			.path("chatmessages");

	public JerseyWebTarget CHATGROUPS_TARGET = APPLICATION_TEMPLATE
			.path("chatgroups");

	public JerseyWebTarget CHATFILES_TARGET = APPLICATION_TEMPLATE
			.path("chatfiles");
}
