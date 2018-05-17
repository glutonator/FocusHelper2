package com.filip.focushelper2;

import android.graphics.drawable.Drawable;

import java.util.Objects;

public class AppList implements Comparable {
    private String name;
    Drawable icon;

    public AppList(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppList appList = (AppList) o;
        return Objects.equals(name, appList.name) &&
                Objects.equals(icon, appList.icon);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, icon);
    }

    @Override
    public int compareTo(Object o) {
        AppList a = (AppList) o;
        return getName().compareTo(a.getName());
    }

    @Override
    public String toString() {
        return "AppList{" +
                "name='" + name + '\'' +
                '}';
    }
}
