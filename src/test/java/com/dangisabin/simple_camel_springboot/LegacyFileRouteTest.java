package com.dangisabin.simple_camel_springboot;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CamelSpringBootTest
@SpringBootTest
@UseAdviceWith
class LegacyFileRouteTest {
	@Autowired
	CamelContext context;

	@EndpointInject("mock:result")
	protected MockEndpoint mockEndpoint;
    @Autowired
    private RouteBuilder routeBuilder;

	@Autowired
	ProducerTemplate producerTemplate;

	@Test
	public void testFileMove() throws Exception {
		//Setup the Mock
		String expectBody = "This is the input file that will be processed and moved to output directory.";
		mockEndpoint.expectedBodiesReceived(expectBody);
		mockEndpoint.expectedMinimumMessageCount(1);

		//Tweak the route definition
		AdviceWith.adviceWith(context, "legacyFileMoveRouteId", routeBuilder ->{
			routeBuilder.weaveByToUri("file:*").replace().to("mock:result");
		});

		//Start the context and validate mock is asserted
		context.start();
		mockEndpoint.assertIsSatisfied();
	}

	@Test
	public void testFileMoveByMockingFromEndPoint() throws Exception {
		String expectedBody = "This is the input data after mocking from the end point.";
		mockEndpoint.expectedBodiesReceived(expectedBody);
		mockEndpoint.expectedMinimumMessageCount(1);

		AdviceWith.adviceWith(context, "legacyFileMoveRouteId", routeBuilder ->{
			routeBuilder.replaceFromWith("direct:mockStart");
			routeBuilder.weaveByToUri("file:*").replace().to(mockEndpoint);
		});

		context.start();
		producerTemplate.sendBody("direct:mockStart", expectedBody);
		mockEndpoint.assertIsSatisfied();
	}

}
