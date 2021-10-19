package com.pmic.apachecameldemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pmic.apachecameldemo.entity.OrderEntity;
import com.pmic.apachecameldemo.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	Optional<List<OrderEntity>> findAllByStatus(OrderStatus status);

	@Modifying
	@Query("update OrderEntity set status = :status where id in (:orderIds)")
	@Transactional
	void upadteOrderStatus(@Param("orderIds") List<Long> orderIds, @Param("status") OrderStatus status);
}
