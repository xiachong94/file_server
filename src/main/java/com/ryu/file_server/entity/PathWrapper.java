package com.ryu.file_server.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class PathWrapper {
    private Boolean isDir;
    private Date date;
    private long size;
    private String name;
    private String link;

    public PathWrapper(Path path) {
        isDir = Files.isDirectory(path);
        try {
            date = Date.from(Files.getLastModifiedTime(path).toInstant());
            size = Files.size(path);
            name = path.getFileName().toString();
            link = "./" + name + (isDir ? "/" : "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean isDir() {
        return isDir;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String formatSize() {
        int divisor = 0;
        String unit = "";
        if (size > 1024 * 1024 * 1024) {
            divisor = 1024 * 1024 * 1024;
            unit = "GB";
        } else if (size > 1024 * 1024) {
            divisor = 1024 * 1024;
            unit = "MB";
        } else if (size > 1024) {
            divisor = 1024;
            unit = "KB";
        } else {
            divisor = 1;
            unit = "B";
        }
        return new BigDecimal(size * 1.0 / divisor).setScale(2, RoundingMode.HALF_UP) + " " + unit;
    }
}
