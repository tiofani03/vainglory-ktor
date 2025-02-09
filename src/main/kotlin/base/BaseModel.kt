package com.tiooooo.base

import java.sql.Connection
import java.sql.ResultSet

abstract class BaseModel(protected val connection: Connection) {
    abstract fun initialize()

    fun executeUpdate(query: String, vararg params: Any) {
        connection.prepareStatement(query).use { stmt ->
            params.forEachIndexed { index, param ->
                stmt.setObject(index + 1, param)
            }
            stmt.executeUpdate()
        }
    }

    fun <T> executeQuery(query: String, vararg params: Any, mapper: (ResultSet) -> T): List<T> {
        return connection.prepareStatement(query).use { stmt ->
            params.forEachIndexed { index, param ->
                stmt.setObject(index + 1, param)
            }
            stmt.executeQuery().use { rs ->
                val results = mutableListOf<T>()
                while (rs.next()) {
                    results.add(mapper(rs))
                }
                results
            }
        }
    }
}
