package gr.harborlab.exception;


public class ServerError {

    private String message;
    private String tag;

    public ServerError(String message, String tag) {
        super();
        this.message = message;
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }



}
