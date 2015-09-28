package com.eligible.net;

import com.eligible.Eligible;
import lombok.*;
import lombok.experimental.Accessors;

import java.security.InvalidParameterException;

import static com.eligible.util.StringUtil.normalizeString;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestOptions {
    public static RequestOptions getDefault() {
        return new RequestOptions(Eligible.apiKey, Eligible.apiVersion, Eligible.isTest);
    }

    private final String apiKey;
    private final String eligibleVersion;
    private final boolean test;

    public static RequestOptionsBuilder builder() {
        return new RequestOptionsBuilder();
    }

    public RequestOptionsBuilder toBuilder() {
        return new RequestOptionsBuilder(this.apiKey, this.eligibleVersion, this.test);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain=true)
    public static final class RequestOptionsBuilder {
        private String apiKey = Eligible.apiKey;
        private String eligibleVersion = Eligible.apiVersion;
        private boolean test = Eligible.isTest;

        public RequestOptionsBuilder clearApiKey() {
            return setApiKey(null);
        }

        public RequestOptionsBuilder clearEligibleVersion() {
            return setEligibleVersion(null);
        }

        public RequestOptionsBuilder clearTest() {
            return setTest(false);
        }

        public RequestOptions build() {
            return new RequestOptions(normalizeString(this.apiKey), normalizeString(this.eligibleVersion), this.test);
        }
    }

    public static class InvalidRequestOptionsException extends InvalidParameterException {
        public InvalidRequestOptionsException(String message) {
            super(message);
        }
    }
}
