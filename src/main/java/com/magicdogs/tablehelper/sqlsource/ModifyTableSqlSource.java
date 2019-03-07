package com.magicdogs.tablehelper.sqlsource;

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

/**
 * @author magic
 * @date 2019/3/5/005 14:49
 * @version 1.0.0
 * Description ModifyTableSqlSource
 */
public class ModifyTableSqlSource implements SqlSource {

    private static final String SQL = "sql";
    private SqlSource sqlSource;
    private String suffix;

    public ModifyTableSqlSource(SqlSource sqlSource,String suffix){
        this.sqlSource = sqlSource;
        this.suffix = suffix;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        try {
            Statement statement = CCJSqlParserUtil.parse(new StringReader(boundSql.getSql()));
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
                table.setName(table.getName().concat(this.suffix));
            });
        }
        Table table = delete.getTable();
        table.setName(table.getName().concat(this.suffix));
        setSqlValue(boundSql,delete.toString());
    }

    private void processInsert(Insert insert, BoundSql boundSql) {
        Table table = insert.getTable();
        table.setName(table.getName().concat(this.suffix));
        setSqlValue(boundSql,insert.toString());
    }

    private void processUpdate(Update update, BoundSql boundSql) {
        List<Table> tables = update.getTables();
        if(!tables.isEmpty()){
            tables.forEach(table -> {
                table.setName(table.getName().concat(this.suffix));
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
                String originTableName = table.getName();
                table.setName(originTableName.concat(suffix));
                super.visit(table);
            }
        };
        expressionDeParser.setSelectVisitor(parser);
        expressionDeParser.setBuffer(buffer);
        select.getSelectBody().accept(parser);
        setSqlValue(boundSql,buffer.toString());
    }
}