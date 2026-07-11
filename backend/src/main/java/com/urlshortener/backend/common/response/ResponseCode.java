package com.urlshortener.backend.common.response;

public enum ResponseCode {

    SUCCESS("SUCCESS", "Success", ResponseType.INFO),
    RECORDS_FETCHED("RECORDS_FETCHED", "Records fetched successfully", ResponseType.INFO),
    URL_CREATED("URL_CREATED", "Short URL created successfully", ResponseType.INFO),
    CUSTOM_ALIAS_ALREADY_EXISTS("CUSTOM_ALIAS_ALREADY_EXISTS", "Custom alias already exists", ResponseType.ERROR),
    URL_NOT_FOUND("URL_NOT_FOUND", "URL not found", ResponseType.ERROR),
    SHORTCODE_NOT_FOUND("SHORTCODE_NOT_FOUND", "Shortcode not found", ResponseType.ERROR),
    ID_NOT_FOUND("ID_NOT_FOUND", "ID not found", ResponseType.ERROR),
    VALIDATION_FAILED("VALIDATION_FAILED", "Request validation failed", ResponseType.ERROR),
    INVALID_SHORTCODE("INVALID_SHORTCODE", "Invalid shortcode", ResponseType.ERROR),
    INVALID_ID("INVALID_ID", "Invalid ID", ResponseType.ERROR),
    INVALID_KEY("INVALID_KEY", "Invalid KEY", ResponseType.ERROR),
    INVALID_URL("INVALID_URL", "Invalid URL", ResponseType.ERROR),
    INVALID_STATUS("INVALID_STATUS", "Invalid Status", ResponseType.ERROR),
    DATA_INTEGRITY_VIOLATION("DATA_INTEGRITY_VIOLATION","Request conflicts with existing data", ResponseType.ERROR),
    INTERNAL_ERROR("INTERNAL_ERROR","Something went wrong. Please try again later.", ResponseType.ERROR),
    SHORTURL_DEACTIVATED("SHORTURL_DEACTIVATED","Short URL deactivated successfully", ResponseType.INFO),
    SHORTURL_ALREADY_DEACTIVATED("SHORTURL_ALREADY_DEACTIVATED","Short URL already deactivated", ResponseType.INFO),
    SHORTURL_ACTIVATED("SHORTURL_ACTIVATED","Short URL activated successfully", ResponseType.INFO),
    SHORTURL_ALREADY_ACTIVE("SHORTURL_ALREADY_ACTIVE"," Short URL already active", ResponseType.INFO),
    URL_UPDATED("URL_UPDATED", "URL updated successfully", ResponseType.INFO),
    STATUS_CHANGE_NOT_POSSIBLE("STATUS_CHANGE_NOT_POSSIBLE", "Status change not possible", ResponseType.ERROR),
    URL_DELETED("URL_DELETED", "URL deleted successfully", ResponseType.INFO)
    ;

    private final String code;
    private final String message;
    private final ResponseType type;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ResponseType getType() {
        return type;
    }

    ResponseCode(String code, String message, ResponseType type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }


}
