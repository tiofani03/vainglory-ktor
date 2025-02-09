package com.tiooooo.data.model.hero.skin_status

import com.tiooooo.base.BaseModel
import java.sql.Connection
import java.sql.PreparedStatement

class SkinStatusModel(connection: Connection) : BaseModel(connection) {
    companion object {
        private const val TABLE_NAME = "skin_statuses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_HERO_ID = "hero_id"
        private const val COLUMN_SKIN_NAME = "skin_name"
        private const val COLUMN_SKIN_IMAGE = "skin_image"
        private const val COLUMN_SKIN_TYPE = "skin_type"

        private const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID SERIAL PRIMARY KEY, " +
                    "$COLUMN_HERO_ID INT NOT NULL, " +
                    "$COLUMN_SKIN_NAME VARCHAR(100) NOT NULL, " +
                    "$COLUMN_SKIN_IMAGE TEXT NOT NULL, " +
                    "$COLUMN_SKIN_TYPE VARCHAR(50) DEFAULT 'Default', " +
                    "FOREIGN KEY ($COLUMN_HERO_ID) REFERENCES heroes(id) ON DELETE CASCADE" +
                    ");"

        private const val INSERT_SKIN =
            "INSERT INTO $TABLE_NAME ($COLUMN_HERO_ID, $COLUMN_SKIN_NAME, $COLUMN_SKIN_IMAGE, $COLUMN_SKIN_TYPE) " +
                    "VALUES (?, ?, ?, ?) RETURNING $COLUMN_ID;"

        private const val SELECT_ALL_SKINS = "SELECT * FROM $TABLE_NAME;"
        private const val SELECT_SKINS_BY_HERO = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_HERO_ID = ?;"
        private const val UPDATE_SKIN =
            "UPDATE $TABLE_NAME SET $COLUMN_SKIN_NAME = ?, $COLUMN_SKIN_IMAGE = ?, $COLUMN_SKIN_TYPE = ? WHERE $COLUMN_ID = ?;"
        private const val DELETE_SKIN = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
    }

    override fun initialize() {
        executeUpdate(CREATE_TABLE)
    }

    /**
     * Insert SkinStatus
     */
    fun insertSkinStatus(heroId: Int, skinName: String, skinImage: String, skinType: String = "Default"): Int? {
        return connection.prepareStatement(INSERT_SKIN, PreparedStatement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setInt(1, heroId)
            stmt.setString(2, skinName)
            stmt.setString(3, skinImage)
            stmt.setString(4, skinType)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) rs.getInt(COLUMN_ID) else null
        }
    }

    /**
     * Get All Skins
     */
    fun getAllSkins(): List<Triple<String, String, String>> {
        val skins = mutableListOf<Triple<String, String, String>>()
        connection.prepareStatement(SELECT_ALL_SKINS).use { stmt ->
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val skinName = rs.getString(COLUMN_SKIN_NAME)
                val skinImage = rs.getString(COLUMN_SKIN_IMAGE)
                val skinType = rs.getString(COLUMN_SKIN_TYPE)
                skins.add(Triple(skinName, skinImage, skinType))
            }
        }
        return skins
    }

    /**
     * Get Skins by Hero ID
     */
    fun getSkinsByHero(heroId: Int): List<Triple<String, String, String>> {
        val skins = mutableListOf<Triple<String, String, String>>()
        connection.prepareStatement(SELECT_SKINS_BY_HERO).use { stmt ->
            stmt.setInt(1, heroId)
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val skinName = rs.getString(COLUMN_SKIN_NAME)
                val skinImage = rs.getString(COLUMN_SKIN_IMAGE)
                val skinType = rs.getString(COLUMN_SKIN_TYPE)
                skins.add(Triple(skinName, skinImage, skinType))
            }
        }
        return skins
    }

    /**
     * Update SkinStatus
     */
    fun updateSkinStatus(id: Int, skinName: String, skinImage: String, skinType: String): Boolean {
        return connection.prepareStatement(UPDATE_SKIN).use { stmt ->
            stmt.setString(1, skinName)
            stmt.setString(2, skinImage)
            stmt.setString(3, skinType)
            stmt.setInt(4, id)
            stmt.executeUpdate() > 0
        }
    }

    /**
     * Delete SkinStatus
     */
    fun deleteSkinStatus(id: Int): Boolean {
        return connection.prepareStatement(DELETE_SKIN).use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate() > 0
        }
    }
}
