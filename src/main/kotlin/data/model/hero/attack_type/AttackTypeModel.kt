package com.tiooooo.data.model.hero.attack_type

import com.tiooooo.base.BaseModel
import java.sql.Connection
import java.sql.PreparedStatement

class AttackTypeModel(connection: Connection) : BaseModel(connection) {
    companion object {
        private const val TABLE_NAME = "attack_types"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TYPE = "type"

        private const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID SERIAL PRIMARY KEY, " +
                    "$COLUMN_TYPE VARCHAR(50) UNIQUE NOT NULL" +
                    ");"

        private const val INSERT_ATTACK_TYPE = "INSERT INTO $TABLE_NAME ($COLUMN_TYPE) VALUES (?) RETURNING $COLUMN_ID;"
        private const val SELECT_ALL_ATTACK_TYPES = "SELECT * FROM $TABLE_NAME;"
        private const val SELECT_ATTACK_TYPE_BY_ID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
        private const val UPDATE_ATTACK_TYPE = "UPDATE $TABLE_NAME SET $COLUMN_TYPE = ? WHERE $COLUMN_ID = ?;"
        private const val DELETE_ATTACK_TYPE = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
    }

    override fun initialize() {
        executeUpdate(CREATE_TABLE)
    }

    /**
     * Insert Attack Type
     */
    fun insertAttackType(type: String): Int? {
        return connection.prepareStatement(INSERT_ATTACK_TYPE, PreparedStatement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setString(1, type)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) rs.getInt(COLUMN_ID) else null
        }
    }

    /**
     * Get All Attack Types
     */
    fun getAllAttackTypes(): List<Pair<Int, String>> {
        val attackTypes = mutableListOf<Pair<Int, String>>()
        connection.prepareStatement(SELECT_ALL_ATTACK_TYPES).use { stmt ->
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val id = rs.getInt(COLUMN_ID)
                val type = rs.getString(COLUMN_TYPE)
                attackTypes.add(id to type)
            }
        }
        return attackTypes
    }

    /**
     * Get Attack Type by ID
     */
    fun getAttackTypeById(id: Int): Pair<Int, String>? {
        connection.prepareStatement(SELECT_ATTACK_TYPE_BY_ID).use { stmt ->
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()
            return if (rs.next()) rs.getInt(COLUMN_ID) to rs.getString(COLUMN_TYPE) else null
        }
    }

    /**
     * Update Attack Type
     */
    fun updateAttackType(id: Int, newType: String): Boolean {
        return connection.prepareStatement(UPDATE_ATTACK_TYPE).use { stmt ->
            stmt.setString(1, newType)
            stmt.setInt(2, id)
            stmt.executeUpdate() > 0
        }
    }

    /**
     * Delete Attack Type
     */
    fun deleteAttackType(id: Int): Boolean {
        return connection.prepareStatement(DELETE_ATTACK_TYPE).use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate() > 0
        }
    }
}
