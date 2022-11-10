package com.egoday.xml.soap.common;

import javax.validation.constraints.NotNull;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

/**
 * Utilidades para proporcionar token de seguridad mediante usuario y contraseña.
 * 
 * @see http://docs.oasis-open.org/wss/v1.1/wss-v1.1-spec-os-UsernameTokenProfile.pdf
 */
public final class UsernameTokenUtil {

	private static final String SCHEMA =
			"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

	private static final String SCHEMA_PREFIX = "wsse";

	private static final String PASSWORD_TYPE =
			"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";

	private UsernameTokenUtil() { }

	public static void addUsernameToken(
			@NotNull SOAPMessage soapMessage,
			@NotNull String username,
			@NotNull String password) throws SOAPException {

		SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();

		SOAPHeader header = soapEnvelope.getHeader();
		if (header == null) {
			header = soapEnvelope.addHeader();
		}

		SOAPHeaderElement security = header.addHeaderElement(new QName(SCHEMA, "Security", SCHEMA_PREFIX));
		SOAPElement usernameToken = security.addChildElement("UsernameToken", SCHEMA_PREFIX);

		SOAPElement usernameElement = usernameToken.addChildElement("Username", SCHEMA_PREFIX);
		usernameElement.setTextContent(username);

		SOAPElement passwordElement = usernameToken.addChildElement("Password", SCHEMA_PREFIX);
		passwordElement.addAttribute(soapEnvelope.createName("type"), PASSWORD_TYPE);
		passwordElement.setTextContent(password);
	}

}
