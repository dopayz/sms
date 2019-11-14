package com.yudu.sms.util;

import com.yudu.sms.config.DbConfig;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.jdbc.SqlRunner;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.*;

@Component
public class SelfSearch {
    @Autowired
    private DbConfig dbConfig;

    public Map<String, Object> execSql(String prepareSql, Map<String, Object> condition) throws Exception {
        Map<String, Object> page = new HashMap<>();
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = this.createSqlSessionFactory();
            sqlSession = sqlSessionFactory.openSession();
            Connection connection = sqlSession.getConnection();
            SqlRunner sqlRunner = new SqlRunner(connection);
            String realSql = this.toSql(prepareSql, condition);
            page.put("results", sqlRunner.selectAll(realSql));
            return page;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("");
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    /**
     * 配置数据库信息，获取SqlSessionFactory
     * @return
     */
    private SqlSessionFactory createSqlSessionFactory() {

        String driver = dbConfig.dbDriverClassName;
        String url = dbConfig.dbUrl;
        String username = dbConfig.dbUserName;
        String password = dbConfig.dbPassword;
        //创建连接池
        DataSource dataSource = new PooledDataSource(driver, url, username, password);
        //事务
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        //创建环境
        Environment environment = new Environment("development", transactionFactory, dataSource);
        //创建配置
        Configuration configuration = new Configuration(environment);
        //开启驼峰规则
        configuration.setMapUnderscoreToCamelCase(true);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        return sqlSessionFactory;
    }
    private String toSql(String prepareSql, Map<String, Object> condition) throws Exception {

        XMLLanguageDriver driver = new XMLLanguageDriver();
        String script = "<script>" + prepareSql + "</script>";
        SqlSource sqlSource;
        BoundSql boundSql;
        SqlSessionFactory sqlSessionFactory = this.createSqlSessionFactory();
        try {
            sqlSource = driver.createSqlSource(sqlSessionFactory.getConfiguration(), script, condition.getClass());
            boundSql = sqlSource.getBoundSql(condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("");
        }

        Configuration configuration = sqlSessionFactory.getConfiguration();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        if (parameterMappings.size() == 0 || parameterObject == null) {
            return sql;
        }

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    sql = sql.replaceFirst("\\?", getParameterValue(obj));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    Object obj = boundSql.getAdditionalParameter(propertyName);
                    sql = sql.replaceFirst("\\?", getParameterValue(obj));
                }
            }
        }
        return sql;
    }
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else if (obj instanceof String[]){
            String str = "";
            for(String s :(String[]) obj){
                str += "'" + s.trim() + "',";
            }
            value = str.substring(0,str.length()-1);
        }else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }
}
