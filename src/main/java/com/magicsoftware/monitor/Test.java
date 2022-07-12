package com.magicsoftware.monitor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//MathContext mc = new MathContext(8);
		//BigDecimal bigDecimal = new BigDecimal(20.0, mc);
		//System.out.println(bigDecimal.doubleValue()); // 20.0
		//System.out.println(Test.foo(20.0));
		
		//double myValue = 0.00000021;

		//DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		//df.setMaximumFractionDigits(340); // 340 = DecimalFormat.DOUBLE_FRACTION_DIGITS

		//System.out.println(df.format(myValue));
		
		//System.out.println( new Double(1213188888).toString());
		
		//System.out.println(new BigDecimal("20.0").stripTrailingZeros().toPlainString());
		
		System.out.println(Test.doubleToString(1213188888));
		
	}
	
	public static String doubleToString(final double d) {
	    // check for integer, also see https://stackoverflow.com/a/9898613/868941 and
	    // https://github.com/google/guava/blob/master/guava/src/com/google/common/math/DoubleMath.java
	    if (isMathematicalInteger(d)) {
	        return Long.toString((long)d);
	    } else {
	        // or use any of the solutions provided by others, this is the best
	        DecimalFormat df = 
	            new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	        df.setMaximumFractionDigits(340); // 340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
	        return df.format(d);
	    }
	}

	// Java 8+
	public static boolean isMathematicalInteger(final double d) {
	    return StrictMath.rint(d) == d && Double.isFinite(d);
	}
	
	
	   public static String foo(double value) //Got here 6.743240136E7 or something..
	    {
	        DecimalFormat formatter;

	        if(value - (int)value > 0.0)
	            formatter = new DecimalFormat("0.00");
	        else
	            formatter = new DecimalFormat("0");

	        return formatter.format(value);
	    }

}
