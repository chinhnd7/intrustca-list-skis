package com.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Writer writer = new Writer();
        List<String> skis = writer.go();
        System.out.println(skis.toString());
        System.out.println("Số lượng SKI: " + skis.size());
    }
}
