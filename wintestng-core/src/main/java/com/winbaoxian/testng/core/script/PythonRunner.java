package com.winbaoxian.testng.core.script;

import com.winbaoxian.testng.model.core.action.normal.ScriptActionSetting;
import com.winbaoxian.testng.utils.JythonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2020-07-20 14:40
 */
@Component
public class PythonRunner implements IScriptLangRunner {

    @Value("${project.jythonExtPackages:[]}")
    private String[] jythonExtPackages;

    @PostConstruct
    public void init() {
        if (ArrayUtils.isEmpty(jythonExtPackages)) {
            return;
        }
        PySystemState sys = Py.getSystemState();
        for (String extPackage : jythonExtPackages) {
            sys.path.add(extPackage);
        }
    }

    @Override
    public Object execute(ScriptActionSetting setting) {
        PythonInterpreter pyInterp = new PythonInterpreter();
        pyInterp.exec(setting.getContent());
        Map<String, Object> map = new HashMap<>();
        for (String var : setting.getExtractVars()) {
            PyObject pyObject = pyInterp.get(var);
            map.put(var, JythonUtils.INSTANCE.toJavaObjectFromPyObject(pyObject));
        }
        return map;
    }

}
