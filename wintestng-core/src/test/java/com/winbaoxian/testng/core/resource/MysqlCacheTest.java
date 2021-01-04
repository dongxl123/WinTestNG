package com.winbaoxian.testng.core.resource;

import com.winbaoxian.testng.BaseTest;
import com.winbaoxian.testng.model.core.resource.MysqlResourceSettings;
import org.testng.annotations.Test;

/**
 * @author dongxuanliang252
 * @date 2019-03-14 18:42
 */
public class MysqlCacheTest extends BaseTest {
    @Test
    public void testExec() {
        MysqlResourceSettings settings = new MysqlResourceSettings();
        settings.setUrl("jdbc:mysql://localhost:3306/baoxian?allowMultiQueries=true");
        settings.setUserName("root");
        settings.setPassword("123456");
        MysqlCache mysqlCache = new MysqlCache(settings);
        String sql = "select * \n" +
                "from a\n" +
                " limit 1;select * from \n" +
                "planbook_activity \n" +
                "limit 1;update a set \n" +
                "act_name ='dong; select test' \n" +
                "where id = 1;select * from a limit 1;";
        String sql2 = "( SELECT course_id FROM `excellent_course_section_tab_tag_course` WHERE course_id IN ( SELECT id FROM `excellent_course_pay_course` WHERE `is_sell` = '1' AND `is_deleted` = '0' AND `price` = '0.00' ) AND `tab_id` = '99' AND `is_free` = '1' ORDER BY order_num DESC ) UNION ( SELECT DISTINCT course_id FROM `excellent_course_section_tab_tag_course` WHERE course_id IN ( SELECT id FROM `excellent_course_pay_course` WHERE `is_sell` = '1' AND `is_deleted` = '0' AND `price` <> '0.00' ) AND `tab_id` = '99' AND `is_free` = '0' ORDER BY order_num DESC )";
        Object a = mysqlCache.execute(sql2, false);
        System.out.printf("111");
    }

}