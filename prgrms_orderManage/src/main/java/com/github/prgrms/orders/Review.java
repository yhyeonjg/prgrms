package com.github.prgrms.orders;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Review {
    private Long seq;

    private Long productId;

    private String content;

    private LocalDateTime createAt;

    public Review(Long seq)
    {
      this.seq = seq;
      this.productId = null;
      this.content = null;
      this.createAt = null;
    }

    public Review(Long seq, Long productId, String content, LocalDateTime createAt) {
      checkArgument(
        isEmpty(content) || content.length() <= 1000,
        "content length must be less than 1000 characters"
      );
      
      this.seq = seq;
      this.productId = productId;
      this.content = content;
      this.createAt = defaultIfNull(createAt, now());
    }

    public void setSeq(Long seq) {
      this.seq = seq;
    }

    public void setProductId(Long productId) {
      this.productId = productId;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public void setCreateAt(LocalDateTime createAt) {
      this.createAt = createAt;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public String getContent() {
      return content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
    
    @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Review review = (Review) o;
    return Objects.equals(seq, review.seq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("seq", seq)
      .append("productId", productId)
      .append("content", content)
      .append("createAt", createAt)
      .toString();
  }

  static public class Builder {
    private Long seq;
    private Long productId;
    private String content;
    private LocalDateTime createAt;

    public Builder() {/*empty*/}

    public Builder(Review review) {
      this.seq = review.seq;
      this.productId = review.productId;
      this.content = review.content;
      this.createAt = review.createAt;
    }

    public Builder seq(Long seq) {
      this.seq = seq;
      return this;
    }

    public Builder productId(Long productId) {
      this.productId = productId;
      return this;
    }

    public Builder content(String content) {
      this.content = content;
      return this;
    }

    public Builder createAt(LocalDateTime createAt) {
      this.createAt = createAt;
      return this;
    }

    public Review build() {
      return new Review(
        seq,
        productId,
        content,
        createAt
      );
    }
  }
}
