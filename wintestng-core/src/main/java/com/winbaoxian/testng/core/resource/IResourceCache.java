package com.winbaoxian.testng.core.resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 17:28
 */
public interface IResourceCache<T> {

    T getCacheObject();

    String getConfigMd5();

}
