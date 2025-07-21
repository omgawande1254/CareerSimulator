package com.careersim.engine;

import com.careersim.database.DatabaseManager;
import com.careersim.model.*;

import java.sql.*;

public class GameEngine {
    private Connection connection;

    public GameEngine() {
        this.connection = DatabaseManager.connect();
    }

    // -------------------- LOGIN --------------------
    public User login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getInt("current_stage"),
                        rs.getBoolean("is_active_game")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // -------------------- REGISTER --------------------
    public boolean register(String username, String password) {
        String checkQuery = "SELECT * FROM users WHERE username = ?";
        String insertQuery = "INSERT INTO users (username, password_hash, current_stage, is_active_game) VALUES (?, ?, 0, true)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // User already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // -------------------- STATS --------------------
    public PlayerStats loadPlayerStats(int userId) {
        String query = "SELECT * FROM player_stats WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new PlayerStats(
                        rs.getInt("user_id"),
                        rs.getInt("money"),
                        rs.getInt("knowledge"),
                        rs.getInt("energy"),
                        rs.getInt("stress"),
                        rs.getInt("reputation"),
                        rs.getString("career_title")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void savePlayerStats(PlayerStats stats) {
        String insert = "INSERT INTO player_stats (user_id, money, knowledge, energy, stress, reputation, career_title) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insert)) {
            stmt.setInt(1, stats.userId);
            stmt.setInt(2, stats.money);
            stmt.setInt(3, stats.knowledge);
            stmt.setInt(4, stats.energy);
            stmt.setInt(5, stats.stress);
            stmt.setInt(6, stats.reputation);
            stmt.setString(7, stats.careerTitle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStats(PlayerStats stats) {
        String update = "UPDATE player_stats SET money = ?, knowledge = ?, energy = ?, stress = ?, reputation = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(update)) {
            stmt.setInt(1, stats.money);
            stmt.setInt(2, stats.knowledge);
            stmt.setInt(3, stats.energy);
            stmt.setInt(4, stats.stress);
            stmt.setInt(5, stats.reputation);
            stmt.setInt(6, stats.userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------------------- GAME LOGIC --------------------
    public GameChoice getChoice(int stage) {
        String query = "SELECT * FROM game_choices WHERE stage = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, stage);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new GameChoice(
                        rs.getInt("choice_id"),
                        rs.getInt("stage"),
                        rs.getString("description"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void makeChoice(User user, PlayerStats stats, int choiceId, String option) {
        String query = "SELECT money_change, knowledge_change, energy_change, stress_change, reputation_change FROM choice_effects WHERE choice_id = ? AND choice_option = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, choiceId);
            stmt.setString(2, option);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int moneyChange = rs.getInt("money_change");
                int knowledgeChange = rs.getInt("knowledge_change");
                int energyChange = rs.getInt("energy_change");
                int stressChange = rs.getInt("stress_change");
                int reputationChange = rs.getInt("reputation_change");

                // Apply changes
                stats.money += moneyChange;
                stats.knowledge += knowledgeChange;
                stats.energy += energyChange;
                stats.stress += stressChange;
                stats.reputation += reputationChange;

                updateStats(stats);

                // Move to next stage
                user.setCurrentStage(user.getCurrentStage() + 1);
                updateStage(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update current stage in DB
    public void updateStage(User user) {
        String update = "UPDATE users SET current_stage = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(update)) {
            stmt.setInt(1, user.getCurrentStage());
            stmt.setInt(2, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
