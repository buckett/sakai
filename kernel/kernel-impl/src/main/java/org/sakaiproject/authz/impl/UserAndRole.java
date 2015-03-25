package org.sakaiproject.authz.impl;

import org.sakaiproject.util.StringUtil;

/**
 * Holder for details of a user and their role in a realm.
 * It's immutable.
 */
public class UserAndRole {
	public final String userId;

	public final String role;

	public final boolean active;

	public final boolean provided;

	public UserAndRole(String userId, String role, boolean active, boolean provided) {
		this.userId = userId;
		this.role = role;
		this.active = active;
		this.provided = provided;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof UserAndRole)) return false;
		if (this == obj) return true;
		UserAndRole other = (UserAndRole) obj;
		if (StringUtil.different(this.role, other.role)) return false;
		if (this.provided != other.provided) return false;
		if (this.active != other.active) return false;
		if (StringUtil.different(this.userId, other.userId)) return false;
		return true;
	}

	public int hashCode() {
		return (this.role + Boolean.valueOf(this.provided).toString() + Boolean.valueOf(this.active).toString() + this.userId).hashCode();
	}
}
