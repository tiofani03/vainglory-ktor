package com.tiooooo.data.model.hero.state_color

import com.tiooooo.base.BaseModel
import java.sql.Connection
import java.sql.PreparedStatement

class StateColorModel(connection: Connection) : BaseModel(connection) {
    companion object {
        private const val TABLE_NAME = "state_colors"
        private const val COLUMN_ID = "id"
        private const val COLUMN_HERO_ID = "hero_id"
        private const val COLUMN_VIBRANT = "vibrant"
        private const val COLUMN_DARK_VIBRANT = "dark_vibrant"
        private const val COLUMN_ON_DARK_VIBRANT = "on_dark_vibrant"

        private const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID SERIAL PRIMARY KEY, " +
                    "$COLUMN_HERO_ID INT UNIQUE NOT NULL, " +
                    "$COLUMN_VIBRANT TEXT NOT NULL, " +
                    "$COLUMN_DARK_VIBRANT TEXT NOT NULL, " +
                    "$COLUMN_ON_DARK_VIBRANT TEXT NOT NULL, " +
                    "FOREIGN KEY ($COLUMN_HERO_ID) REFERENCES heroes(id) ON DELETE CASCADE" +
                    ");"

        private const val INSERT_STATE_COLOR =
            "INSERT INTO $TABLE_NAME ($COLUMN_HERO_ID, $COLUMN_VIBRANT, $COLUMN_DARK_VIBRANT, $COLUMN_ON_DARK_VIBRANT) " +
                    "VALUES (?, ?, ?, ?) RETURNING $COLUMN_ID;"

        private const val SELECT_STATE_COLOR_BY_HERO = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_HERO_ID = ?;"
        private const val UPDATE_STATE_COLOR =
            "UPDATE $TABLE_NAME SET $COLUMN_VIBRANT = ?, $COLUMN_DARK_VIBRANT = ?, $COLUMN_ON_DARK_VIBRANT = ? WHERE $COLUMN_HERO_ID = ?;"
        private const val DELETE_STATE_COLOR = "DELETE FROM $TABLE_NAME WHERE $COLUMN_HERO_ID = ?;"
    }

    override fun initialize() {
        executeUpdate(CREATE_TABLE)
    }

    /**
     * Insert StateColor
     */
    fun insertStateColor(heroId: Int, vibrant: Long, darkVibrant: Long, onDarkVibrant: Long): Int? {
        return connection.prepareStatement(INSERT_STATE_COLOR, PreparedStatement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setInt(1, heroId)
            stmt.setLong(2, vibrant)
            stmt.setLong(3, darkVibrant)
            stmt.setLong(4, onDarkVibrant)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) rs.getInt(COLUMN_ID) else null
        }
    }

    /**
     * Get StateColor by Hero ID
     */
    fun getStateColorByHero(heroId: Int): Triple<Long, Long, Long>? {
        connection.prepareStatement(SELECT_STATE_COLOR_BY_HERO).use { stmt ->
            stmt.setInt(1, heroId)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                val vibrant = rs.getLong(COLUMN_VIBRANT)
                val darkVibrant = rs.getLong(COLUMN_DARK_VIBRANT)
                val onDarkVibrant = rs.getLong(COLUMN_ON_DARK_VIBRANT)
                return Triple(vibrant, darkVibrant, onDarkVibrant)
            }
        }
        return null
    }

    /**
     * Update StateColor
     */
    fun updateStateColor(heroId: Int, vibrant: Long, darkVibrant: Long, onDarkVibrant: Long): Boolean {
        return connection.prepareStatement(UPDATE_STATE_COLOR).use { stmt ->
            stmt.setLong(1, vibrant)
            stmt.setLong(2, darkVibrant)
            stmt.setLong(3, onDarkVibrant)
            stmt.setInt(4, heroId)
            stmt.executeUpdate() > 0
        }
    }

    /**
     * Delete StateColor
     */
    fun deleteStateColor(heroId: Int): Boolean {
        return connection.prepareStatement(DELETE_STATE_COLOR).use { stmt ->
            stmt.setInt(1, heroId)
            stmt.executeUpdate() > 0
        }
    }
}
