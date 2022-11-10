package com.egoday.xml.soap.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.validation.constraints.NotNull;
import javax.xml.XMLConstants;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

class UsernameTokenUtilTest {

	private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	public static final String SAMPLE_SOAP_MSG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<SOAP-ENV:Envelope "
			+ "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "
			+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" + "<SOAP-ENV:Body>"
			+ "<add xmlns=\"http://ws.apache.org/counter/counter_port_type\">" + "<value xmlns=\"\">15</value>"
			+ "</add>" + "</SOAP-ENV:Body>" + "</SOAP-ENV:Envelope>";
	
	public static final String WS_HEADER = "<SOAP-ENV:Header><wsse:Security "
			+ "xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"
			+ "<wsse:UsernameToken><wsse:Username>username</wsse:Username>"
			+ "<wsse:Password "
			+ "type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">"
			+ "password</wsse:Password>"
			+ "</wsse:UsernameToken>"
			+ "</wsse:Security></SOAP-ENV:Header>";

	@Test
	void addUsernameToken() throws Exception {
		SOAPMessage soapMessage = toSOAPMessage(SAMPLE_SOAP_MSG);

		UsernameTokenUtil.addUsernameToken(soapMessage, USERNAME, PASSWORD);

		Assertions.assertTrue(toString(soapMessage.getSOAPPart()).contains(WS_HEADER));
	}

	private static String toString(@NotNull Document document) throws TransformerException, IOException {
		try (StringWriter writer = new StringWriter()) {
			transformer().transform(new DOMSource(document), new StreamResult(writer));
			return writer.toString();
		}
	}

	private static Transformer transformer() throws TransformerConfigurationException {
		TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();

		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

		return factory.newTransformer();
	}

	private static SOAPMessage toSOAPMessage(String value) throws IOException, SOAPException {
		try (InputStream inputStream = new ByteArrayInputStream(value.getBytes())) {
			return MessageFactory.newInstance().createMessage(null, inputStream);
		}
	}

}
