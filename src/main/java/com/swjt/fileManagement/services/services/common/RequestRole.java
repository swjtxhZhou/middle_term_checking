package com.swjt.fileManagement.services.services.common;

//根据路由判断所需要的权限
public class RequestRole {

    public static String getRequestRole(StringBuffer stringBuffer) {
        String[] requestRole = new String[1];
        System.out.println(stringBuffer);
        if (stringBuffer.toString().contains("/project")) {
            requestRole[0] = "project";
        }
        if (stringBuffer.toString().contains("/teacher")) {
            requestRole[0] = "teacher";
        }
        if (stringBuffer.toString().contains("/admin")) {
            requestRole[0] = "admin";
        }
        return requestRole[0];
    }
}
