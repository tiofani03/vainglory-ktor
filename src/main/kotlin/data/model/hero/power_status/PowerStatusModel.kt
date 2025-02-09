package com.tiooooo.data.model.hero.power_status

import com.tiooooo.base.BaseModel
import java.sql.Connection
import java.sql.PreparedStatement

class PowerStatusModel(connection: Connection) : BaseModel(connection) {
    companion object {
        private const val TABLE_NAME = "power_statuses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_HERO_ID = "hero_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CURRENT_VALUE = "current_value"
        private const val COLUMN_MAX_VALUE = "max_value"

        private const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID SERIAL PRIMARY KEY, " +
                    "$COLUMN_HERO_ID INT NOT NULL, " +
                    "$COLUMN_NAME VARCHAR(50) NOT NULL, " +
                    "$COLUMN_CURRENT_VALUE DOUBLE PRECISION NOT NULL, " +
                    "$COLUMN_MAX_VALUE DOUBLE PRECISION NOT NULL, " +
                    "FOREIGN KEY ($COLUMN_HERO_ID) REFERENCES heroes(id) ON DELETE CASCADE" +
                    ");"

        private const val INSERT_POWER_STATUS =
            "INSERT INTO $TABLE_NAME ($COLUMN_HERO_ID, $COLUMN_NAME, $COLUMN_CURRENT_VALUE, $COLUMN_MAX_VALUE) " +
                    "VALUES (?, ?, ?, ?) RETURNING $COLUMN_ID;"

        private const val SELECT_ALL_POWER_STATUS = "SELECT * FROM $TABLE_NAME;"
        private const val SELECT_POWER_STATUS_BY_HERO = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_HERO_ID = ?;"
        private const val UPDATE_POWER_STATUS =
            "UPDATE $TABLE_NAME SET $COLUMN_NAME = ?, $COLUMN_CURRENT_VALUE = ?, $COLUMN_MAX_VALUE = ? WHERE $COLUMN_ID = ?;"
        private const val DELETE_POWER_STATUS = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
    }

    override fun initialize() {
        executeUpdate(CREATE_TABLE)
    }

    /**
     * Insert PowerStatus
     */
    fun insertPowerStatus(heroId: Int, name: String, currentValue: Double, maxValue: Double): Int? {
        return connection.prepareStatement(INSERT_POWER_STATUS, PreparedStatement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setInt(1, heroId)
            stmt.setString(2, name)
            stmt.setDouble(3, currentValue)
            stmt.setDouble(4, maxValue)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) rs.getInt(COLUMN_ID) else null
        }
    }

    /**
     * Get All PowerStatus
     */
    fun getAllPowerStatus(): List<Triple<Int, String, Pair<Double, Double>>> {
        val powerStatuses = mutableListOf<Triple<Int, String, Pair<Double, Double>>>()
        connection.prepareStatement(SELECT_ALL_POWER_STATUS).use { stmt ->
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val heroId = rs.getInt(COLUMN_HERO_ID)
                val name = rs.getString(COLUMN_NAME)
                val currentValue = rs.getDouble(COLUMN_CURRENT_VALUE)
                val maxValue = rs.getDouble(COLUMN_MAX_VALUE)
                powerStatuses.add(Triple(heroId, name, currentValue to maxValue))
            }
        }
        return powerStatuses
    }

    /**
     * Get PowerStatus by Hero ID
     */
    fun getPowerStatusByHero(heroId: Int): List<Pair<String, Pair<Double, Double>>> {
        val powerStatuses = mutableListOf<Pair<String, Pair<Double, Double>>>()
        connection.prepareStatement(SELECT_POWER_STATUS_BY_HERO).use { stmt ->
            stmt.setInt(1, heroId)
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val name = rs.getString(COLUMN_NAME)
                val currentValue = rs.getDouble(COLUMN_CURRENT_VALUE)
                val maxValue = rs.getDouble(COLUMN_MAX_VALUE)
                powerStatuses.add(name to (currentValue to maxValue))
            }
        }
        return powerStatuses
    }

    /**
     * Update PowerStatus
     */
    fun updatePowerStatus(id: Int, name: String, currentValue: Double, maxValue: Double): Boolean {
        return connection.prepareStatement(UPDATE_POWER_STATUS).use { stmt ->
            stmt.setString(1, name)
            stmt.setDouble(2, currentValue)
            stmt.setDouble(3, maxValue)
            stmt.setInt(4, id)
            stmt.executeUpdate() > 0
        }
    }

    /**
     * Delete PowerStatus
     */
    fun deletePowerStatus(id: Int): Boolean {
        return connection.prepareStatement(DELETE_POWER_STATUS).use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate() > 0
        }
    }
}
