package com.example.ecommerceProject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface orderDetailRepository extends JpaRepository<OrderDetail,Long> {
}
