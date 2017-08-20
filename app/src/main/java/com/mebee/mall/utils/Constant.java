package com.mebee.mall.utils;

/**
 * Created by mebee on 2017/8/9.
 */

public class Constant {



    public static class API{
        public static String BASE_API = "http://119.23.33.62:8080/Fruit_Store/";
        public static String ALL_WARES = BASE_API+"fruit/getAll.action";
        public static String LOGIN_API = BASE_API + "user/login.action";
        public static String REGISTE_API = BASE_API +"user/register.action";
        public static String RESETPASSWORD_API = BASE_API + "user/updatepassword.action";

    }
}
