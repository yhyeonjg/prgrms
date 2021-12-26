package com.github.prgrms.orders;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;
import static java.util.Optional.ofNullable;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReviewRepository implements ReviewRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcReviewRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  static RowMapper<Review> mapper = (rs, rowNum) ->
    new Review.Builder()
    .seq(rs.getLong("seq"))
    .productId(rs.getLong("product_seq"))
    .content(rs.getString("content"))
    .createAt(dateTimeOf(rs.getTimestamp("create_at")))
    .build();

    @Override
    public long insert(Review review, Long userId, Long orderSeq) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO REVIEWS(USER_SEQ, PRODUCT_SEQ, CONTENT, CREATE_AT) VALUES(?,?,?,?)",new String[] {"seq"});
            ps.setLong(1, userId);
            ps.setLong(2, review.getProductId());
            ps.setString(3, review.getContent());
            ps.setDate(4, java.sql.Date.valueOf(review.getCreateAt().toLocalDate()));
            return ps;
        }, keyHolder);

        long review_seq = keyHolder.getKey().longValue();

        jdbcTemplate.update(
                "UPDATE ORDERS SET REVIEW_SEQ = ? WHERE SEQ = ?",
                review_seq,
                orderSeq
        );

        jdbcTemplate.update(
                "UPDATE PRODUCTS SET REVIEW_COUNT = REVIEW_COUNT + 1 WHERE SEQ = ?",
                review.getProductId()
        );

        return review_seq;
    }

    @Override
    public Optional<Review> findById(long id) {
      List<Review> results = jdbcTemplate.query(
        "SELECT SEQ, PRODUCT_SEQ, CONTENT, CREATE_AT FROM reviews WHERE seq=?",
        mapper,
        id
      );
      return ofNullable(results.isEmpty() ? null : results.get(0));
    }
}