package com.example.exceptions

class ForeignKeyViolationException(from: String = ""): Exception("POSTGRESQL:\tForeign key violation\t$from") {

}