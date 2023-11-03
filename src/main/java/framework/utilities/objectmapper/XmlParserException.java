package framework.utilities.objectmapper;

public class XmlParserException extends RuntimeException {

    public XmlParserException(String message, Throwable linkedException) {
        super(message, linkedException);
    }
}
