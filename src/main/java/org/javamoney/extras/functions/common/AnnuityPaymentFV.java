package org.javamoney.extras.functions.common;

import java.math.BigDecimal;

import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;

import org.javamoney.extras.functions.CompoundFunction;
import org.javamoney.extras.functions.CompoundType;
import org.javamoney.extras.functions.CompoundValue;

/**
 * The annuity payment formula shown above is used to calculate the cash flows
 * of an annuity when future value is known. An annuity is denoted as a series
 * of periodic payments. The annuity payment formula shown here is specifically
 * used when the future value is known, as opposed to the annuity payment
 * formula used when present value is known. There are not only mathematical
 * differences between calculating an annuity when present value is known and
 * when future value is known, but also differences in the real life application
 * of the formulas. For example, if an individual is wanting to calculate the
 * payments on a loan, the original loan balance would be considered the present
 * value and the payment formula would accommodate this known variable. However,
 * if an individual is wanting to calculate how much they need to save per year
 * in an interest bearing account to have a certain balance after a specific
 * period of time, then this wanted balance would be considered the future
 * value. The latter example would use the annuity payment using future value
 * formula as the balance is increasing instead of decreasing:
 * <p>
 * <img src=
 * "http://www.financeformulas.net/Formula%20Images/Annuity%20Payment%20(FV)%201.gif"
 * />
 * <p>
 * or...
 * 
 * <pre>
 * FV(r) / (((1 + r).pow(n)) - 1)
 * </pre>
 * 
 * @see http://www.financeformulas.net/Annuity-Payment-from-Future-Value.html
 * @author Anatole Tresch
 * 
 */
public class AnnuityPaymentFV implements MonetaryOperator,
		CompoundFunction<MonetaryAmount> {

	private Rate rate;
	private int periods;

	private CompoundType INPUT_TYPE = new CompoundType.Builder()
			.withIdForInput(AnnuityPaymentFV.class)
			.withRequiredArg("rate", Rate.class)
			.withRequiredArg("periods", Integer.class)
			.withRequiredArg("amount", MonetaryAmount.class).build();

	public AnnuityPaymentFV(Rate rate, int periods) {
		if (rate == null) {
			throw new IllegalArgumentException("rate null.");
		}
		this.rate = rate;
		this.periods = periods;
	}

	@Override
	public MonetaryAmount apply(MonetaryAmount value) {
		// FV(r) / (((1 + r).pow(n))-1)
		FutureValue fv = new FutureValue(rate, periods);
		return fv.apply(value).divide(
				BigDecimal.ONE.add(rate.getRate()).pow(periods)
						.subtract(BigDecimal.ONE)
				);
	}

	@Override
	public CompoundType getInputTape() {
		return INPUT_TYPE;
	}

	@Override
	public Class<MonetaryAmount> getResultType() {
		return MonetaryAmount.class;
	}

	@Override
	public MonetaryAmount calculate(CompoundValue input) {
		if (input.getCompoundType().equals(INPUT_TYPE)) {
			throw new IllegalArgumentException("Invalid input type, required: "
					+ INPUT_TYPE + ", but was " + input);
		}
		int p = input.get("periods", Integer.class);
		Rate r = input.get("rate", Rate.class);
		MonetaryAmount amt = input.get("amount", MonetaryAmount.class);
		FutureValue fv = new FutureValue(r, p);
		return fv.apply(amt).divide(
				BigDecimal.ONE.add(r.getRate()).pow(periods)
						.subtract(BigDecimal.ONE)
				);
	}
}
