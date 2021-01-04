package com.winbaoxian.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @author dongxuanliang252
 * @date 2019-03-14 18:42
 */

@SpringBootTest
public class BaseTest extends AbstractTestNGSpringContextTests {

    protected final Logger log = LoggerFactory.getLogger(getClass());

}
