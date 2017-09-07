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
        public static final String CATEGORY_WARES = BASE_API + "fruit/getByCategory.action";
        public static final String INSERTORDER_API = BASE_API + "order/insertorder.action";
        public static final String UPLOAD_PICTURE_API = BASE_API + "user/picture.action";
        public static final String GET_ALL_ADDRESS_API = BASE_API + "order/getAllAddress.action";
        public static final String GET_ALL_ORDERS_API = BASE_API + "order/findByName.action";
        public static final String GET_ADDRESS_BY_ID_API = BASE_API + "order/findById.action";
        public static final String GET_ALL_WARES_BY_ORDER_ID_API = BASE_API + "order/getAllGoods.action" ;
        public static final String GET_WARE_BY_ID_API = BASE_API + "fruit/getById.action";
        public static final String CHANGE_ORDER_STATE_API = BASE_API + "order/updatestate.action";
        public static final String DELETE_ORDER_BY_ID_API = BASE_API + "order/deleteorder.action";
        public static final String FIND_WARE_BY_NAME_API = BASE_API + "fruit/getByName.action";
    }
}
