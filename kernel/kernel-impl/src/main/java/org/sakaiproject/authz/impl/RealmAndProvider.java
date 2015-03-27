package org.sakaiproject.authz.impl;

/**
 * Created by buckett on 27/03/15.
 */
public class RealmAndProvider {
	public Integer realmId;

	public String providerId;

	public RealmAndProvider(Integer id, String provider) {
		this.realmId = id;
		this.providerId = provider;
	}
}
