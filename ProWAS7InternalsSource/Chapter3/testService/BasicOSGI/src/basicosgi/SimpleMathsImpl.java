package basicosgi;

import java.math.BigDecimal;

public class SimpleMathsImpl implements SimpleMaths {

	public BigDecimal add(BigDecimal first, BigDecimal second) {
		return (first.add(second));
	}

	public BigDecimal subtract(BigDecimal first, BigDecimal second) {
		return (first.subtract(second));
	}

	public BigDecimal multiply(BigDecimal first, BigDecimal second) {
		return (first.multiply(second));
	}

	public BigDecimal divide(BigDecimal first, BigDecimal second) {
		return (first.divide(second));
	}
}
