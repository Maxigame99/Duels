package it.maxigame.duels.serializers.error;

public class InvalidSerializationException extends Exception {
    public InvalidSerializationException() {
        super();
    }
    public InvalidSerializationException(String message) {
        super(message);
    }
}
