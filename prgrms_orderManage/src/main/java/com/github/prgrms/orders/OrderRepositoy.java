package com.github.prgrms.orders;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {
  Optional<Orders> findById(long id);

  List<Orders> findAll();

  Optional<Orders> save(long id, String state);
}