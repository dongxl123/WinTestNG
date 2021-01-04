package com.winbaoxian.testng.core.resource;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.resource.MysqlResourceSettings;
import com.winbaoxian.testng.utils.SqlResultSetUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 15:46
 */

public class MysqlCache extends AbstractResourceCache<DataSource> implements ISqlOperation {

    private static final Logger log = LoggerFactory.getLogger(MysqlCache.class);

    private DataSource dataSource;

    public MysqlCache(MysqlResourceSettings settings) {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(settings.getUrl());
        dataSource.setInitialSize(ResourceConfigConstant.MYSQL_POOL_MIN_SIZE);
        dataSource.setMinIdle(ResourceConfigConstant.MYSQL_POOL_MIN_SIZE);
        dataSource.setMaxActive(ResourceConfigConstant.MYSQL_POOL_MAX_SIZE);
        dataSource.setMaxIdle(ResourceConfigConstant.MYSQL_POOL_MAX_SIZE);
        dataSource.setUsername(settings.getUserName());
        dataSource.setPassword(settings.getPassword());
        dataSource.setTestWhileIdle(ResourceConfigConstant.MYSQL_TEST_WHILE_IDLE);
        dataSource.setValidationQuery(ResourceConfigConstant.MYSQL_VALIDATION_QUERY);
        dataSource.setTimeBetweenEvictionRunsMillis(ResourceConfigConstant.MYSQL_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
        dataSource.setMinEvictableIdleTimeMillis(ResourceConfigConstant.MYSQL_MIN_EVICTABLE_IDLE_TIME_MILLIS);
        dataSource.setNumTestsPerEvictionRun(ResourceConfigConstant.MYSQL_POOL_MAX_SIZE);
        dataSource.setTestOnBorrow(ResourceConfigConstant.MYSQL_TEST_ON_BORROW);
        this.dataSource = dataSource;
        this.settings = settings;
    }

    @Override
    public DataSource getCacheObject() {
        return dataSource;
    }

    @Override
    public Object execute(String sql, boolean fetchOne) {
        sql = StringUtils.trim(sql);
        List<String> sqlList = tryToParseMultiSql(sql);
        if (sqlList.size() > 1) {
            return executeMulti(sqlList, fetchOne);
        }
        return executeSingle(sql, fetchOne);
    }

    private List<String> tryToParseMultiSql(String sql) {
        List<String> sqlList = new ArrayList<>();
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        if (CollectionUtils.isNotEmpty(sqlStatements)) {
            for (SQLStatement sqlStatement : sqlStatements) {
                sqlList.add(sqlStatement.toString());
            }
        }
        return sqlList;
    }

    private Object executeMulti(List<String> sqlList, boolean fetchOne) {
        if (CollectionUtils.isEmpty(sqlList)) {
            return null;
        }
        List<Object> retList = new ArrayList<>();
        for (String sql : sqlList) {
            retList.add(executeSingle(sql, fetchOne));
        }
        return retList;
    }

    private Object executeSingle(String sql, boolean fetchOne) {
        sql = StringUtils.trim(sql);
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            if (isSelectSQL(sql)) {
                ResultSet resultSet = stmt.executeQuery(sql);
                if (fetchOne) {
                    return SqlResultSetUtils.INSTANCE.readOneResult(resultSet);
                } else {
                    return SqlResultSetUtils.INSTANCE.readResultList(resultSet);
                }
            } else {
                connection.setAutoCommit(false);
                int ret = stmt.executeUpdate(sql);
                connection.commit();
                return ret;
            }
        } catch (SQLException e) {
            throw new WinTestNgException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    log.error("stmt关闭出错", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("connection关闭出错", e);
                }
            }
        }
    }

    private boolean isSelectSQL(String sql) {
        SQLStatement sqlStatement = SQLUtils.parseSingleMysqlStatement(sql);
        if (sqlStatement != null && sqlStatement instanceof SQLSelectStatement) {
            return true;
        }
        return false;
    }

}
