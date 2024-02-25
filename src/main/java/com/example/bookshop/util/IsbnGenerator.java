package com.example.bookshop.util;

import java.util.Random;

public class IsbnGenerator {

    public static String generic() {
        return "ISBN-" + (new Random().nextInt(1000) + 1000);
    }
}
