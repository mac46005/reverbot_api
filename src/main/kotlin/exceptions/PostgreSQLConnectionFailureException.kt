package com.example.exceptions

class PostgreSQLConnectionFailureException(from: String = ""): Exception("POSTGRESQL:\tConnection Failure\t$from") {

}
