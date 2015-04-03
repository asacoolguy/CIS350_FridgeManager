package cis350.group6.fridgemanager;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Annie on 4/3/2015.
 */
@ParseClassName("Food")
public class Food extends ParseObject{

    public Food(){}

    public String getName(){
        return getString("Name");
    }

    public void setName(String newName){
        put("Name", newName);
    }

    public Date getCreateDate(){
        return getDate("createdAt");
    }

    public Number getQuantity(){
        return getNumber("Quantity");
    }

    public void setQuantity(Number newQuantity){
        put("Quantity", newQuantity);
    }

    public String getUnits(){
        return getString("Units");
    }

    public void setUnits(String newUnits){
        put("Units", newUnits);
    }

    public String getLocation(){
        return getString("Location");
    }

    public void setLocation(String newLoc){
        put("Location", newLoc);
    }

    public Date getExpireDate(){
        return getDate("expiresAt");
    }

    public void setExpireDate(Date expireDate){
        put("expiresAt", expireDate);
    }

    public boolean getFavorite() {return getBoolean("favorite");}

    public void setFavorite(boolean favorite){ put ("favorite", favorite);}
}
