package com.filip.focushelper2;

import android.graphics.drawable.Drawable;

import java.util.Objects;

public class AppList implements Comparable {
    private String name;
    Drawable icon;
    private String packageName;
    //
    private boolean checked = false;
    ///

    public AppList(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public AppList(String name, Drawable icon, String packageName) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
    }

    public AppList(String name, Drawable icon, String packageName, boolean checked) {
        this.name = name;
        this.icon = icon;
        this.packageName = packageName;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toogleChecked() {
        this.checked=!this.checked;
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
