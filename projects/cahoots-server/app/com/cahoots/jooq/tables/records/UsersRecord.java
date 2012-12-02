/**
 * This class is generated by jOOQ
 */
package com.cahoots.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.0"},
                            comments = "This class is generated by jOOQ")
public class UsersRecord extends org.jooq.impl.UpdatableRecordImpl<com.cahoots.jooq.tables.records.UsersRecord> {

	private static final long serialVersionUID = 1049888682;

	/**
	 * The table column <code>public.users.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public void setId(java.lang.Integer value) {
		setValue(com.cahoots.jooq.tables.Users.USERS.ID, value);
	}

	/**
	 * The table column <code>public.users.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.lang.Integer getId() {
		return getValue(com.cahoots.jooq.tables.Users.USERS.ID);
	}

	/**
	 * The table column <code>public.users.username</code>
	 */
	public void setUsername(java.lang.String value) {
		setValue(com.cahoots.jooq.tables.Users.USERS.USERNAME, value);
	}

	/**
	 * The table column <code>public.users.username</code>
	 */
	public java.lang.String getUsername() {
		return getValue(com.cahoots.jooq.tables.Users.USERS.USERNAME);
	}

	/**
	 * The table column <code>public.users.password</code>
	 */
	public void setPassword(java.lang.String value) {
		setValue(com.cahoots.jooq.tables.Users.USERS.PASSWORD, value);
	}

	/**
	 * The table column <code>public.users.password</code>
	 */
	public java.lang.String getPassword() {
		return getValue(com.cahoots.jooq.tables.Users.USERS.PASSWORD);
	}

	/**
	 * The table column <code>public.users.name</code>
	 */
	public void setName(java.lang.String value) {
		setValue(com.cahoots.jooq.tables.Users.USERS.NAME, value);
	}

	/**
	 * The table column <code>public.users.name</code>
	 */
	public java.lang.String getName() {
		return getValue(com.cahoots.jooq.tables.Users.USERS.NAME);
	}

	/**
	 * The table column <code>public.users.role</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT users__users_role_fkey
	 * FOREIGN KEY (role)
	 * REFERENCES public.roles (id)
	 * </pre></code>
	 */
	public void setRole(java.lang.Integer value) {
		setValue(com.cahoots.jooq.tables.Users.USERS.ROLE, value);
	}

	/**
	 * The table column <code>public.users.role</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT users__users_role_fkey
	 * FOREIGN KEY (role)
	 * REFERENCES public.roles (id)
	 * </pre></code>
	 */
	public java.lang.Integer getRole() {
		return getValue(com.cahoots.jooq.tables.Users.USERS.ROLE);
	}

	/**
	 * Link this record to a given {@link com.cahoots.jooq.tables.records.RolesRecord 
	 * RolesRecord}
	 */
	public void setRole(com.cahoots.jooq.tables.records.RolesRecord value) {
		if (value == null) {
			setValue(com.cahoots.jooq.tables.Users.USERS.ROLE, null);
		}
		else {
			setValue(com.cahoots.jooq.tables.Users.USERS.ROLE, value.getValue(com.cahoots.jooq.tables.Roles.ROLES.ID));
		}
	}

	/**
	 * The table column <code>public.users.role</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT users__users_role_fkey
	 * FOREIGN KEY (role)
	 * REFERENCES public.roles (id)
	 * </pre></code>
	 */
	public com.cahoots.jooq.tables.records.RolesRecord fetchRoles() {
		return create()
			.selectFrom(com.cahoots.jooq.tables.Roles.ROLES)
			.where(com.cahoots.jooq.tables.Roles.ROLES.ID.equal(getValue(com.cahoots.jooq.tables.Users.USERS.ROLE)))
			.fetchOne();
	}

	/**
	 * Create a detached UsersRecord
	 */
	public UsersRecord() {
		super(com.cahoots.jooq.tables.Users.USERS);
	}
}