package com.ijob.hx.model.jersey;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.jersey.client.JerseyWebTarget;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ijob.hx.constants.HXConstants;
import com.ijob.hx.constants.HXHTTPMethod;
import com.ijob.hx.jersy.JerseyWorker;

/**
 * ClientSecretCredentail
 * 
 * @author Lynch 2014-09-15
 * 
 */
public class ClientSecretCredential extends Credential {

	private static JerseyWebTarget CLIENT_TOKEN_TARGET = null;

	public ClientSecretCredential(String clientID, String clientSecret, String role) {
		super(clientID, clientSecret);

		if (role.equals(HXConstants.USER_ROLE_APPADMIN)) {
			CLIENT_TOKEN_TARGET = EndPoints.TOKEN_APP_TARGET.resolveTemplate("org_name", HXConstants.ORG_NAME)
					.resolveTemplate("app_name", HXConstants.APP_NAME);
		}
	}

	@Override
	protected GrantType getGrantType() {
		return GrantType.CLIENT_CREDENTIALS;
	}

	@Override
	protected JerseyWebTarget getTokenRequestTarget() {
		return CLIENT_TOKEN_TARGET;
	}

	@Override
	public Token getToken() {
		if (null == token || token.isExpired()) {
			try {
				ObjectNode objectNode = factory.objectNode();
				objectNode.put("grant_type", "client_credentials");
				objectNode.put("client_id", tokenKey1);
				objectNode.put("client_secret", tokenKey2);
				List<NameValuePair> headers = new ArrayList<NameValuePair>();
				headers.add(new BasicNameValuePair("Content-Type", "application/json"));

				ObjectNode tokenRequest = JerseyWorker.sendRequest(getTokenRequestTarget(), objectNode, null,
						HXHTTPMethod.METHOD_POST, headers);
				
				if (tokenRequest == null || null != tokenRequest.get("error")) {
					return token;
				}
				System.out.println("tokenRequest = " + tokenRequest.toString());
				String accessToken = tokenRequest.get("access_token").asText();
				Long expiredAt = System.currentTimeMillis() + tokenRequest.get("expires_in").asLong() * 1000;
				token = new Token(accessToken, expiredAt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return token;
	}
}
