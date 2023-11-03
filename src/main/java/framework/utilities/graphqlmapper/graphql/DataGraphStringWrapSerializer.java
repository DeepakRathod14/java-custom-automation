package framework.utilities.graphqlmapper.graphql;

public class DataGraphStringWrapSerializer implements DataGraphSerializer<String> {

    public String serialize(String value) {
        return "\"" + value + "\"";
    }

}
