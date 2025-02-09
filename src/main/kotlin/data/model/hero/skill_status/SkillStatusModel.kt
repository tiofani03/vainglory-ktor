package com.tiooooo.data.model.hero.skill_status

import com.tiooooo.base.BaseModel
import java.sql.Connection
import java.sql.PreparedStatement

class SkillStatusModel(connection: Connection) : BaseModel(connection) {
    companion object {
        private const val TABLE_NAME = "skill_statuses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_HERO_ID = "hero_id"
        private const val COLUMN_SKILL_NAME = "skill_name"
        private const val COLUMN_SKILL_DESC = "skill_desc"
        private const val COLUMN_SKILL_IMAGE = "skill_image"

        private const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID SERIAL PRIMARY KEY, " +
                    "$COLUMN_HERO_ID INT NOT NULL, " +
                    "$COLUMN_SKILL_NAME VARCHAR(100) NOT NULL, " +
                    "$COLUMN_SKILL_DESC TEXT NOT NULL, " +
                    "$COLUMN_SKILL_IMAGE TEXT NOT NULL, " +
                    "FOREIGN KEY ($COLUMN_HERO_ID) REFERENCES heroes(id) ON DELETE CASCADE" +
                    ");"

        private const val INSERT_SKILL =
            "INSERT INTO $TABLE_NAME ($COLUMN_HERO_ID, $COLUMN_SKILL_NAME, $COLUMN_SKILL_DESC, $COLUMN_SKILL_IMAGE) " +
                    "VALUES (?, ?, ?, ?) RETURNING $COLUMN_ID;"

        private const val SELECT_ALL_SKILLS = "SELECT * FROM $TABLE_NAME;"
        private const val SELECT_SKILLS_BY_HERO = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_HERO_ID = ?;"
        private const val UPDATE_SKILL =
            "UPDATE $TABLE_NAME SET $COLUMN_SKILL_NAME = ?, $COLUMN_SKILL_DESC = ?, $COLUMN_SKILL_IMAGE = ? WHERE $COLUMN_ID = ?;"
        private const val DELETE_SKILL = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
    }

    override fun initialize() {
        executeUpdate(CREATE_TABLE)
    }

    /**
     * Insert SkillStatus
     */
    fun insertSkillStatus(heroId: Int, skillName: String, skillDesc: String, skillImage: String): Int? {
        return connection.prepareStatement(INSERT_SKILL, PreparedStatement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setInt(1, heroId)
            stmt.setString(2, skillName)
            stmt.setString(3, skillDesc)
            stmt.setString(4, skillImage)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) rs.getInt(COLUMN_ID) else null
        }
    }

    /**
     * Get All Skills
     */
    fun getAllSkills(): List<Triple<Int, String, String>> {
        val skills = mutableListOf<Triple<Int, String, String>>()
        connection.prepareStatement(SELECT_ALL_SKILLS).use { stmt ->
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val heroId = rs.getInt(COLUMN_HERO_ID)
                val skillName = rs.getString(COLUMN_SKILL_NAME)
                val skillDesc = rs.getString(COLUMN_SKILL_DESC)
                skills.add(Triple(heroId, skillName, skillDesc))
            }
        }
        return skills
    }

    /**
     * Get Skills by Hero ID
     */
    fun getSkillsByHero(heroId: Int): List<Triple<String, String, String>> {
        val skills = mutableListOf<Triple<String, String, String>>()
        connection.prepareStatement(SELECT_SKILLS_BY_HERO).use { stmt ->
            stmt.setInt(1, heroId)
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val skillName = rs.getString(COLUMN_SKILL_NAME)
                val skillDesc = rs.getString(COLUMN_SKILL_DESC)
                val skillImage = rs.getString(COLUMN_SKILL_IMAGE)
                skills.add(Triple(skillName, skillDesc, skillImage))
            }
        }
        return skills
    }

    /**
     * Update SkillStatus
     */
    fun updateSkillStatus(id: Int, skillName: String, skillDesc: String, skillImage: String): Boolean {
        return connection.prepareStatement(UPDATE_SKILL).use { stmt ->
            stmt.setString(1, skillName)
            stmt.setString(2, skillDesc)
            stmt.setString(3, skillImage)
            stmt.setInt(4, id)
            stmt.executeUpdate() > 0
        }
    }

    /**
     * Delete SkillStatus
     */
    fun deleteSkillStatus(id: Int): Boolean {
        return connection.prepareStatement(DELETE_SKILL).use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate() > 0
        }
    }
}
