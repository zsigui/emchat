package com.ijob.hx.jersy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.ijob.hx.constants.HXHTTPMethod;
import com.ijob.hx.model.jersey.Credential;
import com.ijob.hx.model.jersey.Token;
import com.ijob.hx.utils.HXUtils;

public class JerseyWorker {
	private static Logger L = LoggerFactory.getLogger(JerseyWorker.class);

	private static JsonNodeFactory factory = new JsonNodeFactory(false);

	/**
	 * Send HTTPS request with Jersey
	 * 
	 * @return
	 */
	public static ObjectNode sendRequest(JerseyWebTarget jerseyWebTarget, Object body, Credential credentail,
			String method, List<NameValuePair> headers) throws RuntimeException {

		System.out.println(jerseyWebTarget.getUri());
		ObjectNode objectNode = factory.objectNode();

		if (!HXUtils.isMatchUrl(jerseyWebTarget.getUri().toString())) {
			L.error("The URL to request is illegal");
			objectNode.put("error", "The URL to request is illegal");
			return objectNode;
		}

		try {

			Invocation.Builder inBuilder = jerseyWebTarget.request();
			if (credentail != null) {
				if (credentail.getToken() != null) {
					Token.applyAuthentication(inBuilder, credentail);
				} else {
					L.error("failed to get the token");
					objectNode.put("error", "failed to get the token");
					return objectNode;
				}
			}

			if (null != headers && !headers.isEmpty()) {
				for (NameValuePair nameValuePair : headers) {
					inBuilder.header(nameValuePair.getName(), nameValuePair.getValue());
				}
			}

			Response response = null;
			if (HXHTTPMethod.METHOD_GET.equals(method)) {
				response = inBuilder.get(Response.class);
			} else if (HXHTTPMethod.METHOD_POST.equals(method)) {
				response = inBuilder.post(Entity.entity(body, MediaType.APPLICATION_JSON), Response.class);
			} else if (HXHTTPMethod.METHOD_PUT.equals(method)) {
				response = inBuilder.put(Entity.entity(body, MediaType.APPLICATION_JSON), Response.class);
			} else if (HXHTTPMethod.METHOD_DELETE.equals(method)) {
				response = inBuilder.delete(Response.class);
			}

			objectNode = response.readEntity(ObjectNode.class);
			objectNode.put("statusCode", response.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * DownLoadFile whit Jersey
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static File downLoadFile(JerseyWebTarget jerseyWebTarget, Credential credentail,
			List<NameValuePair> headers, File localPath) throws IOException {

		Invocation.Builder inBuilder = jerseyWebTarget.request();

		if (credentail != null) { // add token into headers
			Token.applyAuthentication(inBuilder, credentail);
		}

		if (null != headers && !headers.isEmpty()) {

			for (NameValuePair nameValuePair : headers) {
				inBuilder.header(nameValuePair.getName(), nameValuePair.getValue());
			}

		}

		File file = inBuilder.get(File.class);
		file.renameTo(localPath);
		FileWriter fr = new FileWriter(file);

		fr.flush();
		fr.close();
		return file;

	}

	/**
	 * UploadFile whit Jersey
	 * 
	 * @return
	 */
	public static ObjectNode uploadFile(JerseyWebTarget jerseyWebTarget, File file, Credential credentail,
			List<NameValuePair> headers) throws RuntimeException {
		ObjectNode objectNode = factory.objectNode();

		try {

			Invocation.Builder inBuilder = jerseyWebTarget.request();
			if (credentail != null) {
				Token.applyAuthentication(inBuilder, credentail);
			}

			if (null != headers && !headers.isEmpty()) {

				for (NameValuePair nameValuePair : headers) {
					inBuilder.header(nameValuePair.getName(), nameValuePair.getValue());
				}

			}

			FormDataMultiPart multiPart = new FormDataMultiPart();
			multiPart.bodyPart(new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));

			objectNode = inBuilder.post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA), ObjectNode.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * Obtain a JerseyClient whit SSL
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static JerseyClient getJerseyClient(boolean isSSL) {
		ClientBuilder clientBuilder = JerseyClientBuilder.newBuilder().register(MultiPartFeature.class);

		// Create a secure JerseyClient
		if (isSSL) {
			try {
				HostnameVerifier verifier = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};

				TrustManager[] tm = new TrustManager[] { new X509TrustManager() {

					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkServerTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
					}

					public void checkClientTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
					}
				} };

				SSLContext sslContext = SSLContext.getInstance("SSL");

				sslContext.init(null, tm, new SecureRandom());

				clientBuilder.sslContext(sslContext).hostnameVerifier(verifier);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
		}

		return (JerseyClient) clientBuilder.build().register(JacksonJsonProvider.class);
	}
}
