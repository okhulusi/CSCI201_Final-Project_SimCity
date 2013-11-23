package city.bank.interfaces;

import city.bank.BankTellerRole;

public interface BankCustomer {
	public void msgWeAreClosed();
	public void msgCalledToDesk(BankTellerRole teller);
	public void msgHereIsInfoPickARequest(double funds, double amountOwed);
	public void msgTransactionComplete(double amountReceived, double funds, double amountOwed);
	public void msgTransactionDenied();
}
