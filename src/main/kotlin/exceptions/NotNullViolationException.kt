package com.example.exceptions

class NotNullViolationException(from: String = ""): Exception("POSTGRESQL:\tNot null violation\t$from") {

}
