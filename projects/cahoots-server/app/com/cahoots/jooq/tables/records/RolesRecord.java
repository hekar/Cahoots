/**
 * This class is generated by jOOQ
 */
package com.cahoots.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.0"},
                            comments = "This class is generated by jOOQ")
public class RolesRecord extends org.jooq.impl.UpdatableRecordImpl<com.cahoots.jooq.tables.records.RolesRecord> {

	private static final long serialVersionUID = -1334419441;

	/**
	 * The table column <code>public.roles.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public void setId(java.lang.Integer value) {
		setValue(com.cahoots.jooq.tables.Roles.ROLES.ID, value);
	}

	/**
	 * The table column <code>public.roles.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.lang.Integer getId() {
		return getValue(com.cahoots.jooq.tables.Roles.ROLES.ID);
	}

	/**
	 * The table column <code>public.roles.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public java.util.List<com.cahoots.jooq.tables.records.UsersRecord> fetchUsersList() {
		return create()
			.selectFrom(com.cahoots.jooq.tables.Users.USERS)
			.where(com.cahoots.jooq.tables.Users.USERS.ROLE.equal(getValue(com.cahoots.jooq.tables.Roles.ROLES.ID)))
			.fetch();
	}

	/**
	 * The table column <code>public.roles.name</code>
	 */
	public void setName(java.lang.String value) {
		setValue(com.cahoots.jooq.tables.Roles.ROLES.NAME, value);
	}

	/**
	 * The table column <code>public.roles.name</code>
	 */
	public java.lang.String getName() {
		return getValue(com.cahoots.jooq.tables.Roles.ROLES.NAME);
	}

	/**
	 * Create a detached RolesRecord
	 */
	public RolesRecord() {
		super(com.cahoots.jooq.tables.Roles.ROLES);
	}
}
