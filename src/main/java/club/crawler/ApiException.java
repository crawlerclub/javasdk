package club.crawler;

public class ApiException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3018541229000155386L;
	private String errorMsg;
    private int errorCode;

    public ApiException(int code, String msg) {
        errorCode = code;
        errorMsg = msg;
    }

    public ApiException(int code, Throwable e) {
        errorCode = code;
        errorMsg = e.getMessage();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
