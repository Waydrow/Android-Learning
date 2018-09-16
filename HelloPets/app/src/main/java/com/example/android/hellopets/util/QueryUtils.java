package com.example.android.hellopets.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.hellopets.data.Pet;
import com.example.android.hellopets.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by wkp on 2016/12/4.
 */
public final class QueryUtils {

    // the ipAddress of the server
    public static final String ipAddress = "222.195.145.152:81";

    // use to mark the log this class
    public static final String LOG_TAG = QueryUtils.class.getName();

    // transfer the json format data into ArrayList<Pet>
    public static final ArrayList<Pet> extractFeatureFromJson(String petsJSON) {
        if (TextUtils.isEmpty(petsJSON)) {
            return null;
        }
        ArrayList<Pet> pets = new ArrayList<>();
        try {
            JSONArray petsArray = new JSONArray(petsJSON);

            for (int i = 0; i < petsArray.length(); i++) {
                JSONObject currentPet = petsArray.getJSONObject(i);

                Integer id = currentPet.getInt("id");
//                Integer roomid=currentPet.getInt("roomid");
                String petname = currentPet.getString("petname");
                String breed = currentPet.getString("breed");
                Integer age = currentPet.getInt("age");
                String sex = currentPet.getString("sex");
                // is DATE format in the mysqldb
                String entertime = currentPet.getString("entertime");
//                String leavetime=currentPet.getString("leavetime");
                Integer isback = currentPet.getInt("isback");
                String backreason = currentPet.getString("backreason");
                String character = currentPet.getString("character");
                String healthy = currentPet.getString("healthy");
                String img = currentPet.getString("img");

                Integer ispass = null;
                Pet pet = null;
                // used in the look application
                if (currentPet.has("ispass")) {
                    ispass = currentPet.getInt("ispass");
                    //表连接后返回的id不是所需要的
                    id = currentPet.getInt("petid");
                    // call the constructor
                    pet = new Pet(id, petname, breed, age, sex,
                            Date.valueOf(entertime), isback, backreason,
                            character, healthy, img, ispass);
                } else {
                    // call the constructor
                    pet = new Pet(id, petname, breed, age, sex,
                            Date.valueOf(entertime), isback, backreason,
                            character, healthy, img);
                }
                pets.add(pet);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the pet JSON results", e);
        }

        return pets;
    }

    public static final User extractFeatureFromUserJson(String userJSON) {
        if (TextUtils.isEmpty(userJSON)) {
            return null;
        }

        User user = null;
        try {

            JSONObject userObject = new JSONObject(userJSON);
            String realname = userObject.getString("realname");
            String sex = userObject.getString("sex");
            String idcard = userObject.getString("idcard");
            String phone = userObject.getString("phone");
            String address = userObject.getString("address");

            user = new User(realname, sex, idcard, phone, address);
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the user JSON results", e);
        }

        return user;
    }
}
