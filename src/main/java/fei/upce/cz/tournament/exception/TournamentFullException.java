package fei.upce.cz.tournament.exception;

public class TournamentFullException extends RuntimeException {
    public TournamentFullException(String message) {
        super(message);
    }
}