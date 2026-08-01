package com.example.exceptions

class UniqueConstrainViolationException(from: String = ""): Exception("POSTGRESQL:\tUnique constraint violation\t$from"){

}