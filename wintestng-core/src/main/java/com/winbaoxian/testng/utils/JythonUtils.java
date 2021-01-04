package com.winbaoxian.testng.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.complex.Complex;
import org.bson.types.ObjectId;
import org.python.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author dongxuanliang252
 * @date 2020-07-20 20:08
 */
public enum JythonUtils {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(JythonUtils.class);

    public Object toJavaObjectFromPyObject(PyObject pyObject) {
        if (pyObject instanceof PyBoolean) {
            return ((PyBoolean) pyObject).getBooleanValue();
        } else if (pyObject instanceof PyInteger) {
            return pyObject.asInt();
        } else if (pyObject instanceof PyLong) {
            return pyObject.asLong();
        } else if (pyObject instanceof PyFloat) {
            return pyObject.asDouble();
        } else if (pyObject instanceof PyComplex) {
            PyComplex pyComplex = (PyComplex) pyObject;
            return new Complex(pyComplex.getReal().getValue(), pyComplex.getImag().getValue());
        } else if (pyObject instanceof PyString) {
            return pyObject.asString();
        } else if (pyObject instanceof PyObjectDerived) {
            PyObjectDerived pyObjectDerived = (PyObjectDerived) pyObject;
            String name = pyObjectDerived.getType().getName();
            try {
                if (StringUtils.equals(name, "datetime")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(pyObjectDerived.__getattr__("year").asInt(), pyObjectDerived.__getattr__("month").asInt(),
                            pyObjectDerived.__getattr__("day").asInt(), pyObjectDerived.__getattr__("hour").asInt(),
                            pyObjectDerived.__getattr__("minute").asInt(), pyObjectDerived.__getattr__("second").asInt());
                    cal.set(Calendar.MILLISECOND, pyObjectDerived.__getattr__("microsecond").asInt() / 1000);
                    return cal.getTime();
                } else if (StringUtils.equals(name, "ObjectId")) {
                    String uuid = pyObjectDerived.__str__().asString();
                    ObjectId objectId = new ObjectId(uuid);
                    return objectId;
                }
            } catch (Exception e) {
                log.error("PyObjectDerived convert to java error, type:{}", name, e);
            }
            return pyObject.__str__();
        } else if (pyObject instanceof PySequenceList) {
            // PyList + PyTuple
            PySequenceList pySequenceList = (PySequenceList) pyObject;
            List<Object> list = new ArrayList();
            for (PyObject pyo : pySequenceList.getArray()) {
                list.add(toJavaObjectFromPyObject(pyo));
            }
            return list;
        } else if (pyObject instanceof PySet) {
            //PySet
            PySet pySet = (PySet) pyObject;
            Set<Object> set = new HashSet();
            for (PyObject pyo : pySet.getSet()) {
                set.add(toJavaObjectFromPyObject(pyo));
            }
            return set;
        } else if (pyObject instanceof PyDictionary) {
            //PyDictionary
            PyDictionary pyDictionary = (PyDictionary) pyObject;
            Map<Object, Object> map = new HashMap();
            for (PyObject pyKey : pyDictionary.pyKeySet()) {
                map.put(pyKey.__tojava__(Object.class), toJavaObjectFromPyObject(pyDictionary.get(pyKey)));
            }
            return map;
        } else if (pyObject instanceof PyNone) {
            return null;
        } else {
            log.warn("pyObject类型{}不支持转换", pyObject.getClass().getSimpleName());
            return null;
        }
    }


}
