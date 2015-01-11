package uk.co.tomkdickinson.twitter.search;

public class InvalidQueryException extends Exception{

    public InvalidQueryException(String query) {
        super("Query string '"+query+"' is invalid");
    }
}