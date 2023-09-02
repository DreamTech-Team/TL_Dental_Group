package com.dreamtech.tldental.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Utils {
    public static ArrayList<String> convertStringToImages(String imgs) {
        return new ArrayList<>(Arrays.asList(imgs
                .substring(1, imgs.length() - 1).split(", ")));
    }

    public static String generateSlug(String str) {
        String cleanedStr = str.replaceAll("[^a-zA-Z0-9]", " ");

        String slug = Normalizer.normalize(cleanedStr, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase(Locale.getDefault());

        return slug.replaceAll("\\s+", "-");
    }
}
