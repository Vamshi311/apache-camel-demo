package com.pmic.apachecameldemo.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.stereotype.Component;

import com.pmic.apachecameldemo.domain.OrderDto;
import com.pmic.apachecameldemo.processor.OrderProcessor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderRoutes extends RouteBuilder {

	private final OrderProcessor orderProcessor;

	public OrderRoutes(OrderProcessor orderProcessor) {
		this.orderProcessor = orderProcessor;
	}

	@Override
	public void configure() throws Exception {
		JacksonDataFormat orderListformat = new ListJacksonDataFormat(OrderDto.class);

		onException(Exception.class).log("Exception occurred processing ${body}").marshal(orderListformat)
				.to("{{error-route}}");

		from("{{file-order-route}}").routeId("orders-from-file").log("Received input : ${body}")
				.to("{{file-order-processed}}").unmarshal(orderListformat).process(orderProcessor);
	}
}
