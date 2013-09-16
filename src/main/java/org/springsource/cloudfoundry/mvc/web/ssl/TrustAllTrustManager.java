package org.springsource.cloudfoundry.mvc.web.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustAllTrustManager implements X509TrustManager {
	@Override
	public void checkClientTrusted(X509Certificate[] certs, String authType)
			throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] certs, String authType)
			throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[]{};
	}
}
