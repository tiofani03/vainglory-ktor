package com.tiooooo.data.model.hero

import com.tiooooo.base.BaseModel
import java.sql.Connection
import java.sql.PreparedStatement

class HeroModel(connection: Connection) : BaseModel(connection) {
    companion object {
        private const val TABLE_NAME = "heroes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_IMAGE_BG = "image_background"
        private const val COLUMN_DESC = "description"
        private const val COLUMN_ROLE_ID = "role_id"
        private const val COLUMN_ATTACK_TYPE_ID = "attack_type_id"
        private const val COLUMN_POSITION_ID = "position_id"

        private const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID SERIAL PRIMARY KEY, " +
                    "$COLUMN_NAME VARCHAR(100) UNIQUE NOT NULL, " +
                    "$COLUMN_IMAGE TEXT NOT NULL, " +
                    "$COLUMN_IMAGE_BG TEXT NOT NULL, " +
                    "$COLUMN_DESC TEXT NOT NULL, " +
                    "$COLUMN_ROLE_ID INT NOT NULL, " +
                    "$COLUMN_ATTACK_TYPE_ID INT NOT NULL, " +
                    "$COLUMN_POSITION_ID INT NOT NULL, " +
                    "FOREIGN KEY ($COLUMN_ROLE_ID) REFERENCES roles(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY ($COLUMN_ATTACK_TYPE_ID) REFERENCES attack_types(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY ($COLUMN_POSITION_ID) REFERENCES positions(id) ON DELETE CASCADE" +
                    ");"

        private const val INSERT_HERO =
            "INSERT INTO $TABLE_NAME ($COLUMN_NAME, $COLUMN_IMAGE, $COLUMN_IMAGE_BG, $COLUMN_DESC, $COLUMN_ROLE_ID, $COLUMN_ATTACK_TYPE_ID, $COLUMN_POSITION_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING $COLUMN_ID;"

        private const val SELECT_HERO_BY_ID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
        private const val SELECT_ALL_HEROES = "SELECT * FROM $TABLE_NAME;"
        private const val UPDATE_HERO =
            "UPDATE $TABLE_NAME SET $COLUMN_NAME = ?, $COLUMN_IMAGE = ?, $COLUMN_IMAGE_BG = ?, $COLUMN_DESC = ?, $COLUMN_ROLE_ID = ?, $COLUMN_ATTACK_TYPE_ID = ?, $COLUMN_POSITION_ID = ? WHERE $COLUMN_ID = ?;"
        private const val DELETE_HERO = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
    }

    override fun initialize() {
        executeUpdate(CREATE_TABLE)
    }

    /**
     * Insert Hero
     */
    fun insertHero(
        name: String,
        image: String,
        imageBackground: String,
        desc: String,
        roleId: Int,
        attackTypeId: Int,
        positionId: Int
    ): Int? {
        return connection.prepareStatement(INSERT_HERO, PreparedStatement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setString(1, name)
            stmt.setString(2, image)
            stmt.setString(3, imageBackground)
            stmt.setString(4, desc)
            stmt.setInt(5, roleId)
            stmt.setInt(6, attackTypeId)
            stmt.setInt(7, positionId)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) rs.getInt(COLUMN_ID) else null
        }
    }

    /**
     * Get Hero by ID
     */
    fun getHeroById(heroId: Int): HeroData? {
        connection.prepareStatement(SELECT_HERO_BY_ID).use { stmt ->
            stmt.setInt(1, heroId)
            val rs = stmt.executeQuery()
            if (rs.next()) {
                return HeroData(
                    rs.getInt(COLUMN_ID),
                    rs.getString(COLUMN_NAME),
                    rs.getString(COLUMN_IMAGE),
                    rs.getString(COLUMN_IMAGE_BG),
                    rs.getString(COLUMN_DESC),
                    rs.getInt(COLUMN_ROLE_ID),
                    rs.getInt(COLUMN_ATTACK_TYPE_ID),
                    rs.getInt(COLUMN_POSITION_ID)
                )
            }
        }
        return null
    }

    /**
     * Get All Heroes
     */
    fun getAllHeroes(): List<HeroData> {
        val heroes = mutableListOf<HeroData>()
        connection.prepareStatement(SELECT_ALL_HEROES).use { stmt ->
            val rs = stmt.executeQuery()
            while (rs.next()) {
                heroes.add(
                    HeroData(
                        rs.getInt(COLUMN_ID),
                        rs.getString(COLUMN_NAME),
                        rs.getString(COLUMN_IMAGE),
                        rs.getString(COLUMN_IMAGE_BG),
                        rs.getString(COLUMN_DESC),
                        rs.getInt(COLUMN_ROLE_ID),
                        rs.getInt(COLUMN_ATTACK_TYPE_ID),
                        rs.getInt(COLUMN_POSITION_ID)
                    )
                )
            }
        }
        return heroes
    }

    /**
     * Update Hero
     */
    fun updateHero(
        heroId: Int,
        name: String,
        image: String,
        imageBackground: String,
        desc: String,
        roleId: Int,
        attackTypeId: Int,
        positionId: Int
    ): Boolean {
        return connection.prepareStatement(UPDATE_HERO).use { stmt ->
            stmt.setString(1, name)
            stmt.setString(2, image)
            stmt.setString(3, imageBackground)
            stmt.setString(4, desc)
            stmt.setInt(5, roleId)
            stmt.setInt(6, attackTypeId)
            stmt.setInt(7, positionId)
            stmt.setInt(8, heroId)
            stmt.executeUpdate() > 0
        }
    }

    /**
     * Delete Hero
     */
    fun deleteHero(heroId: Int): Boolean {
        return connection.prepareStatement(DELETE_HERO).use { stmt ->
            stmt.setInt(1, heroId)
            stmt.executeUpdate() > 0
        }
    }
}

/**
 * Data Class untuk Hero (tanpa List)
 */
data class HeroData(
    val id: Int,
    val name: String,
    val image: String,
    val imageBackground: String,
    val desc: String,
    val roleId: Int,
    val attackTypeId: Int,
    val positionId: Int
)
