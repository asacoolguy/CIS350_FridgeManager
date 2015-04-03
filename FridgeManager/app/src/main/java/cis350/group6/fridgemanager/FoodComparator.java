package cis350.group6.fridgemanager;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Ethan on 4/3/2015.
 * used to sort FridgeActivity's ArrayList of strings containing food items
 * sorts from smaller "days left" to larger
 */

public class FoodComparator implements Comparator<String> {

    public int compare(String s1, String s2){
        String[] tokens1 = s1.split(":");
        String[] tokens2 = s2.split(":");

        int day1 = Integer.parseInt(tokens1[1].substring(1));
        int day2 = Integer.parseInt(tokens2[1].substring(1));

        if (day1 == day2){
            return 0;
        }
        else if (day1 > day2){
            return 1;
        }
        else{
            return -1;
        }
    }

}
