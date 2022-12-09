/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cinemaapp.utils;

import java.util.Random;

public class TripleDESKeyGenerator {
    private final String randomChar ="12345567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final int keyLength = 24;
    private String key;

    public TripleDESKeyGenerator() {
        Random randomGenerator = new Random();
        StringBuilder generatedKey = new StringBuilder();
        for(int i=0; i<keyLength; i++){
            int randomInt = Math.abs(randomGenerator.nextInt()%randomChar.length());
            char ch = randomChar.charAt(randomInt);
            generatedKey.append(ch);
        }
        key = generatedKey.toString();
    }

    public String getKey() {
        return key;
    }
    
}
