package com.dreamtech.tldental.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
    public static ArrayList<String> convertStringToImages(String imgs) {
        return new ArrayList<>(Arrays.asList(imgs
                .substring(1, imgs.length() - 1).split(", ")));
    }
}
