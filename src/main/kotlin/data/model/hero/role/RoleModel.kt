import com.tiooooo.base.BaseModel
import java.sql.Connection
import java.sql.PreparedStatement

class RoleModel(connection: Connection) : BaseModel(connection) {
    companion object {
        private const val TABLE_NAME = "roles"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"

        private const val CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID SERIAL PRIMARY KEY, " +
                    "$COLUMN_NAME VARCHAR(50) UNIQUE NOT NULL" +
                    ");"

        private const val INSERT_ROLE = "INSERT INTO $TABLE_NAME ($COLUMN_NAME) VALUES (?) RETURNING $COLUMN_ID;"
        private const val SELECT_ALL_ROLES = "SELECT * FROM $TABLE_NAME;"
        private const val SELECT_ROLE_BY_ID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
        private const val UPDATE_ROLE = "UPDATE $TABLE_NAME SET $COLUMN_NAME = ? WHERE $COLUMN_ID = ?;"
        private const val DELETE_ROLE = "DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = ?;"
    }

    override fun initialize() {
        executeUpdate(CREATE_TABLE)
    }

    /**
     * Insert Role
     */
    fun insertRole(name: String): Int? {
        return connection.prepareStatement(INSERT_ROLE, PreparedStatement.RETURN_GENERATED_KEYS).use { stmt ->
            stmt.setString(1, name)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) rs.getInt(COLUMN_ID) else null
        }
    }

    /**
     * Get All Roles
     */
    fun getAllRoles(): List<Pair<Int, String>> {
        val roles = mutableListOf<Pair<Int, String>>()
        connection.prepareStatement(SELECT_ALL_ROLES).use { stmt ->
            val rs = stmt.executeQuery()
            while (rs.next()) {
                val id = rs.getInt(COLUMN_ID)
                val name = rs.getString(COLUMN_NAME)
                roles.add(id to name)
            }
        }
        return roles
    }

    /**
     * Get Role by ID
     */
    fun getRoleById(id: Int): Pair<Int, String>? {
        connection.prepareStatement(SELECT_ROLE_BY_ID).use { stmt ->
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()
            return if (rs.next()) rs.getInt(COLUMN_ID) to rs.getString(COLUMN_NAME) else null
        }
    }

    /**
     * Update Role
     */
    fun updateRole(id: Int, newName: String): Boolean {
        return connection.prepareStatement(UPDATE_ROLE).use { stmt ->
            stmt.setString(1, newName)
            stmt.setInt(2, id)
            stmt.executeUpdate() > 0
        }
    }

    /**
     * Delete Role
     */
    fun deleteRole(id: Int): Boolean {
        return connection.prepareStatement(DELETE_ROLE).use { stmt ->
            stmt.setInt(1, id)
            stmt.executeUpdate() > 0
        }
    }
}
