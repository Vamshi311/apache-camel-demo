package com.pmic.apachecameldemo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pmic.apachecameldemo.service.ShippingService;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

	private final ShippingService shippingService;

	public ShippingController(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

	@PostMapping
	public void startShipping(@RequestBody String message) { // message can be anything
		shippingService.startShipping(message);
	}
}
