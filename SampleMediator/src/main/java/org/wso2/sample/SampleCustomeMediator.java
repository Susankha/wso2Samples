package org.wso2.sample;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMProcessingInstruction;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.AxisFault;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class SampleCustomeMediator extends AbstractMediator { 

	public boolean mediate(MessageContext context) {
		OMElement reqMsg = (OMElement) context.getProperty("prop");
		SOAPFactory soapFactory = OMAbstractFactory.getSOAP11Factory();
		StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(reqMsg.getXMLStreamReader(),soapFactory, reqMsg.getQName().getNamespaceURI());
		try {
			context.setEnvelope(builder.getSOAPEnvelope());
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
		}
		SOAPBody body = context.getEnvelope().getBody();
		OMElement searchMatchReturnelement = body.getFirstChildWithName(new QName("urn:siperian.api","searchMatchReturn"));
		OMElement messageElement = searchMatchReturnelement.getFirstChildWithName(new QName("urn:siperian.api","message"));
		OMProcessingInstruction opi = soapFactory.createOMProcessingInstruction(null, "xml-multiple", null);
		messageElement.insertSiblingAfter(opi);
		return true;
	}
}
