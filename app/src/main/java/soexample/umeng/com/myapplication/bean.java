package soexample.umeng.com.myapplication;

/**
 * Created by Shinelon on 2018/11/22.
 */

public class bean {
    private String headPath;
    private String message;
    private String status;

    public bean() {
    }

    public bean(String headPath, String message, String status) {
        this.headPath = headPath;
        this.message = message;
        this.status = status;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
