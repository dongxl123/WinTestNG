package com.winbaoxian.testng.utils;

import com.winbaoxian.testng.constant.WinTestNGConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;

/**
 * @author dongxuanliang252
 * @date 2019-04-01 16:38
 */
public enum ConsoleLogUtils {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(ConsoleLogUtils.class);

    public void logE(String uuid, Throwable exception) {
        if (exception == null) {
            return;
        }
        String text = WinTestNGLogUtils.INSTANCE.getErrorString(exception);
        log(uuid, text);
    }

    public void log(String uuid, String text) {
        FileWriter writer = null;
        try {
            String path = getFilePath(uuid);
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(path, true);
            writer.append(DateFormatUtils.INSTANCE.format(new Date()) + WinTestNGConstant.CHAR_SPACE + text + WinTestNGConstant.LINE_SEPARATOR);
            writer.flush();
        } catch (IOException e) {
            log.error("流关闭异常", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("流关闭异常", e);
                }
            }
        }
    }

    public String read(String uuid, long offset) {
        BufferedReader reader = null;
        try {
            String path = getFilePath(uuid);
            reader = new BufferedReader(new FileReader(path));
            reader.skip(offset);
            StringBuilder sb = new StringBuilder();
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString).append(WinTestNGConstant.LINE_SEPARATOR);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            log.error("文件不存在", e);
        } catch (IOException e) {
            log.error("流关闭异常", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("流关闭异常", e);
                }
            }
        }
        return null;
    }

    private String getFilePath(String uuid) throws IOException {
        String path = String.format("cache/%s/%s", uuid.substring(0, 2), uuid.substring(2, 4));
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String filePath = path + "/" + uuid;
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return filePath;
    }

}
