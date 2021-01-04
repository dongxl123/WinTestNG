package com.winbaoxian.testng.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 深拷贝工具类
 *
 * @Author DongXL
 * @Create 2016-12-03 15:18
 */
public enum CopyUtils {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(CopyUtils.class);

    /**
     * This method makes samples "deep clone" of any object it is given.
     */
    public <T> T deepClone(T object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            log.error("deepClone error", e);
            return null;
        }
    }

    /**
     * 浅拷贝
     *
     * @param object
     * @param <T>
     * @return
     */
    public <T> T shallowClone(T object) {
        try {
            return (T) BeanUtils.cloneBean(object);
        } catch (Exception e) {
            log.error("shallowClone error", e);
            return null;
        }
    }

}
