/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buffersample;

import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 *
 * @author User
 */
public class Delegating {

    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public Delegating(Map<String, Point> points) {
        this.locations = new ConcurrentHashMap<>(points);
        this.unmodifiableMap = Collections.unmodifiableMap(points);
    }

    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocations(String id, int x, int y) {

    }

    public static void main(String[] args) {
        String s;
        try (Scanner sc = new Scanner(System.in)) {
            do {
                s = sc.nextLine();
                if (!checkSeries(s)) {
                    System.out.println("Введите корректно серию");
                    System.out.println(s);
                    System.out.println("--");
                }
            } while (!checkSeries(s));
        }
    }

    private static final String SERIES = "[1-9]{2}\\s?[1-9]{2}";

    public static boolean checkSeries(String s) {

        Pattern patternSeries;
        patternSeries = Pattern.compile(SERIES);
        return patternSeries.matcher(s).matches();
    }
}
