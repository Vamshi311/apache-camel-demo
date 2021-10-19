package com.pmic.apachecameldemo.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "address")
	private String address;

	@Column(name = "total")
	private BigDecimal total;

	@Column(name = "purchaser")
	private String purchaser;

	@Column(name = "seller")
	private String seller;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "status")
	private OrderStatus status;

	@Column(name = "fulfillment_center")
	private String fulfillmentCenter;
}
