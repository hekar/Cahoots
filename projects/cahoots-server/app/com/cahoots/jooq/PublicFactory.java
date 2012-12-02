/**
 * This class is generated by jOOQ
 */
package com.cahoots.jooq;

/**
 * This class is generated by jOOQ.
 *
 * A Factory for specific use with the <code>public</code> schema.
 * <p>
 * This Factory will not render the <code>public</code> schema's schema name 
 * in rendered SQL (assuming that you use it as the default schema on your 
 * connection!). Use the more generic {@link org.jooq.util.postgres.PostgresFactory} 
 * or the {@link org.jooq.impl.Factory} instead, if you want to fully qualify 
 * tables, routines, etc.
 */
@javax.annotation.Generated(value    = {"http://www.jooq.org", "2.4.0"},
                            comments = "This class is generated by jOOQ")
public class PublicFactory extends org.jooq.util.postgres.PostgresFactory {

	private static final long serialVersionUID = -1431447062;

	/**
	 * Create a factory with a connection
	 *
	 * @param connection The connection to use with objects created from this factory
	 */
	public PublicFactory(java.sql.Connection connection) {
		super(connection);

		initDefaultSchema();
	}

	/**
	 * Create a factory with a connection and a schema mapping
	 * 
	 * @deprecated - 2.0.5 - Use {@link #PublicFactory(java.sql.Connection, org.jooq.conf.Settings)} instead
	 */
	@Deprecated
	public PublicFactory(java.sql.Connection connection, org.jooq.SchemaMapping mapping) {
		super(connection, mapping);

		initDefaultSchema();
	}

	/**
	 * Create a factory with a connection and some settings
	 *
	 * @param connection The connection to use with objects created from this factory
	 * @param settings The settings to apply to objects created from this factory
	 */
	public PublicFactory(java.sql.Connection connection, org.jooq.conf.Settings settings) {
		super(connection, settings);

		initDefaultSchema();
	}

	/**
	 * Initialise the render mapping's default schema.
	 * <p>
	 * For convenience, this schema-specific factory should override any pre-existing setting
	 */
	private final void initDefaultSchema() {
		org.jooq.conf.SettingsTools.getRenderMapping(getSettings()).setDefaultSchema(com.cahoots.jooq.Public.PUBLIC.getName());
	}
}
