package com.winbaoxian.testng.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 17:29
 */
public enum SqlResultSetUtils {

    INSTANCE;

    /**
     * 返回LIST
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> readResultList(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnLabel(i + 1), resultSet.getObject(i + 1));
            }
            list.add(map);
        }
        return list;
    }

    public Map<String, Object> readOneResult(ResultSet resultSet) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        if (resultSet.next()) {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                map.put(metaData.getColumnLabel(i + 1), resultSet.getObject(i + 1));
            }
        }
        return map;
    }

}
