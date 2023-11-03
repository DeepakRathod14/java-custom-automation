package framework.utilities.graphqlmapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import framework.utilities.graphqlmapper.graphql.GraphQlArgs;
import framework.utilities.graphqlmapper.graphql.GraphQlField;
import framework.utilities.graphqlmapper.graphql.GraphQlObject;
import framework.utilities.graphqlmapper.graphql.GraphQlQuery;

/**
 * Converts query object to valid graphQL query string. It will be used it GraphQLQueryDto builder with VariablesDto to
 * build a graphQL request. Root query class should be annotated with @GraphQLQuery(name="your query name"). Annotate
 * fields you need with {@literal @}GraphQLField(name="field name"). Annotate fields represents inner objects contains
 * graphQL annotations with {@literal @}GraphQLObject(name="object name", value="string with variables"). Field name
 * will be used if no name provided with annotation. See examples in RecentQueryDto.class
 */
public class GraphQlConvert {

    protected static final Logger LOGGER = LogManager.getLogger();

    private static final String OPEN_BRACES = " {";
    private static final String CLOSE_BRACES = "}";
    private static final String COMMA_SEPARATOR = ",";
    private static final String OPEN_PARENTHESE = "(";
    private static final String CLOSED_PARANTHESE = ")";
    private static final String ARGS_DELIMITER = " :";
    private static final int FIRST_ARGUMENT = 0;

    private GraphQlConvert() {
    }

    /**
     * Parse annotated classes to valid graphQL query string.
     */
    public static String toQueryString(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(GraphQlQuery.class)) {
            throw new ClassNotAnnotatedException(
                "query root class should be annotated with @GraphQLQuery(name=your query name)");
        }
        StringBuilder graphQlQuery = new StringBuilder();

        graphQlQuery.append("query ").append(clazz.getAnnotation(GraphQlQuery.class).name()).append(
            OPEN_BRACES);
        parse(clazz, graphQlQuery);
        graphQlQuery.append(CLOSE_BRACES);

        return graphQlQuery.toString();
    }


    /**
     * Parse next query class according to it type. If provided class annotated as object it will be enclosed with
     * braces. If provided class annotated as field it will be separated by comma.
     */
    private static void parse(Class<?> clazz, StringBuilder graphQlQuery) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(GraphQlObject.class)) {
                GraphQlObject annotation = field.getAnnotation(GraphQlObject.class);
                // use field name if no name provided
                String name = (annotation.name().equals(Strings.EMPTY)) ? field.getName() : annotation.name();
                graphQlQuery.append(name);
                appendArguments(clazz, field, annotation, graphQlQuery);
                parseParameterized(field, graphQlQuery);
                graphQlQuery.append(annotation.empty() ? "" : CLOSE_BRACES);
            }

            if (field.isAnnotationPresent(GraphQlField.class)) {
                GraphQlField annotation = field.getAnnotation(GraphQlField.class);
                // use field name if no name provided
                String name = (annotation.name().equals(Strings.EMPTY)) ? field.getName() : annotation.name();

                graphQlQuery.append(name);

                parseParameterized(field, graphQlQuery);
                graphQlQuery.append(COMMA_SEPARATOR);
            }
        }
    }

    /**
     * Parse parametrized graphql Sometimes you have your field defined as list or collection.
     *
     * <p>GraphQLObject private List of YourClassDto someField;
     *
     * <p>You need to check generic class type and extract YourClassDto class type from generic List or Collection
     *     or Map declaration. Or just get field type if no generic declarations provided.
     *
     * <p>The only reason for the method exists - reuse your Dtos for query/response processing.
     *
     */
    private static void parseParameterized(Field field, StringBuilder qraphQlQuery) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            try {
                // Extract List/Collection/Map parameter class name and use it for process next class
                parse(Class.forName(parameterizedType.getActualTypeArguments()[FIRST_ARGUMENT].getTypeName()),
                    qraphQlQuery);
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage());
            }
        } else {
            parse(field.getType(), qraphQlQuery);
        }
    }

    private static void appendArguments(Class<?> clazz, Field field, GraphQlObject object, StringBuilder query) {
        if (field.isAnnotationPresent(GraphQlArgs.class)) {
            GraphQlArgs args = field.getAnnotation(GraphQlArgs.class);
            if (args.replaceArgs()) {
                query.append(OPEN_PARENTHESE)
                    .append(args.argName())
                    .append(ARGS_DELIMITER)
                    .append(retrieveArgumentValues(clazz))
                    .append(CLOSED_PARANTHESE);
            } else {
                query.append(OPEN_PARENTHESE).append(args.argName()).append(CLOSED_PARANTHESE);
            }
        }
        query.append(object.empty() ? "" : OPEN_BRACES);
    }

    private static String retrieveArgumentValues(Class<?> clazz) {
        Method getArgumentsValue = null;
        String result = null;
        try {
            getArgumentsValue = clazz.getDeclaredMethod("argumentsToString");
            result = (String) getArgumentsValue.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(e);
        }
        return result;
    }
}
