package com.winbaoxian.testng.utils;

import com.winbaoxian.testng.BaseTest;
import org.testng.annotations.Test;

/**
 * @author dongxuanliang252
 * @date 2019-04-01 17:00
 */
public class ConsoleLogUtilsTest extends BaseTest {

    @Test
    public void testGetLogger() {
        String uuid = UUIDUtil.INSTANCE.randomUUID();
        new Thread(() -> {
            int size = 0;
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(size);
                String a = ConsoleLogUtils.INSTANCE.read(uuid, size);
                if (a.length() == 0) {
                    continue;
                }
                size += a.length();
                System.out.println("打印数据:" + a + "，长度:" + size);
            }
        }).start();

        for (int j = 0; j < 10; j++) {
            new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    System.out.println("write");
                    ConsoleLogUtils.INSTANCE.log(uuid, UUIDUtil.INSTANCE.randomUUID());
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }

}
