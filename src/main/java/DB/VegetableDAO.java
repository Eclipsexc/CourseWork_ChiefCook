package DB;

import Vegetables.Vegetable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VegetableDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(VegetableDAO.class);

    public List<Vegetable> getAllVegetables() {
        List<Vegetable> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vegetables");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapToVegetable(rs));
            }
            LOGGER.info("Зчитано {} овочів з бази", list.size());

        } catch (SQLException e) {
            LOGGER.error("Помилка при зчитуванні овочів", e);
            throw new RuntimeException("Помилка при зчитуванні овочів", e);
        }

        return list;
    }

    public void insertVegetable(Vegetable veg) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Vegetables (name, caloriesPer100g, vitamins, proteinsPer100g, carbohydratesPer100g, fatsPer100g, taste) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, veg.getName());
            stmt.setDouble(2, veg.getCaloriesPer100g());
            stmt.setString(3, String.join(",", veg.getVitamins()));
            stmt.setDouble(4, veg.getProteinsPer100g());
            stmt.setDouble(5, veg.getCarbohydratesPer100g());
            stmt.setDouble(6, veg.getFatsPer100g());
            stmt.setString(7, veg.getTaste());

            stmt.executeUpdate();
            LOGGER.info("Овоч '{}' успішно вставлено в базу", veg.getName());

        } catch (SQLException e) {
            LOGGER.error("Помилка при вставці овоча '{}'", veg.getName(), e);
            throw new RuntimeException("Помилка при вставці овоча", e);
        }
    }

    public void clearAll() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate("DELETE FROM Vegetables");
            LOGGER.warn("Таблиця Vegetables очищена, видалено {} записів", rows);
        } catch (SQLException e) {
            LOGGER.error("Помилка при очищенні таблиці Vegetables", e);
            throw new RuntimeException("Помилка при очищенні таблиці Vegetables", e);
        }
    }

    private Vegetable mapToVegetable(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        double calories = rs.getDouble("caloriesPer100g");
        String vitaminsStr = rs.getString("vitamins");
        double proteins = rs.getDouble("proteinsPer100g");
        double carbs = rs.getDouble("carbohydratesPer100g");
        double fats = rs.getDouble("fatsPer100g");
        String taste = rs.getString("taste");

        List<String> vitamins = vitaminsStr != null && !vitaminsStr.isEmpty()
                ? Arrays.asList(vitaminsStr.split(","))
                : new ArrayList<>();

        Vegetable veg = new Vegetable(name, calories, vitamins, proteins, carbs, fats, taste);
        veg.setId(id);
        return veg;
    }
}
