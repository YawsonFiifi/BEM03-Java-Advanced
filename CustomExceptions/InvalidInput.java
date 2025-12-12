package CustomExceptions;

public class InvalidInput extends Exception{
    public InvalidInput(){
        super("Invalid Input");
    }

    public InvalidInput(String message){
        super(message);
    }

    public InvalidInput(String message, Throwable cause){
        super(message, cause);
    }

    public InvalidInput(Throwable cause){
        super(cause);
    }
}
