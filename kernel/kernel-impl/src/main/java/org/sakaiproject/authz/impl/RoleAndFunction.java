package org.sakaiproject.authz.impl;

import org.sakaiproject.util.StringUtil;

/**
 * Created by buckett on 27/03/15.
 */
public class RoleAndFunction {
	public String role;

	public String function;

	public RoleAndFunction(String role, String function) {
		this.role = role;
		this.function = function;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof RoleAndFunction)) return false;
		if (this == obj) return true;
		RoleAndFunction other = (RoleAndFunction) obj;
		if (StringUtil.different(this.role, other.role)) return false;
		if (StringUtil.different(this.function, other.function)) return false;
		return true;
	}

	public int hashCode() {
		return (this.role + this.function).hashCode();
	}
}
