public class UsernameDoesNotExistException extends Exception {

    @Override
    public String getMessage() {
        return "Seems you've misspelled you username.";
    }
}
