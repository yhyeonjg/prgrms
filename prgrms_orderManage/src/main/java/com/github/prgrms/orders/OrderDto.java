package com.github.prgrms.orders;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//import static org.springframework.beans.BeanUtils.copyProperties;

public class OrderDto {
    private Long seq;

    private Long productId;
    
    private Review review;

    private String state;

    private String requestMessage;

    private String rejectMessage;

    private LocalDateTime completedAt;

    private LocalDateTime rejectedAt;

    private LocalDateTime createAt;

    public OrderDto(Orders source, Review review) {
        this.seq= source.getSeq();
        this.productId = source.getProductId();
        this.review = review;
        this.state = source.getState();

        this.requestMessage = source.getRequestMessage().orElse(null);
        this.rejectMessage = source.getRejectMessage().orElse(null);
        this.completedAt = source.getCompletedAt();
        this.rejectedAt = source.getRejectedAt();
        this.createAt =source.getCreateAt();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("seq", seq)
        .append("productSeq", productId)
        .append("review", review)
        .append("state", state)
        .append("requestMessage", requestMessage)
        .append("rejectMessage", rejectMessage)
        .append("completedAt", completedAt)
        .append("rejectedAt", rejectedAt)
        .append("createAt", createAt)
        .toString();
  }
}
