package com.google.mobilesafe.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectHelper {
    public static Object reflectConstructor(Class<?> cls, Object[] objArr) throws Exception {
        if (objArr == null || objArr.length <= 0) {
            return cls.getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        return cls.getConstructor(getParamsTypes(objArr)).newInstance(objArr);
    }

    public static Object reflectConstructor(Class<?> cls, Class<?>[] clsArr, Object[] objArr) throws Exception{
        if (objArr == null || objArr.length <= 0) {
            return cls.getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        return cls.getConstructor(clsArr).newInstance(objArr);
    }

    public static Object invoke(Object obj, String str, Object[] objArr) throws Exception{
        return invoke(obj.getClass(), obj, str, objArr);
    }

    private static Object invoke(Class<?> cls, Object obj, String str, Object[] objArr) throws Exception{
        Method method = null;
        if (objArr == null || objArr.length == 0) {
            method = cls.getMethod(str, new Class[0]);
            method.setAccessible(true);
            return method.invoke(obj, new Object[0]);
        }
        method = cls.getMethod(str, getParamsTypes(objArr));
        method.setAccessible(true);
        return method.invoke(obj, objArr);
    }

    public static Object invokeStatic(String str, String str2, Object[] objArr) throws Exception{
        Class cls = Class.forName(str);
        return invoke(cls, (Object) cls, str2, objArr);
    }

    public static Object invoke(Object obj, String str, Class<?>[] clsArr, Object[] objArr) throws Exception {
        return invoke(obj.getClass(), obj, str, clsArr, objArr);
    }

    private static Object invoke(Class<?> cls, Object obj, String str, Class<?>[] clsArr, Object[] objArr) throws Exception{
        Method method = null;
        if (objArr == null || objArr.length == 0) {
            method = cls.getMethod(str, new Class[0]);
            method.setAccessible(true);
            return method.invoke(obj, new Object[0]);
        }
        method = cls.getMethod(str, clsArr);
        method.setAccessible(true);
        return method.invoke(obj, objArr);
    }

    public static Object invokeStatic(String str, String str2, Class<?>[] clsArr, Object[] objArr) throws Exception{
        Class cls = Class.forName(str);
        return invoke(cls, cls, str2, clsArr, objArr);
    }

    private static Class<?>[] getParamsTypes(Object[] objArr) {
        Class<?>[] clsArr = new Class[objArr.length];
        for (int i = 0; i < clsArr.length; i++) {
            clsArr[i] = objArr[i].getClass();
        }
        return clsArr;
    }

    public static boolean setField(Object obj, Class<?> cls, String str, Object obj2) {
        if (obj == null || cls == null || str == null) {
            throw new IllegalArgumentException("parameter can not be null!");
        }
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean setField(Object obj, String str, Object obj2) {
        if (obj != null && str != null) {
            return setFieldStepwise(obj, obj.getClass(), str, obj2);
        }
        throw new IllegalArgumentException("parameter can not be null!");
    }

    private static boolean setFieldStepwise(Object obj, Class<?> cls, String str, Object obj2) {
        while (cls != null) {
            if (setField(obj, cls, str, obj2)) {
                return true;
            }
            try {
                cls = cls.getSuperclass();
            } catch (Exception e) {
                cls = null;
            }
        }
        return false;
    }

    public static boolean setStaticField(String str, String str2, Object obj) {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("parameter can not be null!");
        }
        try {
            Class cls = Class.forName(str);
            return setFieldStepwise(cls, cls, str2, obj);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("className not found");
        }
    }

    public static Object getField(Object obj, Class<?> cls, String str) throws NoSuchFieldException {
        if (obj == null || cls == null || str == null) {
            throw new IllegalArgumentException("parameter can not be null!");
        }
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField.get(obj);
        } catch (Exception e) {
            throw new NoSuchFieldException(str);
        }
    }

    public static Object getField(Object obj, String str) {
        if (obj != null && str != null) {
            return getFieldStepwise(obj, obj.getClass(), str);
        }
        throw new IllegalArgumentException("parameter can not be null!");
    }

    private static Object getFieldStepwise(Object obj, Class<?> cls, String str) {
        while (cls != null) {
            try {
                return getField(obj, cls, str);
            } catch (NoSuchFieldException e) {
                try {
                    cls = cls.getSuperclass();
                } catch (Exception e2) {
                    cls = null;
                }
                return cls;
            }
        }
        return null;
    }

    public static Object getStaticField(String str, String str2) throws ClassNotFoundException {
        if (str == null || str2 == null) {
            throw new IllegalArgumentException("parameter can not be null!");
        }
        try {
            Class cls = Class.forName(str);
            return getFieldStepwise(cls, cls, str2);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("className not found");
        }
    }
}
