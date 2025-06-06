package com.dangisabin.simple_camel_springboot;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
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
class SimpleCamelSpringbootApplicationTests {
	@Autowired
	CamelContext context;

	@EndpointInject("mock:result")
	protected MockEndpoint mockEndpoint;

    @Autowired
    private RouteBuilder routeBuilder;

	@Test
	public void testSimpleTimer() throws Exception {
		String expectBody = "Hello World";

		mockEndpoint.expectedBodiesReceived(expectBody);
		mockEndpoint.expectedMinimumMessageCount(1);

		AdviceWith.adviceWith(context, "simpleTimerId", routeBuilder ->{
			routeBuilder.weaveAddLast().to(mockEndpoint);
		});

		context.start();
		mockEndpoint.assertIsSatisfied();
	}

}
