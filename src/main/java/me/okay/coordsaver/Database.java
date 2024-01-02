package me.okay.coordsaver;

import me.okay.coordsaver.objects.CoordsObj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


public class Database {

    private final String CONNECT_URL;
    private final Logger logger;
    private Connection conn;

    public Database(CoordSaver plugin) {
        CONNECT_URL = "jdbc:sqlite:" + plugin.getDataFolder() + "/storage.db";

        logger = plugin.getLogger();

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(CONNECT_URL);

            logger.info("Connected to database.");

            conn.prepareStatement("""
               CREATE TABLE IF NOT EXISTS "Coords" (
                    "name" TEXT NOT NULL,
                    "uuid" TEXT NOT NULL,
                    "x" INTEGER NOT NULL,
                    "y" INTEGER NOT NULL,
                    "z" INTEGER NOT NULL,
                    "global" INTEGER NOT NULL,
                    "world" TEXT NOT NULL,
                    PRIMARY KEY("uuid", "name")
                );
            """).execute();

            try {
                conn.prepareStatement("ALTER TABLE Coords ADD COLUMN item TEXT NOT NULL DEFAULT 'COMPASS';").execute();
            } catch (SQLException ignored) {}

            try {
                conn.prepareStatement("ALTER TABLE Coords ADD COLUMN playerName TEXT NOT NULL DEFAULT '';").execute();
            } catch (SQLException ignored) {}





        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

    }

    public void safeDisconnect() {
        if (conn != null) {
            try {
                conn.close();
                logger.info("Disconnected from database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveCoords(CoordsObj coords) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT OR REPLACE INTO Coords VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            statement.setString(1, coords.name);
            statement.setString(2, coords.uuid.toString());
            statement.setInt(3, coords.x);
            statement.setInt(4, coords.y);
            statement.setInt(5, coords.z);
            statement.setInt(6, coords.global);
            statement.setString(7, coords.world);
            statement.setString(8, coords.item);
            statement.setString(9, coords.playerName);
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public void renameCoords(String oldName, String newName) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE Coords SET name = ? WHERE name = ?;");
            statement.setString(1, newName);
            statement.setString(2, oldName);
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public boolean deleteCoords(UUID uuid, String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Coords WHERE uuid = ? AND name = ?;");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);

            int deleted = statement.executeUpdate();

            return deleted != 0;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return false;
    }

    public boolean forceDeleteCoord(String playerName, String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Coords WHERE playerName = ? AND name = ?;");
            statement.setString(1, playerName);
            statement.setString(2, name);

            int deleted = statement.executeUpdate();

            return deleted != 0;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return false;
    }

    public void clearCoords(UUID uuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Coords WHERE uuid = ?;");
            statement.setString(1, uuid.toString());
            statement.execute();
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    public int getCoordsCount(UUID uuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM Coords WHERE uuid = ? or global = 1;");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getInt(1);
            }
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return -1;
    }

    public List<CoordsObj> getCoordsList(UUID uuid, int page) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Coords WHERE uuid = ? or global = 1 ORDER BY name LIMIT ?, ?;");
            statement.setString(1, uuid.toString());
            statement.setInt(2, (page - 1) * CoordSaver.COORDS_PER_PAGE);
            statement.setInt(3, CoordSaver.COORDS_PER_PAGE);
            ResultSet result = statement.executeQuery();

            List<CoordsObj> coordinates = new ArrayList<>();
            while (result.next()) {
                coordinates.add(new CoordsObj(result.getString("name"), UUID.fromString(result.getString("uuid")), result.getInt("x"), result.getInt("y"), result.getInt("z"), result.getInt("global"), result.getString("world"),  result.getString("item"), result.getString("playerName")));
            }

            return coordinates;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }
    public List<CoordsObj> getMyCoordsList(UUID uuid, int page) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Coords WHERE uuid = ? ORDER BY name LIMIT ?, ?;");
            statement.setString(1, uuid.toString());
            statement.setInt(2, (page - 1) * CoordSaver.COORDS_PER_PAGE);
            statement.setInt(3, CoordSaver.COORDS_PER_PAGE);
            ResultSet result = statement.executeQuery();

            List<CoordsObj> coordinates = new ArrayList<>();
            while (result.next()) {
                coordinates.add(new CoordsObj(result.getString("name"), UUID.fromString(result.getString("uuid")), result.getInt("x"), result.getInt("y"), result.getInt("z"), result.getInt("global"), result.getString("world"),  result.getString("item"), result.getString("playerName")));
            }

            return coordinates;
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

    public CoordsObj getCoord(UUID uuid, String name) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Coords WHERE (uuid = ? or global = 1) and name = ?;");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                return null;
            }

            return new CoordsObj(result.getString("name"), UUID.fromString(result.getString("uuid")), result.getInt("x"), result.getInt("y"), result.getInt("z"), result.getInt("global"), result.getString("world"), result.getString("item"), result.getString("playerName"));
        }
        catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return null;
    }

}