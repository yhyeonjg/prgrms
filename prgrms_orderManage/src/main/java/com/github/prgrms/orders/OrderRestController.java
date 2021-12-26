package com.github.prgrms.orders;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.github.prgrms.errors.NotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

import static java.util.stream.Collectors.toList;
import static com.github.prgrms.utils.ApiUtils.ApiResult;
import static com.github.prgrms.utils.ApiUtils.success;

@RestController
@RequestMapping("api/orders")
public class OrderRestController {
  // TODO findAll, findById, accept, reject, shipping, complete 메소드 구현이 필요합니다.

  private final OrderService orderService;
  private final ReviewService reviewService;

  public OrderRestController(OrderService orderService, ReviewService reviewService) {
    this.orderService = orderService;
    this.reviewService = reviewService;
  }

  @GetMapping(path = "{id}")
  public ApiResult<OrderDto> findById(Authentication authentication, @PathVariable("id") Long orderId) {
    Orders order = orderService.findById(orderId).orElseThrow(() -> {
      throw new NotFoundException("could not found order for " + orderId);
    });
    Optional<Review> reviewOp = reviewService.findById(order.getReview().getSeq());
    Review review = null;
    if (reviewOp != null) {
      review = new Review(reviewOp.get().getSeq(), reviewOp.get().getProductId(), reviewOp.get().getContent(),
          reviewOp.get().getCreateAt());
    }
    OrderDto orderDto = new OrderDto(order, review);

    return success(orderDto);
  }

  @GetMapping
  public ApiResult<List<OrderDto>> findAll(Authentication authentication) {
    List<Orders> orders = orderService.findAll().stream().collect(toList());
    List<OrderDto> orderDtos = new ArrayList<>();
    for (Orders order : orders) {
      Optional<Review> reviewOp = reviewService.findById(order.getReview().getSeq());
      Review review = null;
      if (reviewOp != null) {
        review = new Review(reviewOp.get().getSeq(), reviewOp.get().getProductId(), reviewOp.get().getContent(),
            reviewOp.get().getCreateAt());
      }

      OrderDto orderDto = new OrderDto(order, review);
      orderDtos.add(orderDto);
    }

    return success(orderDtos);
  }
  
  @PatchMapping(path = "{id}/accept")
  public ApiResult<Boolean> accept(Authentication authentication, @PathVariable("id") Long orderId) {
    return success(orderService.accept(orderId));
  }

  @PatchMapping(path = "{id}/reject")
  public ApiResult<Boolean> reject(Authentication authentication, @PathVariable("id") Long orderId) {
    return success(orderService.reject(orderId));
  }

  @PatchMapping(path = "{id}/shipping")
  public ApiResult<Boolean> shipping(Authentication authentication, @PathVariable("id") Long orderId) {
    return success(orderService.shipping(orderId));
  }

  @PatchMapping(path = "{id}/complete")
  public ApiResult<Boolean> complete(Authentication authentication, @PathVariable("id") Long orderId) {
    return success(orderService.complete(orderId));
  }
}