package com.example.exceptions

import org.h2.mode.FunctionsPostgreSQL
import org.postgresql.util.PSQLException


// postgresql.org/docs/current/errcodes-appendix.html
object PSqlExceptionHandler {
    fun handle(e: PSQLException, from: String = ""): Exception {
        when (e.sqlState) {
            "23505" -> {
                return UniqueConstrainViolationException(from)
            }
            "23503" -> {
                return ForeignKeyViolationException(from)
            }
            "23502" -> {
                return NotNullViolationException(from)
            }
            "8006" -> {
                return PostgreSQLConnectionFailureException(from)
            }
            else -> {
                return Exception("Unknown exception")
            }
        }
    }
}