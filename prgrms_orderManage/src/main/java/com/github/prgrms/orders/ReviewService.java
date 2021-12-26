package com.github.prgrms.orders;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.products.Product;
import com.github.prgrms.products.ProductService;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final OrderService orderService;
  private final ProductService productService;

  public ReviewService(ReviewRepository reviewRepository, OrderService orderService, ProductService productService, OrderRepository orderRepository) {
    this.reviewRepository = reviewRepository;
    this.orderService = orderService;
    this.productService = productService;
  }

  @Transactional(readOnly = true)
  public Optional<Review> findById(Long reviewId) {
    checkNotNull(reviewId, "reviewId must be provided");

    return reviewRepository.findById(reviewId);
  }
  public Review review(Long userSeq, Long orderSeq, ReviewDto request) {
    // 주문 정보 찾기
    Orders order = orderService.findById(orderSeq).orElseThrow(() -> {
      throw new NotFoundException("could not found order for " + orderSeq);
    });
    Product product = productService.findById(order.getProductId()).orElseThrow(() -> {
      throw new NotFoundException("could not found order for " + order.getProductId());
    });
    
    checkArgument(order.isCompleted(), "Could not write review for order "+Long.toString(orderSeq)+" because state("+order.getState()+") is not allowed");
    checkArgument(product.getReviewCount() != 1, "Could not write review for order "+Long.toString(orderSeq)+" because have already written");

    Review review = new Review.Builder().productId(order.getProductId())
        .content(request.getContent()).createAt(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()))
        .build();

    Long review_seq = reviewRepository.insert(review, userSeq, order.getSeq());
    review.setSeq(review_seq);

    return review;
  }
}