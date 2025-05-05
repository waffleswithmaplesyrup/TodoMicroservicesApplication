package com.waffles.auth_microservice.StandardResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestResponse {

    private int code;
    private String message;
    private Object data;

    public RestResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RestResponse failure(String message) {
        return new RestResponse(0, message, null);
    }

    public RestResponse readSuccess(Object data) {
        return new RestResponse(1, "Resource retrieved successfully", data);
    }

    public RestResponse createSuccess(Object data) {
        return new RestResponse(2, "Resource created successfully", data);
    }

    public RestResponse deleteSuccess(Object data) {
        return new RestResponse(3, "Resource deleted successfully", data);
    }

    public RestResponse updateSuccess(Object data) {
        return new RestResponse(4, "Resource updated successfully", data);
    }
}
