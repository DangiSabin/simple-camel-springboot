package com.dangisabin.simple_camel_springboot.components;

import com.dangisabin.simple_camel_springboot.beans.InboundNameAddress;
import com.dangisabin.simple_camel_springboot.processor.InboundMessageProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class LegacyFileRoute extends RouteBuilder {

    Logger logger = Logger.getLogger(LegacyFileRoute.class.getName());
    BeanIODataFormat inboundDataFormat = new BeanIODataFormat("InboundMessageBeanIOMapping.xml","inboundMessageStream");

    @Override
    public void configure() throws Exception {
        // reading and writing operations on txt file
        from("file:src/data/input?fileName=inputFile.txt")
                .routeId("legacyFileMoveRouteId")
                .process(exchange -> {
                    String fileData = exchange.getIn().getBody(String.class);
                    logger.info("This is the read fileData: " + fileData);
                })
                .to("file:src/data/output?fileName=outputFile.txt");

        // reading and writing operations on CSV file
        from("file:src/data/input?fileName=inputCsvFile.csv")
                .routeId("legacyFileMoveRouteId")
                .split(body().tokenize("\n",1,true))
                .streaming()
                .unmarshal(inboundDataFormat)
                    .process(new InboundMessageProcessor())
                    .log(LoggingLevel.INFO, "Transformed Body: ${body}")
                    .convertBodyTo(String.class)
                    .to("file:src/data/output?fileName=outputCsvFile.csv&fileExist=append&appendChars=\\n")
                .end();
    }
}
