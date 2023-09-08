package com.hugorithm.hopfencraft.utils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.math.BigDecimal;

public class BigDecimalMatcher  extends TypeSafeMatcher<BigDecimal> {

    private final BigDecimal expectedValue;

    BigDecimalMatcher(BigDecimal expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    protected boolean matchesSafely(BigDecimal actualValue) {
        // Use compareTo to compare BigDecimal values
        return actualValue.compareTo(expectedValue) == 0;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a BigDecimal equal to ").appendValue(expectedValue);
    }

    public static BigDecimalMatcher equalTo(BigDecimal expectedValue) {
        return new BigDecimalMatcher(expectedValue);
    }

}
