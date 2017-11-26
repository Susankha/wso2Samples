package org.wso2.sample;

import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.AxisFault;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMProcessingInstruction;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SaopPayloadBuilder extends AbstractMediator {

    public boolean mediate(MessageContext context) {

        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
                .getAxis2MessageContext();
        String jsonMsg = JsonUtil.jsonPayloadToString(axis2MessageContext);
        SOAPFactory soapFactory = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope soapEnvelope = soapFactory.createSOAPEnvelope();
        soapFactory.createSOAPBody(soapEnvelope);
        //StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(null,soapFactory,null);
        //SOAPBody soapBody = soapFactory.createSOAPBody();

        try {
            JSONObject jsonPayload = new JSONObject(jsonMsg);
            JSONArray param = jsonPayload.getJSONObject("insert").getJSONObject("applicationData")
                    .getJSONArray("record").getJSONObject(0).getJSONArray("param");
            JSONObject child;
            String key;
            String value;
            OMElement childEle;
            for (int i = 0; i < param.length(); i++) {
                child = param.getJSONObject(i);
                key = child.getString("key");
                value = child.getString("value");
                System.out.println(" AAAAAAAAAAAA ************* " + key + " ===== " + value);
                childEle = new OMElementImpl(key,null,soapFactory);
                childEle.setText(value);
                soapEnvelope.getBody().addChild(childEle);
            }
            context.setEnvelope(soapEnvelope);
            System.out.println(" ********* End Of Mediator *************");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }

        return true;
    }
}
