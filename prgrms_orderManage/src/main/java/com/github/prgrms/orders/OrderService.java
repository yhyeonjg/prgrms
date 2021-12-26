package com.github.prgrms.orders;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.github.prgrms.errors.NotFoundException;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Transactional(readOnly = true)
  public Optional<Orders> findById(Long orderId) {
    checkNotNull(orderId, "orderId must be provided");
    
    return orderRepository.findById(orderId);
  }

  @Transactional(readOnly = true)
  public List<Orders> findAll() {
    return orderRepository.findAll();
  }

  public boolean accept(Long orderId){
    Orders order = findById(orderId).orElseThrow(() -> {
      throw new NotFoundException("could not found order for " + orderId);
    });

    if (order.getState().compareTo("REQUESTED")==0)
    {
      orderRepository.save(orderId, "ACCEPTED");
      return true;
    }
    else
    {
      return false;
    }
  }

  public boolean reject(Long orderId){
    Orders order = findById(orderId).orElseThrow(() -> {
      throw new NotFoundException("could not found order for " + orderId);
    });

    if (order.getState().compareTo("REQUESTED")==0)
    {
      orderRepository.save(orderId, "REJECTED");
      return true;
    }
    else
    {
      return false;
    }
  }

  public boolean shipping(Long orderId){
    Orders order = findById(orderId).orElseThrow(() -> {
      throw new NotFoundException("could not found order for " + orderId);
    });

    if (order.getState().compareTo("ACCEPTED")==0)
    {
      orderRepository.save(orderId, "SHIPPING");
      return true;
    }
    else
    {
      return false;
    }
  }

  public boolean complete(Long orderId){
    Orders order = findById(orderId).orElseThrow(() -> {
      throw new NotFoundException("could not found order for " + orderId);
    });

    if (order.getState().compareTo("SHIPPING")==0)
    {
      orderRepository.save(orderId, "COMPLETED");
      return true;
    }
    else
    {
      return false;
    }
  }
}