package jp.legalontech.cabinet.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.annotation.Nullable;
import jp.legalontech.cabinet.infra.entity.AuthenticationAzureParamEntity;
import jp.legalontech.cabinet.model.AccessToken;

public class AzureUseCase {
    private static final String CLIENT_ID = "a1e6508b-2944-4640-b4d2-81db8b70b3f9";
    private static final String CLIENT_SECRET = "8Vq8Q~3YFv0Sr.GAT16K2_kUbei.p1an1E22SdCi";
    private static final String USERNAME = "tantn@bap.jp";
    private static final String PASSWORD = "Thewindlove9!()";
    private static final String SCOPE = "User.Read";
    private static final String TOKEN_PATH = "de588638-000a-4853-bc1d-53510403fd8f/oauth2/token";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public @Nullable AccessToken getAccessToken(AuthenticationAzureParamEntity params) {
        String requestBody = String.format("grant_type=password&client_id=%s&client_secret=%s&username=%s&password=%s&scope=%s&resource=%s&name=%s",
                CLIENT_ID, CLIENT_SECRET, USERNAME, PASSWORD, SCOPE, CLIENT_ID, USERNAME);
        WebClient webClient = WebClient.of("https://login.microsoftonline.com");
        AggregatedHttpResponse response = webClient.post(TOKEN_PATH, requestBody)
                .aggregate().join();

        if (response.status().code() != 200) {
            return null;
        }

        String responseBodyUtf8 = response.contentUtf8();
        try {
            return objectMapper.readValue(responseBodyUtf8, AccessToken.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
