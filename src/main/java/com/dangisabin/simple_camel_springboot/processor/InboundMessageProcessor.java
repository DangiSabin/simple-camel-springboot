package com.dangisabin.simple_camel_springboot.processor;

import com.dangisabin.simple_camel_springboot.beans.InboundNameAddress;
import com.dangisabin.simple_camel_springboot.beans.OutboundNameAddress;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Logger;

public class InboundMessageProcessor implements Processor {
    Logger logger = Logger.getLogger(InboundMessageProcessor.class.getName());

    @Override
    public void process(Exchange exchange) throws Exception {
        InboundNameAddress inboundNameAddress = exchange.getIn().getBody(InboundNameAddress.class);
        logger.info(inboundNameAddress.toString());
        exchange.getIn().setBody(new OutboundNameAddress(inboundNameAddress.getName(),returnOutboundNameAddress(inboundNameAddress)));
    }

    private String returnOutboundNameAddress(InboundNameAddress nameAddress){
        StringBuilder concatenatedNameAddress = new StringBuilder();
        concatenatedNameAddress.append(nameAddress.getHouseNumber());
        concatenatedNameAddress.append(" " + nameAddress.getCity());
        concatenatedNameAddress.append(" " + nameAddress.getProvince());
        concatenatedNameAddress.append(" " + nameAddress.getPostalCode());

        return concatenatedNameAddress.toString();
    }
}
