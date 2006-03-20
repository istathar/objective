/*
 * AustralianInitBooksCommand.java
 * 
 * See LICENCE file for usage and redistribution terms
 * Copyright (c) 2006 Operational Dynamics
 */
package country.au.services;

import country.au.domain.AustralianPayrollTaxIdentifier;
import accounts.domain.Currency;
import accounts.domain.IdentifierGroup;
import accounts.persistence.UnitOfWork;
import accounts.services.CommandNotReadyException;
import accounts.services.InitBooksCommand;
import accounts.services.StoreIdentifierGroupCommand;

/**
 * Setup a set of books appropriate to an company domiciled in Australia.
 * 
 * @author Andrew Cowie
 */
public class AustralianInitBooksCommand extends InitBooksCommand
{
	public AustralianInitBooksCommand() {
		super();

		home = new Currency("AUD", "Australian Dollar", "$");

		/*
		 * Initialize the Australia specific Identifiers.
		 */
		AustralianPayrollTaxIdentifier.NO_TAXFREE_THRESHOLD = new AustralianPayrollTaxIdentifier(
			"No tax-free threshold");
		AustralianPayrollTaxIdentifier.TAXFREE_THRESHOLD_NO_LEAVE_LOADING = new AustralianPayrollTaxIdentifier(
			"Tax-free threshold claimed but without leave loading");
		AustralianPayrollTaxIdentifier.TAXFREE_THRESHOLD_WITH_LEAVE_LOADING = new AustralianPayrollTaxIdentifier(
			"Tax-free threshold and leave loading claimed");
		AustralianPayrollTaxIdentifier.NO_TFN_PROVIDED = new AustralianPayrollTaxIdentifier("No TFN (or ABN) quoted");

	}

	/**
	 * The bulk of the implementation is in
	 * {@link InitBooksCommand#action(UnitOfWork)}, which we call mid way
	 * through this method via super.action()
	 */
	protected void action(UnitOfWork uow) throws CommandNotReadyException {

		super.action(uow);

		IdentifierGroup grp = new IdentifierGroup("PAYG witholding types");
		grp.addIdentifier(AustralianPayrollTaxIdentifier.NO_TAXFREE_THRESHOLD);
		grp.addIdentifier(AustralianPayrollTaxIdentifier.TAXFREE_THRESHOLD_NO_LEAVE_LOADING);
		grp.addIdentifier(AustralianPayrollTaxIdentifier.TAXFREE_THRESHOLD_WITH_LEAVE_LOADING);
		grp.addIdentifier(AustralianPayrollTaxIdentifier.NO_TFN_PROVIDED);

		StoreIdentifierGroupCommand sigc = new StoreIdentifierGroupCommand(grp);
		sigc.execute(uow);

	}

}
