/**
 * This class is generated by jOOQ
 */
package com.cahoots.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.0"},
                            comments = "This class is generated by jOOQ")
public class Roles extends org.jooq.impl.UpdatableTableImpl<com.cahoots.jooq.tables.records.RolesRecord> {

	private static final long serialVersionUID = 465215166;

	/**
	 * The singleton instance of public.roles
	 */
	public static final com.cahoots.jooq.tables.Roles ROLES = new com.cahoots.jooq.tables.Roles();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.cahoots.jooq.tables.records.RolesRecord> getRecordType() {
		return com.cahoots.jooq.tables.records.RolesRecord.class;
	}

	/**
	 * The table column <code>public.roles.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<com.cahoots.jooq.tables.records.RolesRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The table column <code>public.roles.name</code>
	 */
	public final org.jooq.TableField<com.cahoots.jooq.tables.records.RolesRecord, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR, this);

	public Roles() {
		super("roles", com.cahoots.jooq.Public.PUBLIC);
	}

	public Roles(java.lang.String alias) {
		super(alias, com.cahoots.jooq.Public.PUBLIC, com.cahoots.jooq.tables.Roles.ROLES);
	}

	@Override
	public org.jooq.Identity<com.cahoots.jooq.tables.records.RolesRecord, java.lang.Integer> getIdentity() {
		return com.cahoots.jooq.Keys.IDENTITY_ROLES;
	}

	@Override
	public org.jooq.UniqueKey<com.cahoots.jooq.tables.records.RolesRecord> getMainKey() {
		return com.cahoots.jooq.Keys.ROLES_PKEY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<com.cahoots.jooq.tables.records.RolesRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.cahoots.jooq.tables.records.RolesRecord>>asList(com.cahoots.jooq.Keys.ROLES_PKEY, com.cahoots.jooq.Keys.ROLES_ID_NAME_KEY);
	}

	@Override
	public com.cahoots.jooq.tables.Roles as(java.lang.String alias) {
		return new com.cahoots.jooq.tables.Roles(alias);
	}
}