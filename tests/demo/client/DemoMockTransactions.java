/*
 * DemoMockTransactions.java
 * 
 * See LICENCE file for usage and redistribution terms
 * Copyright (c) 2005-2006 Operational Dynamics
 */
package demo.client;

import generic.persistence.DataClient;
import generic.persistence.Engine;
import generic.util.Debug;

import java.io.FileNotFoundException;
import java.util.List;

import country.au.domain.AustralianPayrollTaxIdentifier;

import accounts.domain.Amount;
import accounts.domain.Credit;
import accounts.domain.Currency;
import accounts.domain.Datestamp;
import accounts.domain.Debit;
import accounts.domain.Employee;
import accounts.domain.Entry;
import accounts.domain.ForeignAmount;
import accounts.domain.GenericTransaction;
import accounts.domain.Ledger;
import accounts.domain.PayrollTransaction;
import accounts.domain.ReimbursableExpensesTransaction;
import accounts.domain.Transaction;
import accounts.services.CommandNotReadyException;
import accounts.services.PostTransactionCommand;
import accounts.services.SpecificLedgerFinder;

/**
 * Contains a prelinary main() method and program initialization (much of which
 * will move to an eventual Client class). The remainder is a huge number of
 * Commands, instantiated programmatically, to run a bunch of transactions.
 * <p>
 * This class is deliberately separate from the DemoBooksSetup program to a)
 * forcedly test the ability to retrieve the objects that it lays down and b)
 * allows us to test scenarios where seemingly identical objects have to be
 * compared - if you run this twice, then there <i>should</i> be two of
 * everything in any ListView or TextOutput display. If there aren't duplicates,
 * then there is a bug in a Comparator somewhere.
 */
public class DemoMockTransactions
{
	public final static String	DEMO_DATABASE	= DemoBooksSetup.DEMO_DATABASE;

	public static void main(String[] args) {
		Debug.setProgname("mock");
		Debug.register("main");
		Debug.register("command");
		Debug.register("memory");

		args = Debug.init(args);
		Debug.print("main", "Starting OprDynMockTransactions");

		Debug.print("main", "Openning database " + DEMO_DATABASE);
		try {
			Engine.openDatafile(DEMO_DATABASE);
		} catch (FileNotFoundException fnfe) {
			System.err.println("\nDemo database not found! Did you run DemoBooksSetup?\n");
			System.exit(1);
		}

		DataClient rw = Engine.gainClient();

		Debug.print("main", "Loading mock account data");
		try {
			/*
			 * Fetch some accounts' ledgers. This is hideously manual and hard
			 * coded, but neceessary for the moment.
			 */

			SpecificLedgerFinder finder = new SpecificLedgerFinder();
			finder.setAccountTitle("ANZ");
			finder.setLedgerName("Current Account");
			finder.query(rw);
			Ledger bankAccount = finder.getLedger();

			finder.setAccountTitle("Petty Cash");
			finder.setLedgerName("Manly");
			finder.query(rw);
			Ledger pettyCash = finder.getLedger();

			finder.setAccountTitle("Furniture");
			finder.setLedgerName("Cost");
			finder.query(rw);
			Ledger furniture = finder.getLedger();

			finder.setAccountTitle("GST");
			finder.setLedgerName("Collected");
			finder.query(rw);
			Ledger gstCollected = finder.getLedger();

			finder.setAccountTitle("Shareholder");
			finder.setLedgerName("Cowie");
			finder.query(rw);
			Ledger shareholdersLoan = finder.getLedger();

			finder.setAccountTitle("GST");
			finder.setLedgerName("Paid");
			finder.query(rw);
			Ledger gstPaid = finder.getLedger();

			finder.setAccountTitle("Owner's Equity");
			finder.setLedgerName("");
			finder.query(rw);
			Ledger ownersEquity = finder.getLedger();

			finder.setAccountTitle("Procedures");
			finder.setLedgerName("Fees");
			finder.query(rw);
			Ledger consultingRevenue = finder.getLedger();

			finder.setAccountTitle("Travel Expenses");
			finder.setLedgerName("Ground Transfer");
			finder.query(rw);
			Ledger groundTransport = finder.getLedger();

			finder.setAccountTitle("Travel Expenses");
			finder.setLedgerName("Accom");
			finder.query(rw);
			Ledger hotels = finder.getLedger();

			finder.setAccountTitle("Travel Expenses");
			finder.setLedgerName("Meals");
			finder.query(rw);
			Ledger travelMeals = finder.getLedger();

			finder.setAccountTitle("Meals and");
			finder.setLedgerName("Staff");
			finder.query(rw);
			Ledger staffMeetings = finder.getLedger();

			finder.setAccountTitle("Telecom");
			finder.setLedgerName("Lines, Inter");
			finder.query(rw);
			Ledger internationalPhone = finder.getLedger();

			finder.setAccountTitle("Employment");
			finder.setLedgerName("Salaries");
			finder.query(rw);
			Ledger salaryExpense = finder.getLedger();

			finder.setAccountTitle("PAYG");
			finder.setLedgerName("Collected");
			finder.query(rw);
			Ledger paygCollected = finder.getLedger();

			/*
			 * Fetch some people
			 */
			// TODO better finder!
			Employee person, andrew, katrina;
			person = new Employee("Andrew Cowie");
			List result = rw.queryByExample(person);
			andrew = (Employee) result.get(0);

			person = new Employee("Katrina Ross");
			result = rw.queryByExample(person);
			katrina = (Employee) result.get(0);

			/*
			 * Fetch some currencies
			 */
			// TODO This is clumsy. How about SingleObjectFinder?
			Currency cur, usd, eur, gbp, chf;

			cur = new Currency();
			cur.setCode("GBP");
			result = rw.queryByExample(cur);
			gbp = (Currency) result.get(0);

			cur.setCode("EUR");
			result = rw.queryByExample(cur);
			eur = (Currency) result.get(0);

			cur.setCode("USD");
			result = rw.queryByExample(cur);
			usd = (Currency) result.get(0);

			cur.setCode("CHF");
			result = rw.queryByExample(cur);
			chf = (Currency) result.get(0);

			cur = null;

			/*
			 * Now start storing some transactions
			 */
			Debug.print("main", "Store various transactions");

			Transaction[] initialization = {
				new GenericTransaction("Initial capitalization", new Datestamp("19 Dec 02"),
					new Entry[] {
						new Credit(new Amount("1.00"), ownersEquity),
						new Debit(new Amount("1.00"), pettyCash),
					}),
				// FIXME change to some kind of Loan Transaction?
				new GenericTransaction("Loan from owner", new Datestamp("21 Dec 02"), new Entry[] {
					new Debit(new Amount("52700.00"), bankAccount),
					new Credit(new Amount("52700.00"), shareholdersLoan),
				}),
				new GenericTransaction("Chair for office", new Datestamp("6 Jan 04"), new Entry[] {
					new Debit(new Amount("659.10"), furniture),
					new Debit(new Amount("65.90"), gstPaid),
					new Credit(new Amount("725.00"), bankAccount),
				}),
				// FIXME change Transaction type?
				new GenericTransaction("Procedures implementation ACME, Inc",
					new Datestamp("29 Aug 04"), new Entry[] {
						new Debit(new Amount("21500.00"), bankAccount),
						new Credit(new Amount("19545.45"), consultingRevenue),
						new Credit(new Amount("1954.55"), gstCollected),
					}),
				new ReimbursableExpensesTransaction(andrew, "Dinner during WSIS", new Datestamp(
					"11 Dec 03"), new Entry[] {
					new Debit(new ForeignAmount("67.00", chf, "1.0657"), travelMeals),
					new Credit(new Amount("71.40"), andrew.getExpensesPayable()),
				}),
				new ReimbursableExpensesTransaction(andrew, "Dorcheseter Hotel London", new Datestamp(
					"14 Sep 04"), new Entry[] {
					new Debit(new ForeignAmount("242.16", gbp, "2.5606"), hotels),
					new Credit(new Amount("620.07"), andrew.getExpensesPayable()),
				}),
				new ReimbursableExpensesTransaction(andrew, "Taxi from CDG to Paris", new Datestamp(
					"27 Mar 05"), new Entry[] {
					new Credit(new Amount("16.77"), andrew.getExpensesPayable()),
					new Debit(new ForeignAmount("9.99", eur, "1.67819"), groundTransport),
				}),
				new ReimbursableExpensesTransaction(katrina, "Coffee at Bacino", new Datestamp(
					"4 May 06"), new Entry[] {
					new Credit(new Amount("11.50"), katrina.getExpensesPayable()),
					new Debit(new Amount("10.45"), staffMeetings),
					new Debit(new Amount("1.05"), gstPaid),
				}),
				// FIXME change to Supplier/Bill Transaction when A/P system in
				// place
				new GenericTransaction("March phone bill", new Datestamp("15 Apr 06"), new Entry[] {
					new Debit(new ForeignAmount("55.65", usd, "1.37193"), internationalPhone),
					new Credit(new Amount("76.35"), bankAccount),
				}),
				new PayrollTransaction(andrew,
					AustralianPayrollTaxIdentifier.TAXFREE_THRESHOLD_WITH_LEAVE_LOADING, new Datestamp(
						"13 Dec 05"), new Entry[] {
						new Debit(new Amount("6572.00"), salaryExpense),
						new Credit(new Amount("572.00"), paygCollected),
						new Credit(new Amount("6000.00"), bankAccount)
					}),

			};

			// new Credit(new Amount("9.99"), pettyCash),

			for (int i = 0; i < initialization.length; i++) {
				initialization[i].setReference("R" + padZeros(i + 1, 4));
				PostTransactionCommand cmd = new PostTransactionCommand(initialization[i]);
				cmd.execute(rw);
			}

			Debug.print("main", "Committing.");
			rw.commit();

		} catch (CommandNotReadyException cnre) {
			rw.rollback();
			cnre.printStackTrace();
			System.err.println("Shouldn't have had a problem with any commands being not ready!");
		} catch (Exception e) {
			rw.rollback();
			e.printStackTrace();
		}

		Engine.releaseClient(rw);
		Engine.shutdown();

		Debug.print("main", "Done.");
	}

	/**
	 * Quickly prepend zeros to a number.
	 */
	protected static String padZeros(int num, int width) {
		StringBuffer buf = new StringBuffer(Integer.toString(num));

		for (int i = width - buf.length(); i > 0; i--) {
			buf.insert(0, '0');
		}

		return buf.toString();
	}
}