/*
 * AccountTextOutput.java
 * 
 * See LICENCE file for usage and redistribution terms
 * Copyright (c) 2005 Operational Dynamics
 */
package accounts.ui;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import accounts.domain.Account;
import accounts.domain.Ledger;
import accounts.persistence.DataStore;
import accounts.services.AccountComparator;

/**
 * Output the contents of a Account and its subordinate Ledgers in a human
 * readable text form.
 * 
 * @author Andrew Cowie
 */
public class AccountTextOutput extends TextOutput
{
	protected static final int	descWidth;
	protected static final int	idWidth	= 6;
	protected static final int	typeWidth;

	static {
		int piece = COLUMNS / 7;
		typeWidth = piece * 3;
		// desc gets the rest, less one to keep off the edge.
		descWidth = COLUMNS - idWidth - typeWidth - 1;
	}

	private Set					accounts;

	/**
	 * @param store
	 *            a DataStore from which to fetch all instances of Account.
	 */
	public AccountTextOutput(DataStore store) {
		List aL = store.query(Account.class);

		accounts = new TreeSet(new AccountComparator());
		accounts.addAll(aL);
	}

	/**
	 * @param accounts
	 *            the Set of accounts to be output. It will be sorted by a new
	 *            TreeSet during instantiation.
	 */
	public AccountTextOutput(Set accounts) {
		this.accounts = new TreeSet(new AccountComparator());

		Iterator iter = accounts.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			if (!(o instanceof Account)) {
				throw new IllegalArgumentException("The Set passed must only contain Account objects");
			}
			accounts.add(o);
		}
	}

	/**
	 * @param a
	 *            a single Account to run the outputter over. Use for spot
	 *            debugging only.
	 */
	public AccountTextOutput(Account a) {
		accounts = Collections.singleton(a);
		accounts.add(a);
	}

	/**
	 * @param out
	 *            the PrintWriter you want to send the output to.
	 */
	public void toOutput(PrintWriter out) {
		if (accounts.size() == 0) {
			return;
		}

		Iterator aI = accounts.iterator();
		while (aI.hasNext()) {
			Account a = (Account) aI.next();

			out.print(pad("\"" + chomp(a.getTitle(), descWidth + idWidth - 3) + "\" ", descWidth + idWidth, LEFT));
			// String codeText = a.getCode();
			// codeText = ((codeText == null) ? "" : codeText);
			// out.print(pad(codeText, idWidth, LEFT));
			out.print(pad(chomp(a.getClassString(), typeWidth), typeWidth, RIGHT));

			out.println();

			Set lS = a.getLedgers();
			Iterator lI = lS.iterator();
			while (lI.hasNext()) {
				Ledger l = (Ledger) lI.next();

				out.print(pad("Ledger: \"" + chomp(l.getName(), descWidth + idWidth - 11) + "\" ", descWidth + idWidth,
						LEFT));
				out.print(pad(l.getClassString(), typeWidth, RIGHT));
				out.println();

				EntryTextOutput entryOutputter = new EntryTextOutput(l);
				entryOutputter.toOutput(out);
			}

			// blank line after account is done
			out.println();
		}
	}
}