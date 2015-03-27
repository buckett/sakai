package org.sakaiproject.authz.impl;

import org.sakaiproject.util.StringUtil;

/**
 * Created by buckett on 27/03/15.
 */
public class RealmAndRole {
    public Integer realmId;

    public String role;

    public boolean active;

    public boolean provided;

    public RealmAndRole(Integer id, String role, boolean active, boolean provided) {
        this.realmId = id;
        this.role = role;
        this.active = active;
        this.provided = provided;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof RealmAndRole)) return false;
        if (this == obj) return true;
        RealmAndRole other = (RealmAndRole) obj;
        if (StringUtil.different(this.role, other.role)) return false;
        if (this.provided != other.provided) return false;
        if (this.active != other.active) return false;
        if (((this.realmId == null) && (other.realmId != null)) || ((this.realmId != null) && (other.realmId == null))
                || ((this.realmId != null) && (other.realmId != null) && (!this.realmId.equals(other.realmId))))
            return false;
        return true;
    }

    public int hashCode() {
        return (this.role + Boolean.valueOf(this.provided).toString() + Boolean.valueOf(this.active).toString() + this.realmId).hashCode();
    }
}
