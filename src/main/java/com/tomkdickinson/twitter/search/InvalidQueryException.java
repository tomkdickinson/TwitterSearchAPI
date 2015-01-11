package com.tomkdickinson.twitter.search;

public class InvalidQueryException extends Exception{

    public InvalidQueryException() {
        super("Query String can not be null or empty");
    }
}
