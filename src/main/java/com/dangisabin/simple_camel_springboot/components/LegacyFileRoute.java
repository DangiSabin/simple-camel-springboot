package com.dangisabin.simple_camel_springboot.components;

import com.dangisabin.simple_camel_springboot.processor.InboundMessageProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class LegacyFileRoute extends RouteBuilder {
    BeanIODataFormat inboundDataFormat = new BeanIODataFormat("InboundMessageBeanIOMapping.xml","inboundMessageStream");
    Logger logger = Logger.getLogger(LegacyFileRoute.class.getName());

    @Override
    public void configure() throws Exception {
        //reading from input txt file and writing to txt file
        from("file:src/data/input?fileName=inputFile.txt")
                .routeId("legacyFileMoveRouteId")
                .to("file:src/data/output?fileName=outputFile.txt");

        // reading from csv file and writing to csv file
        from("file:src/data/input?fileName=inputCsvFile.csv&noop=true")
                .routeId("legacyFileRouteId")
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
