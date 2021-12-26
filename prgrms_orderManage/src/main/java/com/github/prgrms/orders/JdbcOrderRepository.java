package com.github.prgrms.orders;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;
import static java.util.Optional.ofNullable;

import java.time.LocalDate;

@Repository
public class JdbcOrderRepository implements OrderRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Optional<Orders> findById(long id) {
    List<Orders> results = jdbcTemplate.query(
      "SELECT * FROM orders WHERE seq=?",
      mapper,
      id
    );
    return ofNullable(results.isEmpty() ? null : results.get(0));
  }

  @Override
  public List<Orders> findAll() {
    return jdbcTemplate.query(
      "SELECT * FROM orders ORDER BY seq DESC",
      mapper
    );
  }

  static RowMapper<Orders> mapper = (rs, rowNum) ->
    new Orders.Builder()
      .seq(rs.getLong("seq"))
      .productId(rs.getLong("product_seq"))
      .review(new Review(rs.getLong("review_seq")))
      .state(rs.getString("state"))
      .requestMessage(rs.getString("request_msg"))
      .rejectMessage(rs.getString("reject_msg"))
      .completedAt(dateTimeOf(rs.getTimestamp("completed_at")))
      .rejectedAt(dateTimeOf(rs.getTimestamp("rejected_at")))
      .createAt(dateTimeOf(rs.getTimestamp("create_at")))
      .build();

  @Override
  public Optional<Orders> save(long id, String state) {
    if ((state.compareTo("ACCEPTED")==0) || (state.compareTo("SHIPPING")==0))
    {
      jdbcTemplate.update(
                "UPDATE ORDERS SET STATE = ? WHERE SEQ = ?",
                state
        );
    }
    else if (state.compareTo("REJECTED")==0)
    {
      jdbcTemplate.update(
                "UPDATE ORDERS SET STATE = ?, REJECTED_AT = ? WHERE SEQ = ?",
                state,
                java.sql.Date.valueOf(LocalDate.now())
        );
    }
    else if (state.compareTo("COMPLETED")==0)
    {
      jdbcTemplate.update(
                "UPDATE ORDERS SET STATE = ?, COMPLETED_AT = ? WHERE SEQ = ?",
                state,
                java.sql.Date.valueOf(LocalDate.now())
        );
    }

    return null;
  }
}