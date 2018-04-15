/*
 * Copyright (c) 2012, 2018, Werner Keil, Anatole Tresch and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Contributors: @manuela-grindei
 */
package org.javamoney.calc.securities;

import java.math.BigDecimal;

import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;

import org.javamoney.calc.common.Rate;

/**
 * A zero coupon bond, sometimes referred to as a pure discount bond or simply discount bond, is a bond that does not pay coupon payments and instead pays one lump sum at maturity. The amount paid at maturity is called the face value. The term discount bond is used to reference how it is sold originally at a discount from its face value instead of standard pricing with periodic dividend payments as seen otherwise.
 *
 * @author Manuela Grindei
 * @see <a href="http://www.financeformulas.net/Zero_Coupon_Bond_Value.html">http://www.financeformulas.net/Zero_Coupon_Bond_Value.html</a>
 */
public class ZeroCouponBondValue implements MonetaryOperator {

	
	private final Rate rate;
	
	private final int numberOfYearsToMaturity;
	
    /**
     * Private constructor.
     */
	private ZeroCouponBondValue(Rate rate, int numberOfYearsToMaturity) {
		this.rate = rate;
		this.numberOfYearsToMaturity = numberOfYearsToMaturity;
    }

    /**
     * Gets rate.
     *
     * @return the rate
     */
    public Rate getRate() {
		return rate;
	}

    /**
     * Gets number of years to maturity.
     *
     * @return the number of years to maturity
     */
    public int getNumberOfYearsToMaturity() {
		return numberOfYearsToMaturity;
	}

    /**
     * Access a MonetaryOperator for calculation.
     *
     * @param rate                    the rate
     * @param numberOfYearsToMaturity the number of years to maturity
     * @return the operator
     */
    public static ZeroCouponBondValue of(Rate rate, int numberOfYearsToMaturity) {
		return new ZeroCouponBondValue(rate, numberOfYearsToMaturity);
	}

    /**
     * Calculates the zero coupon bond value.
     *
     * @param face                    the face value of the bond
     * @param rate                    the rate
     * @param numberOfYearsToMaturity the number of years to maturity
     * @return the zero coupon bond value
     */
    public static MonetaryAmount calculate(MonetaryAmount face, Rate rate, int numberOfYearsToMaturity) {
        return face.divide(BigDecimal.ONE.add(rate.get()).pow(numberOfYearsToMaturity));
	}
	
	@Override
	public MonetaryAmount apply(MonetaryAmount face) {
		return calculate(face, rate, numberOfYearsToMaturity);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		ZeroCouponBondValue that = (ZeroCouponBondValue) o;
		
		return numberOfYearsToMaturity == that.numberOfYearsToMaturity && rate.equals(that.rate);
		
	}
	
	@Override
	public int hashCode() {
		int result = rate.hashCode();
		result = 31 * result + numberOfYearsToMaturity;
		return result;
	}
	
	@Override
	public String toString() {
		return "ZeroCouponBondValue{" + "rate=" + rate + ", numberOfYearsToMaturity=" + numberOfYearsToMaturity + '}';
    }
}
