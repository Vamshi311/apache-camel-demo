package com.pmic.apachecameldemo.mapper;



import java.util.List;

import org.mapstruct.Mapper;

import com.pmic.apachecameldemo.domain.OrderDto;
import com.pmic.apachecameldemo.entity.OrderEntity;

@Mapper
public interface OrderMapper {

	OrderDto mapToDto(OrderEntity order);

	OrderEntity mapFromDto(OrderDto orderDto);

	List<OrderDto> mapToDtos(List<OrderEntity> orders);

	List<OrderEntity> mapFromDtos(List<OrderDto> orderDtos);
}
