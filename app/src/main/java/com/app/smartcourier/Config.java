package com.app.smartcourier;

public class Config {
    public static final String BASE_URL = "http://finalproject24.com/courier/android/";
    public static boolean isInBranch = false;
    //public static final String BASE_URL = "http://192.168.43.59/Smart Courier/android/";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String CONTACT = "contact";
    public static String LATITUDE = "";
    public static String LONGITUDE = "";

    public static final String SHARED_PREF_NAME = "com.app.smartcourier"; //pcakage name+ id

    //This would be used to store the cell of current logged in user
    public static final String CELL_SHARED_PREF = "contact";
    public static final String Branch_SHARED_PREF = "branch";
    public static final String Manager_Cell_SHARED_PREF = "manager_contact";
}
