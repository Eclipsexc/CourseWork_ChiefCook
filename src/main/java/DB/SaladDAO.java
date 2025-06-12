package DB;

import Salad.Salad;
import Vegetables.Vegetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class SaladDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaladDAO.class);

    public void insertSalad(Salad salad) {
        String insertSaladSQL = "INSERT INTO Salads (name) VALUES (?)";
        String insertIngredientSQL = "INSERT INTO Salad_Ingredients (salad_id, vegetable_id, weight) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement saladStmt = conn.prepareStatement(insertSaladSQL, Statement.RETURN_GENERATED_KEYS)) {
                saladStmt.setString(1, salad.getName());
                saladStmt.executeUpdate();

                ResultSet keys = saladStmt.getGeneratedKeys();
                if (keys.next()) {
                    int saladId = keys.getInt(1);
                    salad.setId(saladId);

                    try (PreparedStatement ingrStmt = conn.prepareStatement(insertIngredientSQL)) {
                        for (Map.Entry<Vegetable, Double> entry : salad.getIngredients().entrySet()) {
                            ingrStmt.setInt(1, saladId);
                            ingrStmt.setInt(2, entry.getKey().getId());
                            ingrStmt.setDouble(3, entry.getValue());
                            ingrStmt.executeUpdate();
                        }
                    }

                    LOGGER.info("Успішно додано салат '{}' з ID {}", salad.getName(), saladId);
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                LOGGER.error("Помилка вставки салату: {}", ex.getMessage(), ex);
                throw ex;
            }

        } catch (SQLException e) {
            LOGGER.error("Помилка з'єднання під час вставки: {}", e.getMessage(), e);
        }
    }

    public List<Salad> getAllSalads(List<Vegetable> allVegetables) {
        List<Salad> list = new ArrayList<>();

        String saladQuery = "SELECT * FROM Salads";
        String ingrQuery = "SELECT vegetable_id, weight FROM Salad_Ingredients WHERE salad_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement saladStmt = conn.prepareStatement(saladQuery);
             ResultSet rs = saladStmt.executeQuery()) {

            while (rs.next()) {
                int saladId = rs.getInt("id");
                String saladName = rs.getString("name");

                Salad salad = new Salad(saladName);
                salad.setId(saladId);

                try (PreparedStatement ingrStmt = conn.prepareStatement(ingrQuery)) {
                    ingrStmt.setInt(1, saladId);
                    try (ResultSet irs = ingrStmt.executeQuery()) {
                        while (irs.next()) {
                            int vegId = irs.getInt("vegetable_id");
                            double grams = irs.getDouble("weight");

                            Vegetable veg = allVegetables.stream()
                                    .filter(v -> v.getId() == vegId)
                                    .findFirst().orElse(null);

                            if (veg != null) {
                                salad.addIngredient(veg, grams);
                            }
                        }
                    }
                }

                list.add(salad);
            }

            LOGGER.info("Зчитано {} салатів з бази", list.size());

        } catch (SQLException e) {
            LOGGER.error("Помилка читання салатів: {}", e.getMessage(), e);
        }

        return list;
    }

    public void deleteSaladById(int id) {
        String deleteSQL = "DELETE FROM Salads WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            LOGGER.info("Видалено салат з ID {}, рядків: {}", id, rows);

        } catch (SQLException e) {
            LOGGER.error("Не вдалося видалити салат ID={}: {}", id, e.getMessage(), e);
        }
    }

    public void updateSalad(Salad salad) {
        String updateSaladSQL = "UPDATE Salads SET name = ? WHERE id = ?";
        String deleteIngredientsSQL = "DELETE FROM Salad_Ingredients WHERE salad_id = ?";
        String insertIngredientSQL = "INSERT INTO Salad_Ingredients (salad_id, vegetable_id, weight) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement updateSaladStmt = conn.prepareStatement(updateSaladSQL);
                    PreparedStatement deleteIngrStmt = conn.prepareStatement(deleteIngredientsSQL);
                    PreparedStatement insertIngrStmt = conn.prepareStatement(insertIngredientSQL)
            ) {
                updateSaladStmt.setString(1, salad.getName());
                updateSaladStmt.setInt(2, salad.getId());
                updateSaladStmt.executeUpdate();

                deleteIngrStmt.setInt(1, salad.getId());
                deleteIngrStmt.executeUpdate();

                for (Map.Entry<Vegetable, Double> entry : salad.getIngredients().entrySet()) {
                    insertIngrStmt.setInt(1, salad.getId());
                    insertIngrStmt.setInt(2, entry.getKey().getId());
                    insertIngrStmt.setDouble(3, entry.getValue());
                    insertIngrStmt.executeUpdate();
                }

                conn.commit();
                LOGGER.info("Оновлено салат '{}' (ID={}) з {} інгредієнтами",
                        salad.getName(), salad.getId(), salad.getIngredients().size());

            } catch (Exception ex) {
                conn.rollback();
                LOGGER.error("Помилка оновлення салату ID={}: {}", salad.getId(), ex.getMessage(), ex);
                throw ex;
            }

        } catch (SQLException e) {
            LOGGER.error("Помилка з'єднання при оновленні: {}", e.getMessage(), e);
        }
    }
}
