package org.sakaiproject.authz.impl;

import org.sakaiproject.util.StringUtil;

/**
 * Created by buckett on 27/03/15.
 */
public class RoleAndDescription {
	public String role;

	public String description;

	public boolean providerOnly;

	public RoleAndDescription(String role, String description, boolean providerOnly) {
		this.role = role;
		this.description = description;
		this.providerOnly = providerOnly;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof RoleAndDescription)) return false;
		if (this == obj) return true;
		RoleAndDescription other = (RoleAndDescription) obj;
		if (StringUtil.different(this.role, other.role)) return false;
		if (StringUtil.different(this.description, other.description)) return false;
		if (this.providerOnly != other.providerOnly) return false;
		return true;
	}

	public int hashCode() {
		return (this.role + this.description + Boolean.valueOf(this.providerOnly).toString()).hashCode();
	}
}
