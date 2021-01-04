package com.winbaoxian.testng.utils;

import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.enums.RedisSupportCommandType;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.resource.RedisCommandDTO;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-12 10:17
 */
public enum RedisShellUtils {

    INSTANCE;

    private static final String SPECIAL_CHAR_STRING = WinTestNGConstant.CHAR_DOUBLE_QUOTA + WinTestNGConstant.CHAR_SINGLE_QUOTA;


    /**
     * @param command
     * @return
     */
    public RedisCommandDTO parseShellCommand(String command) {
        if (StringUtils.isBlank(command)) {
            throw new WinTestNgException("redis command can not be null");
        }
        String[] parts = parseParts(command);
        if (ArrayUtils.isEmpty(parts)) {
            throw new WinTestNgException(String.format("redis command is error: %s", command));
        }
        String typeStr = parts[0];
        RedisSupportCommandType commandType = RedisSupportCommandType.getByName(typeStr);
        if (commandType == null || RedisSupportCommandType.RedisReplyType.NOT_SUPPORTED.equals(commandType.getReplyType())) {
            throw new WinTestNgException(String.format("redis command(%s) is not supported: %s", typeStr, command));
        }
        RedisCommandDTO commandDTO = new RedisCommandDTO();
        commandDTO.setCommandType(commandType);
        commandDTO.setArgs(ArrayUtils.subarray(parts, 1, parts.length));
        return commandDTO;
    }

    private String[] parseParts(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList();
        String separator = WinTestNGConstant.CHAR_SPACE;
        //start = -1 表明未找到起始位置
        int start = -1;
        //标识特殊符号-单引号、双引号
        boolean specialFlag = false;
        char specialChar = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            //该字符是分隔符
            if (separator.indexOf(c) != -1) {
                if (start != -1) {
                    //start!=-1，则找到结束分割符
                    //前面有特殊符号，结束也有该特殊符号
                    if (specialFlag && i > 0 && specialChar == str.charAt(i - 1) && isSpecialChar(str, i - 1)) {
                        list.add(substring(str, start + 1, i - 1));
                        specialFlag = false;
                        //重新标记
                        start = -1;
                    } else if (!specialFlag) {
                        list.add(substring(str, start, i));
                        //重新标记
                        start = -1;
                    }
                }
            } else {
                //最后一个字符
                if (i == str.length() - 1) {
                    //前面有计数
                    if (start != -1) {
                        //特殊符号匹配到
                        if (specialFlag && specialChar == str.charAt(i) && isSpecialChar(str, i)) {
                            list.add(substring(str, start + 1, i));
                        } else {
                            list.add(substring(str, start, str.length()));
                        }
                    } else {
                        //前面无计数
                        list.add(substring(str, i, str.length()));
                    }
                } else {
                    if (isSpecialChar(str, i)) {
                        //该字符是特殊符号
                        specialChar = str.charAt(i);
                        specialFlag = true;
                    }
                    if (start == -1) {
                        start = i;
                    }
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }


    private boolean isSpecialChar(String str, int index) {
        if (index < 0) {
            return false;
        }
        char c = str.charAt(index);
        if (SPECIAL_CHAR_STRING.indexOf(c) != -1) {
            //该字符是特殊符号
            if (index > 0) {
                //前面有\ ，不标识
                char perChar = str.charAt(index - 1);
                if (WinTestNGConstant.CHAR_RIGHT_SLASH.equals(String.valueOf(perChar))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private String substring(String str, int fromIndex, int endIndex) {
        if (StringUtils.isBlank(str) || fromIndex >= endIndex || str.length() < endIndex) {
            return null;
        }
        return StringEscapeUtils.unescapeJava(str.substring(fromIndex, endIndex));
    }


}
