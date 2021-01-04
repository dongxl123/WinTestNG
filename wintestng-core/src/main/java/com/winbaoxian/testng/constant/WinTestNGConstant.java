package com.winbaoxian.testng.constant;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 18:10
 */
public interface WinTestNGConstant {
    String OPEN_PAREN = "(";
    String CLOSE_PAREN = ")";
    String OPEN_BRACE = "{";
    String CLOSE_BRACE = "}";
    String OPEN_BRACKET = "[";
    String CLOSE_BRACKET = "]";
    String CHAR_DOT = ".";
    String CHAR_COMMA = ",";
    String CHAR_COLON = ":";
    String CHAR_SEMICOLON = ";";
    String CHAR_SPACE = " ";
    String CHAR_RIGHT_ARROW = "→";
    String CHAR_DOUBLE_QUOTA = "\"";
    String CHAR_SINGLE_QUOTA = "'";
    String CHAR_SEPARATOR = "/";
    String CHAR_RIGHT_SLASH = "\\";
    String CHAR_AND = "&";
    String CHAR_EQUAL = "=";
    String FREEMARKER_VARIABLE_PREFIX = "${";
    String FREEMARKER_CONDITION_PREFIX = "<#";
    String LINE_SEPARATOR = System.getProperty("line.separator").intern();
    String LOG_PREFIX_UUID = "%s";
    String LOG_PREFIX_TESTCASE = "TestCase(%s)";
    String LOG_PREFIX_FLOW = "(Title:%s)";
    String LOG_PREFIX_ACTION = "%s<%s>";
    String HTTP_HEADER_COOKIE = "Cookie";
    String LOG_TITLE_START = "start";
    String LOG_TITLE_END = "end";
    String LOG_TITLE_GLOBAL_PARAMS_CONFIG = "获取全局配置";
    String LOG_TITLE_BASE_PARAMS_CONFIG = "获取基础数据配置";
    String LOG_TITLE_PRE_ACTIONS = "执行前置动作";
    String LOG_TITLE_DATA_PREPARATION = "准备测试用例";
    String LOG_TITLE_MAIN_ACTIONS = "执行主流程";
    String LOG_TITLE_POST_ACTIONS = "执行后置动作";
    String LOG_SUB_TITLE_ACTIONS_FETCH_TEST_CASE = "获取测试用例";
    String LOG_SUB_TITLE_ACTIONS_FETCH_CONFIG = "获取配置";
    String LOG_SUB_TITLE_ACTIONS_GLOBAL_DATA = "全局数据";
    String STRING_HTTP = "http";
    String CONSOLE_LOG_END_STRING = "(*^▽^*)";
    int TPL_CACHE_LOCK_TIMEOUT = 60 * 10;
    String REPORT_EMAIL_TITLE = "(%s)%s测试用例%s";
    int QUALITY_SCORE_DATA_CACHE_LIFE_TIME = 60 * 60;

}
