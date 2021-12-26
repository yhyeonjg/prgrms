package com.github.prgrms.orders;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Orders {
  private Long seq;

  private Long productId;

  private Review review;

  private String state;

  private String requestMessage;

  private String rejectMessage;

  private LocalDateTime completedAt;

  private LocalDateTime rejectedAt;

  private LocalDateTime createAt;

  public Orders(Long seq, Long productId, Review review, String state, String requestMessage,
      String rejectMessage, LocalDateTime completedAt, LocalDateTime rejectedAt, LocalDateTime createAt) {
    checkArgument(isEmpty(requestMessage) || requestMessage.length() <= 1000,
        "request message length must be less than 1000 characters");
    checkArgument(isEmpty(rejectMessage) || rejectMessage.length() <= 1000,
        "reject message length must be less than 1000 characters");

    this.seq = seq;
    this.productId = productId;
    this.review = review;
    this.state = state;
    this.requestMessage = requestMessage;
    this.rejectMessage = rejectMessage;
    this.completedAt = completedAt;
    this.rejectedAt = rejectedAt;
    this.createAt = defaultIfNull(createAt, now());
  }

  public Long getSeq() {
    return seq;
  }

  public Long getProductId() {
    return productId;
  }

  public Review getReview() {
    return review;
  }

  public String getState() {
    return state;
  }

  public void setSeq(Long seq) {
    this.seq = seq;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public void setReview(Review review) {
    this.review = review;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Optional<String> getRequestMessage() {
    return ofNullable(requestMessage);
  }

  public void setRequestMessage(String requestMessage) {
    checkArgument(isEmpty(requestMessage) || requestMessage.length() <= 1000,
        "request message length must be less than 1000 characters");

    this.requestMessage = requestMessage;
  }

  public Optional<String> getRejectMessage() {
    return ofNullable(rejectMessage);
  }

  public void setRejectMessage(String rejectMessage) {
    checkArgument(isEmpty(rejectMessage) || rejectMessage.length() <= 1000,
        "reject message length must be less than 1000 characters");

    this.rejectMessage = rejectMessage;
  }

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(LocalDateTime completedAt) {
    this.completedAt = completedAt;
  }

  public LocalDateTime getRejectedAt() {
    return rejectedAt;
  }

  public void setRejectedAt(LocalDateTime rejectedAt) {
    this.rejectedAt = rejectedAt;
  }

  public LocalDateTime getCreateAt() {
    return createAt;
  }

  public void setCreateAt(LocalDateTime createAt) {
    this.createAt = createAt;
  }

  public boolean isCompleted(){
    return this.state.compareTo("COMPLETED")==0;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("seq", seq)
        .append("productId", productId).append("review", review).append("state", state)
        .append("requestMessage", requestMessage).append("rejectMessage", rejectMessage).append("completedAt", completedAt)
        .append("rejectedAt", rejectedAt).append("createAt", createAt).toString();
  }

  static public class Builder {
    private Long seq;
    private Long productId;
    private Review review;
    private String state;
    private String requestMessage;
    private String rejectMessage;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime createAt;

    public Builder() {
      /* empty */}

    public Builder(Orders order) {
      this.seq = order.seq;
      this.productId = order.productId;
      this.review = order.review;
      this.state = order.state;
      this.requestMessage = order.requestMessage;
      this.rejectMessage = order.rejectMessage;
      this.completedAt = order.completedAt;
      this.rejectedAt = order.rejectedAt;
      this.createAt = order.createAt;
    }

    public Builder seq(Long seq) {
      this.seq = seq;
      return this;
    }

    public Builder productId(Long productId) {
      this.productId = productId;
      return this;
    }

    public Builder review(Review review) {
      this.review = review;
      return this;
    }

    public Builder state(String state) {
      this.state = state;
      return this;
    }

    public Builder requestMessage(String requestMessage) {
      this.requestMessage = requestMessage;
      return this;
    }

    public Builder rejectMessage(String rejectMessage) {
      this.rejectMessage = rejectMessage;
      return this;
    }

    public Builder completedAt(LocalDateTime completedAt) {
      this.completedAt = completedAt;
      return this;
    }

    public Builder rejectedAt(LocalDateTime rejectedAt) {
      this.rejectedAt = rejectedAt;
      return this;
    }

    public Builder createAt(LocalDateTime createAt) {
      this.createAt = createAt;
      return this;
    }

    public Orders build() {
      return new Orders(seq, productId, review, state, requestMessage, rejectMessage, completedAt, rejectedAt,
          createAt);
    }
  }
}
