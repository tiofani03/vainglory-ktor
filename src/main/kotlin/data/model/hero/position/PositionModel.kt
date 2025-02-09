package com.tiooooo.data.model.hero.position

import com.tiooooo.base.BaseModel
import java.sql.Connection
import java.sql.PreparedStatement

class PositionModel(connection: Connection) : BaseModel(connection) {
    companion object {
        private const val TABLE_NAME = "positions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_POSITION = "position"

        private const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID SERIAL PRIMARY KEY, " +
                    "$COLUMN_POSITION VARCHAR(50) UNIQUE NOT NULL" +
                    ");"

        private const val INSERT_POSITION =
            "INSERT INTO $TABLE_NAME ($COLUMN_POSITION) VALUES (?) RETURNING $COLUMN_ID;"
        private const val SELECT_ALL_POSITIONS = "SELECT * FROM $TABLE_NAME;"
        private const val SELECT_POSITION_BY_ID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
        private const val UPDATE_POSITION = "UPDATE $TABLE_NAME SET $COLUMN_POSITION = ? WHERE $COLUMN_ID = ?;"
        private const val DELETE_POSITION = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
    }

    override fun initialize() {
        executeUpdate(CREATE_TABLE)
    }

    /**
     * Insert Position
     */
    fun insertPosition(position: String): Int? {
        return connection.prepareStatement(INSERT_POSITION, PreparedStatement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setString(1, position)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) rs.getInt(COLUMN_ID) else null
        }
    }

    /**
     * Get All Positions
     */
    fun getAllPositions(): List<Pair<Int, String>> {
        val positions = mutableListOf<Pair<Int, String>>()
        connection.prepareStatement(SELECT_ALL_POSITIONS).use { stmt ->
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val id = rs.getInt(COLUMN_ID)
                val position = rs.getString(COLUMN_POSITION)
                positions.add(id to position)
            }
        }
        return positions
    }

    /**
     * Get Position by ID
     */
    fun getPositionById(id: Int): Pair<Int, String>? {
        connection.prepareStatement(SELECT_POSITION_BY_ID).use { stmt ->
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()
            return if (rs.next()) rs.getInt(COLUMN_ID) to rs.getString(COLUMN_POSITION) else null
        }
    }

    /**
     * Update Position
     */
    fun updatePosition(id: Int, newPosition: String): Boolean {
        return connection.prepareStatement(UPDATE_POSITION).use { stmt ->
            stmt.setString(1, newPosition)
            stmt.setInt(2, id)
            stmt.executeUpdate() > 0
        }
    }

    /**
     * Delete Position
     */
    fun deletePosition(id: Int): Boolean {
        return connection.prepareStatement(DELETE_POSITION).use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate() > 0
        }
    }
}
