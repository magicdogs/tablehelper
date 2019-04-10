package com.magicdogs.tablehelper.sqlsource;

import com.magicdogs.tablehelper.TableNameHelper;
import com.magicdogs.tablehelper.options.Options;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.io.StringReader;
import java.util.List;
import java.util.Objects;

/**
 * @author magic
 * @date 2019/3/5/005 14:49
 * @version 1.0.0
 * Description ModifyTableSqlSource
 */
public class ModifyTableSqlSource implements SqlSource {

    private static final String SQL = "sql";
    public static final String SUFFIX_TOKEN = "`";
    private SqlSource sqlSource;
    private Options options;

    public ModifyTableSqlSource(SqlSource sqlSource){
        this.sqlSource = sqlSource;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        try {
            Statement statement = CCJSqlParserUtil.parse(new StringReader(boundSql.getSql()));
            this.options = TableNameHelper.take();
            processStatement(statement,boundSql);
        } catch (JSQLParserException e) {
            /*e.printStackTrace();*/
        }
        return boundSql;
    }

    private void processStatement(Statement statement, BoundSql boundSql) {
        if(statement instanceof Select){
            Select select = (Select) statement;
            processSelect(select,boundSql);
        }else if(statement instanceof Update){
            Update update = (Update) statement;
            processUpdate(update,boundSql);
        }else if(statement instanceof Insert){
            Insert insert = (Insert) statement;
            processInsert(insert,boundSql);
        }else if(statement instanceof Delete){
            Delete delete = (Delete) statement;
            processDelete(delete,boundSql);
        }
    }

    private void setSqlValue(BoundSql boundSql, String sql) {
        MetaObject metaObject = SystemMetaObject.forObject(boundSql);
        metaObject.setValue(SQL,sql);
    }

    private void processDelete(Delete delete, BoundSql boundSql) {
        List<Table> tables = delete.getTables();
        if(!tables.isEmpty()){
            tables.forEach(table -> {
                table.setName(getTableName(table));
            });
        }
        Table table = delete.getTable();
        table.setName(getTableName(table));
        setSqlValue(boundSql,delete.toString());
    }

    private String getTableName(Table table) {
        String origin = table.getName();
        if(Objects.isNull(options)){
            return origin;
        }
        if(Objects.isNull(options.getSuffix()) && options.getRenames().isEmpty()){
            return origin;
        }
        if(Objects.nonNull(options.getExcludes())
                && options.getExcludes().contains(replaceTableToken(origin))){
            return origin;
        }
        if(options.getRenames().containsKey(origin)){
            return options.getRenames().getOrDefault(origin,origin);
        }
        if(Objects.nonNull(options.getSuffix())){
            if(origin.endsWith(SUFFIX_TOKEN)){
                StringBuilder builder = new StringBuilder(origin);
                builder.deleteCharAt(builder.length() - 1)
                        .append(options.getSuffix())
                        .append(SUFFIX_TOKEN);
                return builder.toString();
            }
            return origin.concat(options.getSuffix());
        }
        return origin;
    }

    private String replaceTableToken(String origin) {
        return origin.replaceAll(SUFFIX_TOKEN,"");
    }

    private void processInsert(Insert insert, BoundSql boundSql) {
        Table table = insert.getTable();
        table.setName(getTableName(table));
        setSqlValue(boundSql,insert.toString());
    }

    private void processUpdate(Update update, BoundSql boundSql) {
        List<Table> tables = update.getTables();
        if(!tables.isEmpty()){
            tables.forEach(table -> {
                table.setName(getTableName(table));
            });
            setSqlValue(boundSql,update.toString());
        }
    }

    private void processSelect(Select select, BoundSql boundSql) {
        StringBuilder buffer = new StringBuilder();
        ExpressionDeParser expressionDeParser = new ExpressionDeParser();
        SelectDeParser parser = new SelectDeParser(expressionDeParser, buffer) {
            @Override
            public void visit(Table table) {
                table.setName(getTableName(table));
                super.visit(table);
            }
        };
        expressionDeParser.setSelectVisitor(parser);
        expressionDeParser.setBuffer(buffer);
        select.getSelectBody().accept(parser);
        setSqlValue(boundSql,buffer.toString());
    }
}
