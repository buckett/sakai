package org.sakaiproject.authz.impl;

/**
 * Created by buckett on 27/03/15.
 */
class RealmRole implements Comparable<RealmRole> {
    private String name;
    private String key;

    RealmRole(String name) {
        this.name = name;
    }

    RealmRole(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int compareTo(RealmRole realmRole) {
        return this.name.compareToIgnoreCase(realmRole.name);
    }
}
