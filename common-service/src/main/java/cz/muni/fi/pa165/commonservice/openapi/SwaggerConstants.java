package cz.muni.fi.pa165.commonservice.openapi;

public final class SwaggerConstants {

    public static final String SECURITY_SCHEME_NAME = "MUNI";

    public static final String READ_SCOPE = "test_read";
    public static final String WRITE_SCOPE = "test_write";

    public static final class ResponseCodes {

        public static final String OK = "200";
        public static final String CREATED = "201";
        public static final String NO_CONTENT = "204";
        public static final String BAD_REQUEST = "400";
        public static final String UNAUTHORIZED = "401";
        public static final String FORBIDDEN = "403";
        public static final String NOT_FOUND = "404";
        public static final String INTERNAL_ERROR = "500";

        private ResponseCodes() {
        }
    }

    public static final class ResponseDescriptions {

        public static final String BAD_REQUEST = "Invalid request payload or parameters";
        public static final String UNAUTHORIZED = "Access token is missing or invalid";
        public static final String FORBIDDEN = "Missing required scope";
        public static final String NOT_FOUND = "Resource not found";
        public static final String INTERNAL_ERROR = "Unexpected server error";

        private ResponseDescriptions() {
        }
    }

    private SwaggerConstants() {
    }
}
