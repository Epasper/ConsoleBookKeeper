public class IncorrectPasswordException extends Exception {

    @Override
    public String getMessage() {
        return "You won't go far trying to log in with that password. Try again.";
    }
}
