package me.bright.util;

import java.util.Random;

public class Chance {

    private static Random rnd = new Random();


    public static int getRandomNumber(int min, int max) {
        return rnd.nextInt(max-min+1) + min;
    }

    public static boolean isLuck(double chance) {
        return getRandomNumber(0, 100) <= chance;
    }
}
