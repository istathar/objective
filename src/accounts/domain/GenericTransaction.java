/*
 * GenericTransaction.java
 * 
 * See LICENCE file for usage and redistribution terms
 * Copyright (c) 2005 Operational Dynamics
 */
package accounts.domain;

import java.util.Set;

/**
 * A generic transaction in the "general ledger" as traditional accounting
 * systems would call it. Simply consists of a balanced set of Entries, and does
 * not link to any special user interface.
 * <P>
 * This would have been called GeneralLedgerTransaction but ther was potential
 * for naming confustion as we have modelled the actual ledger(s) in any given
 * account with the Ledger class. This is most assuredly a Transaction.
 * 
 * @author Andrew Cowie
 */
public class GenericTransaction extends Transaction
{
	/**
	 * Default constructor, for searching.
	 */
	public GenericTransaction() {
		super();
	}

	public GenericTransaction(String description, Set entries) {
		super(description, entries);
	}

	public GenericTransaction(String description, Entry[] entries) {
		super(description, entries);
	}
}