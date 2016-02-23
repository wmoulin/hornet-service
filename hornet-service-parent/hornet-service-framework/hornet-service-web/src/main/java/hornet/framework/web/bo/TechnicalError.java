package hornet.framework.web.bo;

public class TechnicalError {
	
	/**
	 * <code>code</code> the exception name
	 */
	private final String name;

	/**
     * <code>code</code> the exception code
     */
    private final String code;
    
    /**
     * <code>message</code> the exception message
     */
    private final String message;
    
    /**
     * <code>message</code> the exception message
     */
    private final StackTraceElement[] stackTrace;

	public TechnicalError(final String name, final String code, final String message,
			final StackTraceElement[] stackTrace) {
		super();
		this.name = name;
		this.code = code;
		this.message = message;
		this.stackTrace = stackTrace;
	}
	
	public String getName() {
		return name;
	}
    
    public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}


}
