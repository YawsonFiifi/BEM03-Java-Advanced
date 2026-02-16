package Utils;

public interface RegexConstants {
    String ACCOUNT_ID = "(?i)acc[0-9]{3}";
    String PHONE_NUMBER = "(\\+\\d{2,3}\\s?|0)[1-9]\\d{8}";
    String ADDRESS = "((\\w+\\s*)+,\\s*)*(\\w+\\s*)+";
    String AGE = "[1-9]\\d*";
    String NUMBER = "\\d+";
    String NAME = "([A-Za-z]*\\s+)*[A-Za-z]+";
    String MONEY = "\\$?[1-9]\\d*(\\.\\d{1,2})?";
    String YES_OR_NO = "(?i)([yY](es)?|[Nn]o?)";
}

