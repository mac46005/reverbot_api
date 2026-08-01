package com.example.exceptions 

class SqlINSERTException(from: String): Exception("Unable to retrieve ID from $from")