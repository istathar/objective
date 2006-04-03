/*
 * PayrollTaxCalculator.java
 * 
 * See LICENCE file for usage and redistribution terms
 * Copyright (c) 2006 Operational Dynamics
 */
package accounts.services;

import accounts.domain.Amount;
import accounts.domain.Datestamp;

/**
 * Work out the payroll tax or other emploment related deductions appropriate to
 * a given salary or paycheck receieved. Either salary or paycheck withholding
 * can be worked out given the other. There are two [abstract] use cases that
 * subclasses need to implement where the actual calculation work should be
 * done.
 * 
 * @author Andrew Cowie
 */
public abstract class PayrollTaxCalculator extends TaxCalculator
{
	protected transient Amount	salary;
	protected transient Amount	withhold;
	protected transient Amount	paycheck;

	/**
	 * @param asAtDate
	 *            see {@link TaxCalculator#TaxCalculator(Datestamp)}
	 */
	protected PayrollTaxCalculator(Datestamp asAtDate) {
		super(asAtDate);
	}

	public Amount getSalary() {
		return salary;
	}

	/**
	 * Set the salary the Employee was given.
	 * 
	 * @param salary
	 *            The positive Amount from which the necessary deduction will be
	 *            worked out.
	 */
	public void setSalary(Amount salary) {
		if (salary == null) {
			throw new IllegalArgumentException("Can't use null as the salary.");
		}
		if (salary.getNumber() < 0) {
			throw new IllegalArgumentException("Can't set a negative value as the salary.");
		}
		if (salary == paycheck) {
			throw new IllegalArgumentException("You can't use the same Amount object for paycheck and salary fields.");
		}

		this.salary = salary;
	}

	public Amount getPaycheck() {
		return paycheck;
	}

	/**
	 * Set the paycheck the Employee actually received.
	 * 
	 * @param salary
	 *            The positive Amount which is the result of subtracting
	 *            neccessary withholding taxes from the salary an Employee is
	 *            given. Note that this must be a different object from the
	 *            salary Amount.
	 */
	public void setPaycheck(Amount paid) {
		if (paid == null) {
			throw new IllegalArgumentException("Can't use null as the paid amount.");
		}
		if (paid.getNumber() < 0) {
			throw new IllegalArgumentException("Can't set a negative value as the paid amount.");
		}
		if (paid == salary) {
			throw new IllegalArgumentException("You can't use the same Amount object for paycheck and salary fields.");
		}

		this.paycheck = paid;
	}

	/**
	 * Get the Amount to withhold, assuming it has been calculated.
	 * 
	 * @return
	 */
	public Amount getWithhold() {
		return withhold;
	}

	public abstract void calculateGivenPayable();

	public abstract void calculateGivenSalary();

}