package com.filip.focushelper2.ProfilePackage;

import java.util.Objects;

public class ProfileList {
    private String profileName;
    private String blockedApps;

    public ProfileList() {
    }

    public ProfileList(String profileName, String blockedApps) {
        this.profileName = profileName;
        this.blockedApps = blockedApps;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileList that = (ProfileList) o;
        return Objects.equals(profileName, that.profileName) &&
                Objects.equals(blockedApps, that.blockedApps);
    }

    @Override
    public int hashCode() {

        return Objects.hash(profileName, blockedApps);
    }

    @Override
    public String toString() {
        return "ProfileList{" +
                "profileName='" + profileName + '\'' +
                ", blockedApps='" + blockedApps + '\'' +
                '}';
    }
}
