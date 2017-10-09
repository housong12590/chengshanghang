package com.jmm.csg.imgsel.bean;

import java.util.List;

public class TestBean {

    private List<Tracks> tracks;

    public List<Tracks> getTracks() {
        return tracks;
    }

    public static class Tracks {
        private String title;

        public String getTitle() {
            return title;
        }
    }
}
