package com.jmm.csg.imgsel.bean;

import java.io.Serializable;

public class ImageFolderBean implements Serializable{

    public String path;
    public int pisNum = 0;
    public String fileName;
    public int selectPosition;
    public int _id;
    public int position;
    private String folderPath;

    public String getFolderPath(){
        return path.substring(0,path.lastIndexOf("/"));
    }
}
