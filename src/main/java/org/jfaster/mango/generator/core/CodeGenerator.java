package org.jfaster.mango.generator.core;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.squareup.javapoet.*;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.Result;
import org.jfaster.mango.annotation.Results;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.util.Strings;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * @author ash
 */
public class CodeGenerator {

    private static final String COLUMNS = "COLUMNS";
    private static final String COLON = ":";
    private static final String INDENT = "    ";

    public static String generate(String tableName, List<String> columns,
                                  ClassName pojoType, List<String> properties,
                                  ClassName daoType, String keyProperty, TypeName keyPropertyType) throws Exception {

        String pojoParameter = pojoType.simpleName().substring(0, 1).toLowerCase()
                + pojoType.simpleName().substring(1, pojoType.simpleName().length());
        String keyColumn = getKeyColumn(keyProperty, properties, columns);

        TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(daoType.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(buildDBAnnotationSpec(tableName))
                .addField(buildStaticColumns(columns))
                .addMethod(buildInsertSpec(pojoType, pojoParameter, properties))
                .addMethod(buildSelectSpec(pojoType, keyPropertyType, keyProperty, keyColumn))
                .addMethod(buildUpdateSpec(pojoType, pojoParameter, properties, columns, keyProperty, keyColumn))
                .addMethod(buildDeleteSpec(pojoType, keyPropertyType, keyProperty, keyColumn));

        List<Tuple> tuples = getMappingTuples(columns, properties);
        if (!tuples.isEmpty()) {
            typeBuilder.addAnnotation(buildResultsAnnotationSpec(tuples));
        }

        return JavaFile.builder(daoType.packageName(),
                typeBuilder.build())
                .skipJavaLangImports(true)
                .indent(INDENT)
                .build()
                .toString();
    }

    private static String getKeyColumn(String keyProperty, List<String> properties, List<String> columns) {
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).equals(keyProperty)) {
                return columns.get(i);
            }
        }
        return null;
    }

    private static AnnotationSpec buildDBAnnotationSpec(String tableName) {
        return AnnotationSpec.builder(DB.class)
                .addMember("table", "$S", tableName).build();
    }

    private static FieldSpec buildStaticColumns(List<String> columns) {
        return FieldSpec.builder(String.class, COLUMNS)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", Joiner.on(", ").join(columns))
                .build();
    }

    private static MethodSpec buildInsertSpec(ClassName pojoType, String pojoParameter, List<String> properties) {
        List<String> colonProperties = Lists.newArrayList();
        for (String property : properties) {
            colonProperties.add(COLON + property);
        }
        return MethodSpec.methodBuilder("add" + pojoType.simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(SQL.class)
                        .addMember("value", "$S + $L + $S",
                                "insert into #table(",
                                COLUMNS,
                                ") values(" + Joiner.on(", ").join(colonProperties) + ")")
                        .build())
                .addParameter(pojoType, pojoParameter)
                .build();
    }

    private static MethodSpec buildSelectSpec(ClassName pojoType, TypeName keyPropertyType,
                                              String keyProperty, String keyColumn) {
        return MethodSpec.methodBuilder("get" + pojoType.simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(SQL.class)
                        .addMember("value", "$S + $L + $S",
                                "select ",
                                COLUMNS,
                                " from #table where " + keyColumn + " = :1")
                        .build())
                .addParameter(keyPropertyType, keyProperty)
                .returns(pojoType)
                .build();
    }

    private static MethodSpec buildUpdateSpec(ClassName pojoType, String pojoParameter,
                                              List<String> properties, List<String> columns,
                                              String keyProperty, String keyColumn) {
        List<String> items = Lists.newArrayList();
        for (int i = 0; i < properties.size(); i++) {
            String property = properties.get(i);
            if (!keyProperty.equals(property)) {
                String column = columns.get(i);
                items.add(column + "=" + COLON + property);
            }
        }
        return MethodSpec.methodBuilder("update" + pojoType.simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(SQL.class)
                        .addMember("value", "$S",
                                "update #table set " + Joiner.on(", ").join(items) + " where " + keyColumn + " = :" + keyColumn)
                        .build())
                .addParameter(pojoType, pojoParameter)
                .returns(Boolean.TYPE)
                .build();
    }

    private static MethodSpec buildDeleteSpec(ClassName pojoType, TypeName keyPropertyType,
                                              String keyProperty, String keyColumn) {
        return MethodSpec.methodBuilder("delete" + pojoType.simpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(SQL.class)
                        .addMember("value", "$S",
                                "delete from #table where " + keyColumn + " = :1")
                        .build())
                .addParameter(keyPropertyType, keyProperty)
                .returns(Boolean.TYPE)
                .build();
    }

    private static List<Tuple> getMappingTuples(List<String> columns, List<String> fields) {
        List<Tuple> tuples = Lists.newArrayList();
        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);
            String field = fields.get(i);
            String underscoreField = Strings.underscoreName(field);

            if (!column.toLowerCase().equals(field.toLowerCase())
                    && !column.toLowerCase().equals(underscoreField.toLowerCase())) {
                tuples.add(new Tuple(column, field));
            }
        }
        return tuples;
    }

    private static AnnotationSpec buildResultsAnnotationSpec(List<Tuple> tuples) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(Results.class);
        for (Tuple tuple : tuples) {
            builder.addMember("value", "$L", AnnotationSpec.builder(Result.class)
                    .addMember("column", "$S", tuple.getColumn()).addMember("property", "$S", tuple.field)
                    .build());
        }
        return builder.build();
    }

    private static class Tuple {

        public Tuple(String column, String field) {
            this.column = column;
            this.field = field;
        }

        private String column;

        private String field;

        public String getColumn() {
            return column;
        }

        public String getField() {
            return field;
        }

    }

    public static void main(String[] args) throws Exception {
    }

}
