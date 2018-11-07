
package com.marshmallowsocks.msfinance.core.response;

public class CreateGroupResponse {

    private boolean error;
    private String id;
    private String message;

    public CreateGroupResponse(boolean error, String id, String message) {
        this.error = error;
        this.id = id;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}