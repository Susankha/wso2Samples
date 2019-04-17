package org.wso2.sample.mediator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.axiom.om.OMException;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.transport.passthru.PassThroughConstants;
import org.apache.synapse.transport.passthru.Pipe;
import org.apache.commons.io.IOUtils;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.apache.synapse.util.MessageHelper;

public class TestMediator extends AbstractMediator {

    private Log log = LogFactory.getLog(TestMediator.class);

    public boolean mediate(MessageContext context) {
        // Getting the json payload to string
        Axis2MessageContext axis2smc = (Axis2MessageContext) context;
        org.apache.axis2.context.MessageContext axis2MessageCtx = axis2smc.getAxis2MessageContext();
        BufferedInputStream bufferedInputStream = (BufferedInputStream) axis2MessageCtx
                .getProperty(PassThroughConstants.BUFFERED_INPUT_STREAM);
        byte[] byteArray = null;
        try {
            //reset the possion of the buffer to zero
            bufferedInputStream.reset();

            //Trying to access the payload and for malformed payload, it's thorws exception
            String payload = context.getEnvelope().toString();

        } catch (OMException e) {
            try {
                byteArray = IOUtils.toByteArray(bufferedInputStream);
                InputStream input = new ByteArrayInputStream(byteArray);

                StringWriter writer = new StringWriter();
                IOUtils.copy(input, writer);
                String malformedPayload = writer.toString();

                handleException("Malformed payload received :" + malformedPayload, e, context);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            log.error(" IOException occured ", e);
        }
        return true;
    }
}