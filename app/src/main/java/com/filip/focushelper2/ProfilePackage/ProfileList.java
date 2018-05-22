package com.filip.focushelper2.ProfilePackage;

import java.util.Objects;

public class ProfileList {
    private String profileName;
    private String blockedApps;
    private boolean type;

    public ProfileList() {
    }

    public ProfileList(String profileName, String blockedApps) {
        this.profileName = profileName;
        this.blockedApps = blockedApps;
    }

    public ProfileList(String profileName, String blockedApps, boolean type) {
        this.profileName = profileName;
        this.blockedApps = blockedApps;
        this.type = type;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getBlockedApps() {
        return blockedApps;
    }

    public void setBlockedApps(String blockedApps) {
        this.blockedApps = blockedApps;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileList that = (ProfileList) o;
        return type == that.type &&
                Objects.equals(profileName, that.profileName) &&
                Objects.equals(blockedApps, that.blockedApps);
    }

    @Override
    public int hashCode() {

        return Objects.hash(profileName, blockedApps, type);
    }

    @Override
    public String toString() {
        return "ProfileList{" +
                "profileName='" + profileName + '\'' +
                ", blockedApps='" + blockedApps + '\'' +
                ", type=" + type +
                '}';
    }
}
