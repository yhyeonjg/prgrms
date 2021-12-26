package com.github.prgrms.orders;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {
  long insert(Review review, Long userId, Long orderSeq);
  
  Optional<Review> findById(long reviewId);
}