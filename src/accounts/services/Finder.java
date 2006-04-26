/*
 * Finder.java
 * 
 * See LICENCE file for usage and redistribution terms
 * Copyright (c) 2006 Operational Dynamics
 */
package accounts.services;

import java.util.List;

import accounts.persistence.DataClient;
import accounts.persistence.Engine;

/**
 * As Commands are the place to put the logic for constructing, valdidating and
 * storing things to the database, Finders are the place to put predefined logic
 * cases to query things from the database.
 * <p>
 * Although the API is a touch more cumbersome that it coule be (eg, static
 * methods might tighten it up), once a finder is instantiated it can be used
 * repeatedly with minor tweaks to search parameters - both in the sense of SQL
 * bind variables and in the sense of being able to cache intermediate results
 * as appropriate.
 * <p>
 * Like Commands, Finders are not "committed" to the database. The use of
 * transient is encouraged.
 * 
 * @author Andrew Cowie
 */
public abstract class Finder
{
	/**
	 * Subclasses should call super() to initialize basic common elements.
	 */
	protected Finder() {
		if (Engine.server == null) {
			throw new IllegalStateException("Trying to init a Finder but the persistence Engine is not initialized.");
		}
	}

	/**
	 * Somewhat as a definition, Finders return a List. (After all, they just
	 * use the underlying db4o query mechanism which we expose via DataClient's
	 * .nativeQuery() or .queryByExample() which return List). While concrete
	 * subclasses may (and indeed are encouraged to) use specificly crafted
	 * methods approprate to their use cases, this is the one common API element
	 * that we have across Finders, and where the reference to the database is
	 * passed in. An implentation's query() can internally cache the result for
	 * [re]use by the domain specific result methods; otherwise they can be the
	 * thing that caches.
	 * 
	 * @param an
	 *            open DataClient from which to retrieve Objects.
	 * @throws NotFoundException
	 *             in the event that zero objects are retrieved. This isn't a
	 *             problem, per se; but this allows you to proceed smoothly with
	 *             your logic when using the finder rather than having to
	 *             remember to immediately guard against zero result.
	 */
	public abstract List query(DataClient store) throws NotFoundException;

	/**
	 * Clear any cached query results in this Finder. Any setters should call
	 * this in order to invalidate a previous query() or other call's cached
	 * results. As with query(), expose this as public if appropriate to your
	 * use case.
	 */
	protected abstract void reset();
}
