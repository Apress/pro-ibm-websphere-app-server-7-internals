package basicosgi;

import java.math.BigDecimal;

public interface SimpleMaths {
	
	public BigDecimal add(BigDecimal first, BigDecimal second);
	public BigDecimal subtract(BigDecimal first, BigDecimal second);
	public BigDecimal multiply(BigDecimal first, BigDecimal second);
	public BigDecimal divide(BigDecimal first, BigDecimal second);	
}
