package com.winbaoxian.testng.core.resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-08 9:57
 */
public interface ISqlOperation {

    Object execute(String sql, boolean fetchOne);

}
