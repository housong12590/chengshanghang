package com.jmm.csg.bean;

import java.util.List;

public class ChineseCities {

    private String name;
    private List<City> sub;

    public String getName() {
        return name;
    }

    public List<City> getCity() {
        return sub;
    }

    public static class City {
        private String name;
        private List<String> sub;

        public String getName() {
            return name;
        }

        public List<String> getArea() {
            return sub;
        }
    }
}
