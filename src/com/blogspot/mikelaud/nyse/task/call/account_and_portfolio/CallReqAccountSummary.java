package com.blogspot.mikelaud.nyse.task.call.account_and_portfolio;

import com.blogspot.mikelaud.nyse.connection.ConnectionContext;
import com.blogspot.mikelaud.nyse.task.Task;
import com.blogspot.mikelaud.nyse.task.call.CallTaskEx;
import com.blogspot.mikelaud.nyse.task.call.CallType;

/**
 * Call this call to request and keep up to date the data
 * that appears on the TWS Account Window Summary tab.
 * The data is returned by OnAccountSummary.
 * 
 * Note: This request can only be made when connected
 *       to a Financial Advisor (FA) account.
 */
public class CallReqAccountSummary
	extends CallTaskEx<CallReqAccountSummary.Info>
{
	//------------------------------------------------------------------------
	public static class Info {
	
		/**
		 * The ID of the data request.
		 * Ensures that responses are matched to requests
		 * if several requests are in process.
		 */
		public final int REQ_ID;
		/**
		 * Set to All to return account summary data for all accounts,
		 * or set to a specific Advisor Account Group name
		 * that has already been created in TWS Global Configuration.
		 */
		public final String GROUP;
		/**
		 * A comma-separated list of account tags. Available tags are:
		 *     AccountType
		 *     NetLiquidation
		 *     TotalCashValue — Total cash including futures pnl
		 *     SettledCash — For cash accounts,
		 *                   this is the same as TotalCashValue
		 *     AccruedCash — Net accrued interest
		 *     BuyingPower — The maximum amount of marginable US stocks
		 *                   the account can buy
		 *     EquityWithLoanValue — Cash + stocks + bonds + mutual funds
		 *     PreviousEquityWithLoanValue
		 *     GrossPositionValue — The sum of the absolute value of all
		 *                          stock and equity option positions
		 *     RegTEquity
		 *     RegTMargin
		 *     SMA — Special Memorandum Account
		 *     InitMarginReq
		 *     MaintMarginReq
		 *     AvailableFunds
		 *     ExcessLiquidity
		 *     Cushion — Excess liquidity as a percentage
		 *               of net liquidation value
		 *     FullInitMarginReq
		 *     FullMaintMarginReq
		 *     FullAvailableFunds
		 *     FullExcessLiquidity
		 *     LookAheadNextChange — Time when look-ahead values
		 *                           take effect
		 *     LookAheadInitMarginReq
		 *     LookAheadMaintMarginReq
		 *     LookAheadAvailableFunds
		 *     LookAheadExcessLiquidity
		 *     HighestSeverity — A measure of how close
		 *                       the account is to liquidation
		 *     DayTradesRemaining — The Number of Open/Close trades a user
		 *                          could put on before Pattern Day Trading
		 *                          is detected. A value of "-1" means that
		 *                          the user can put on unlimited day trades.
		 *     Leverage — GrossPositionValue / NetLiquidation
		 */
		public final String TAGS;		
		
		public Info(int aReqId, String aGroup, String aTags) {
			REQ_ID = aReqId;
			GROUP = aGroup;
			TAGS = aTags;
		}
		
	}
	//------------------------------------------------------------------------

	@Override
	protected Task onCall() throws Exception {
		getClientSocket().reqAccountSummary
		(	INFO.REQ_ID
		,	INFO.GROUP
		,	INFO.TAGS
		);
		return null;
	}

	@Override
	public String toString() {
		return String.format
		(	"%s(\"%s\")[%d] { group=\"%s\" }"
		,	super.toString()
		,	INFO.TAGS
		,	INFO.REQ_ID
		,	INFO.GROUP
		);
	}

	public CallReqAccountSummary(ConnectionContext aContext, Info aInfo) {
		super(aContext, aInfo, CallType.reqAccountSummary);
	}

	public CallReqAccountSummary
	(	ConnectionContext aContext
	,	int aReqId
	,	String aGroup
	,	String aTags
	) {
		this(aContext, new Info
		(	aReqId
		,	aGroup
		,	aTags
		));
	}

}
