package com.ece.cov19.DataModels;

public class LoggedInUserData {
    public static String loggedInUserName;
    public static String loggedInUserPhone;
    public static String loggedInUserGender;
    public static String loggedInUserBloodGroup;
    public static String loggedInUserDivision;
    public static String loggedInUserDistrict;
    public static String loggedInUserThana;
    public static String loggedInUserAge;
    public static String loggedInUserDonorInfo;

    public LoggedInUserData(String name, String phone, String gender, String bloodGroup,
                         String division, String district, String thana, String age, String donor) {
        loggedInUserName = name;
        loggedInUserPhone = phone;
        loggedInUserGender = gender;
        loggedInUserBloodGroup = bloodGroup;
        loggedInUserDivision = division;
        loggedInUserDistrict = district;
        loggedInUserThana = thana;
        loggedInUserAge = age;
        loggedInUserDonorInfo = donor;
    }
}
