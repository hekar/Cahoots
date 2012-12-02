/**
 * This class is generated by jOOQ
 */
package com.cahoots.jooq;

/**
 * This class is generated by jOOQ.
 *
 * A class modelling foreign key relationships between tables of the <code>public</code> 
 * schema
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.0"},
                            comments = "This class is generated by jOOQ")
public class Keys {

	// IDENTITY definitions
	public static final org.jooq.Identity<com.cahoots.jooq.tables.records.RolesRecord, java.lang.Integer> IDENTITY_ROLES = Identities0.IDENTITY_ROLES;
	public static final org.jooq.Identity<com.cahoots.jooq.tables.records.UsersRecord, java.lang.Integer> IDENTITY_USERS = Identities0.IDENTITY_USERS;

	// UNIQUE and PRIMARY KEY definitions
	public static final org.jooq.UniqueKey<com.cahoots.jooq.tables.records.RolesRecord> ROLES_PKEY = UniqueKeys0.ROLES_PKEY;
	public static final org.jooq.UniqueKey<com.cahoots.jooq.tables.records.RolesRecord> ROLES_ID_NAME_KEY = UniqueKeys0.ROLES_ID_NAME_KEY;
	public static final org.jooq.UniqueKey<com.cahoots.jooq.tables.records.UsersRecord> NEW_USER_PKEY = UniqueKeys0.NEW_USER_PKEY;
	public static final org.jooq.UniqueKey<com.cahoots.jooq.tables.records.UsersRecord> NEW_USER_ID_USERNAME_KEY = UniqueKeys0.NEW_USER_ID_USERNAME_KEY;

	// FOREIGN KEY definitions
	public static final org.jooq.ForeignKey<com.cahoots.jooq.tables.records.UsersRecord, com.cahoots.jooq.tables.records.RolesRecord> USERS__USERS_ROLE_FKEY = ForeignKeys0.USERS__USERS_ROLE_FKEY;

	/**
	 * No instances
	 */
	private Keys() {}

	@SuppressWarnings("hiding")
	private static class Identities0 extends org.jooq.impl.AbstractKeys {
		public static org.jooq.Identity<com.cahoots.jooq.tables.records.RolesRecord, java.lang.Integer> IDENTITY_ROLES = createIdentity(com.cahoots.jooq.tables.Roles.ROLES, com.cahoots.jooq.tables.Roles.ROLES.ID);
		public static org.jooq.Identity<com.cahoots.jooq.tables.records.UsersRecord, java.lang.Integer> IDENTITY_USERS = createIdentity(com.cahoots.jooq.tables.Users.USERS, com.cahoots.jooq.tables.Users.USERS.ID);
	}

	@SuppressWarnings({"hiding", "unchecked"})
	private static class UniqueKeys0 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.UniqueKey<com.cahoots.jooq.tables.records.RolesRecord> ROLES_PKEY = createUniqueKey(com.cahoots.jooq.tables.Roles.ROLES, com.cahoots.jooq.tables.Roles.ROLES.ID);
		public static final org.jooq.UniqueKey<com.cahoots.jooq.tables.records.RolesRecord> ROLES_ID_NAME_KEY = createUniqueKey(com.cahoots.jooq.tables.Roles.ROLES, com.cahoots.jooq.tables.Roles.ROLES.ID, com.cahoots.jooq.tables.Roles.ROLES.NAME);
		public static final org.jooq.UniqueKey<com.cahoots.jooq.tables.records.UsersRecord> NEW_USER_PKEY = createUniqueKey(com.cahoots.jooq.tables.Users.USERS, com.cahoots.jooq.tables.Users.USERS.ID);
		public static final org.jooq.UniqueKey<com.cahoots.jooq.tables.records.UsersRecord> NEW_USER_ID_USERNAME_KEY = createUniqueKey(com.cahoots.jooq.tables.Users.USERS, com.cahoots.jooq.tables.Users.USERS.ID, com.cahoots.jooq.tables.Users.USERS.USERNAME);
	}

	@SuppressWarnings({"hiding", "unchecked"})
	private static class ForeignKeys0 extends org.jooq.impl.AbstractKeys {
		public static final org.jooq.ForeignKey<com.cahoots.jooq.tables.records.UsersRecord, com.cahoots.jooq.tables.records.RolesRecord> USERS__USERS_ROLE_FKEY = createForeignKey(com.cahoots.jooq.Keys.ROLES_PKEY, com.cahoots.jooq.tables.Users.USERS, com.cahoots.jooq.tables.Users.USERS.ROLE);
	}
}
