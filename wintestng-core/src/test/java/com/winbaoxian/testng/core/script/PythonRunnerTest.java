package com.winbaoxian.testng.core.script;

import com.winbaoxian.testng.BaseTest;
import com.winbaoxian.testng.model.core.action.normal.ScriptActionSetting;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author dongxuanliang252
 * @date 2020-07-20 15:26
 */
public class PythonRunnerTest extends BaseTest {

    @Resource
    private PythonRunner pythonRunner;

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder();
        String host = "";
        String user = "";
        String password = "";
        String db = "";
        sb.append("import io").append("\n");
        sb.append("import pymysql").append("\n");
        sb.append(String.format("connection = pymysql.connect('%s', '%s', '%s', db='%s', charset='utf8mb4', cursorclass=pymysql.cursors.DictCursor)", host, user, password, db)).append("\n");
        sb.append("cursor = connection.cursor()").append("\n");
        sb.append("cursor.execute(\"select * from sales_user limit 1\")").append("\n");
        sb.append("a1=cursor.fetchone()").append("\n");
        sb.append("connection.close()").append("\n");
        sb.append("from bs4 import BeautifulSoup").append("\n");
        sb.append("soup = BeautifulSoup(\"<p>Some<b>bad<i>HTML\")").append("\n");
        sb.append("dtest = soup.prettify()").append("\n");
        sb.append("a = 111").append("\n");
        sb.append("b = 12312421421222").append("\n");
        sb.append("c = 11.22").append("\n");
        sb.append("d = True").append("\n");
        sb.append("e = complex(2.0,11.2)").append("\n");
        sb.append("f = dtest").append("\n");
        sb.append("g = [[13,21],'232',True]").append("\n");
        sb.append("h = (1,3,4)").append("\n");
        sb.append("i = {1,3,4}").append("\n");
        sb.append("j = {'a':1,'b':True}").append("\n");
        sb.append("k = None").append("\n");
        ScriptActionSetting setting = new ScriptActionSetting();
        setting.setContent(sb.toString());
        setting.setExtractVars(Arrays.asList("a1", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"));
        Object o = pythonRunner.execute(setting);
        System.out.println(o.toString());
    }

    @Test
    public void testCode() {
        String mongoUrl = "";
        String code = "import pymongo\n" +
                "\n" +
                "client = pymongo.MongoClient(" + mongoUrl + ")\n" +
                "db = client.get_default_database()\n" +
                "a = db.get_collection('planbook_result').find_one()\n" +
                "b = True\n" +
                "c = 1 / 232 * 1123";
        ScriptActionSetting setting = new ScriptActionSetting();
        setting.setContent(code);
        setting.setExtractVars(Arrays.asList("a", "b", "c"));
        Object o = pythonRunner.execute(setting);
        System.out.println(o.toString());

    }
}
