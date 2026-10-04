package org.store.pro3assignment.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.store.pro3assignment.model.ProductRecord;

import java.util.List;

@Repository
public class TraceabilityRepository {

  private final JdbcTemplate jdbc;

  public TraceabilityRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public List<String> findAnimalIdsByProduct(String productId) {
    String sql = """
                SELECT DISTINCT p.animal_id
                FROM product_tray pt
                JOIN part p ON p.tray_id = pt.tray_id
                WHERE pt.product_id = CAST(? AS INTEGER)
                ORDER BY p.animal_id
                """;
    return jdbc.queryForList(sql, String.class, productId);
  }

  public List<ProductRecord> findProductsByAnimal(String animalId) {
    String sql = """
                SELECT DISTINCT pr.product_id, pr.product_type
                FROM part p
                JOIN product_tray pt ON pt.tray_id = p.tray_id
                JOIN product pr      ON pr.product_id = pt.product_id
                WHERE p.animal_id = CAST(? AS INTEGER)
                ORDER BY pr.product_id
                """;
    return jdbc.query(sql,
        (rs, rowNum) -> new ProductRecord(rs.getString("product_id"), rs.getString("product_type")),
        animalId);
  }

  public boolean animalExists(String animalId) {
    Integer count = jdbc.queryForObject(
        "SELECT COUNT(*) FROM animal WHERE animal_id = CAST(? AS INTEGER)", Integer.class, animalId);
    return count != null && count > 0;
  }

  public boolean productExists(String productId) {
    Integer count = jdbc.queryForObject(
        "SELECT COUNT(*) FROM product WHERE product_id = CAST(? AS INTEGER)", Integer.class, productId);
    return count != null && count > 0;
  }
}