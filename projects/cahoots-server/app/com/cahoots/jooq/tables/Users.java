/**
 * This class is generated by jOOQ
 */
package com.cahoots.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.0"},
                            comments = "This class is generated by jOOQ")
public class Users extends org.jooq.impl.UpdatableTableImpl<com.cahoots.jooq.tables.records.UsersRecord> {

	private static final long serialVersionUID = -509884135;

	/**
	 * The singleton instance of public.users
	 */
	public static final com.cahoots.jooq.tables.Users USERS = new com.cahoots.jooq.tables.Users();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.cahoots.jooq.tables.records.UsersRecord> getRecordType() {
		return com.cahoots.jooq.tables.records.UsersRecord.class;
	}

	/**
	 * The table column <code>public.users.id</code>
	 * <p>
	 * This column is part of the table's PRIMARY KEY
	 */
	public final org.jooq.TableField<com.cahoots.jooq.tables.records.UsersRecord, java.lang.Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this);

	/**
	 * The table column <code>public.users.username</code>
	 */
	public final org.jooq.TableField<com.cahoots.jooq.tables.records.UsersRecord, java.lang.String> USERNAME = createField("username", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>public.users.password</code>
	 */
	public final org.jooq.TableField<com.cahoots.jooq.tables.records.UsersRecord, java.lang.String> PASSWORD = createField("password", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>public.users.name</code>
	 */
	public final org.jooq.TableField<com.cahoots.jooq.tables.records.UsersRecord, java.lang.String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR, this);

	/**
	 * The table column <code>public.users.role</code>
	 * <p>
	 * This column is part of a FOREIGN KEY: <code><pre>
	 * CONSTRAINT users__users_role_fkey
	 * FOREIGN KEY (role)
	 * REFERENCES public.roles (id)
	 * </pre></code>
	 */
	public final org.jooq.TableField<com.cahoots.jooq.tables.records.UsersRecord, java.lang.Integer> ROLE = createField("role", org.jooq.impl.SQLDataType.INTEGER, this);

	public Users() {
		super("users", com.cahoots.jooq.Public.PUBLIC);
	}

	public Users(java.lang.String alias) {
		super(alias, com.cahoots.jooq.Public.PUBLIC, com.cahoots.jooq.tables.Users.USERS);
	}

	@Override
	public org.jooq.Identity<com.cahoots.jooq.tables.records.UsersRecord, java.lang.Integer> getIdentity() {
		return com.cahoots.jooq.Keys.IDENTITY_USERS;
	}

	@Override
	public org.jooq.UniqueKey<com.cahoots.jooq.tables.records.UsersRecord> getMainKey() {
		return com.cahoots.jooq.Keys.NEW_USER_PKEY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.UniqueKey<com.cahoots.jooq.tables.records.UsersRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.cahoots.jooq.tables.records.UsersRecord>>asList(com.cahoots.jooq.Keys.NEW_USER_PKEY, com.cahoots.jooq.Keys.NEW_USER_ID_USERNAME_KEY, com.cahoots.jooq.Keys.USERNAME);
	}

	@Override
	@SuppressWarnings("unchecked")
	public java.util.List<org.jooq.ForeignKey<com.cahoots.jooq.tables.records.UsersRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.cahoots.jooq.tables.records.UsersRecord, ?>>asList(com.cahoots.jooq.Keys.USERS__USERS_ROLE_FKEY);
	}

	@Override
	public com.cahoots.jooq.tables.Users as(java.lang.String alias) {
		return new com.cahoots.jooq.tables.Users(alias);
	}
}
