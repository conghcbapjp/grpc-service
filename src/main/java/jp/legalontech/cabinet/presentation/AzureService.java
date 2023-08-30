package jp.legalontech.cabinet.presentation;

import io.grpc.stub.StreamObserver;
import jp.legalontech.cabinet.AzureServiceGrpc;
import jp.legalontech.cabinet.GetAccessTokenRequest;
import jp.legalontech.cabinet.GetAccessTokenResponse;
import jp.legalontech.cabinet.AuthenticationResponse;
import jp.legalontech.cabinet.infra.entity.AuthenticationAzureParamEntity;
import jp.legalontech.cabinet.model.AccessToken;
import jp.legalontech.cabinet.usecase.AzureUseCase;

public class AzureService extends AzureServiceGrpc.AzureServiceImplBase {
    private final AzureUseCase useCase = new AzureUseCase();

    /**
     * Get info authentication
     * @param request GetAccessTokenRequest
     * @param responseObserver GetAccessTokenResponse
     */
    @Override
    public void getAccessToken(GetAccessTokenRequest request, StreamObserver<AuthenticationResponse> responseObserver) {
        AccessToken responseBody = useCase.getAccessToken(new AuthenticationAzureParamEntity(request.getUsername(), request.getPassword()));

        if (responseBody != null) {
            GetAccessTokenResponse result = GetAccessTokenResponse.newBuilder()
                    .setTokenType(responseBody.getTokenType())
                    .setScope(responseBody.getScope())
                    .setExpiresIn(responseBody.getExpiresIn())
                    .setExtExpiresIn(responseBody.getExtExpiresIn())
                    .setExpiresOn(responseBody.getExpiresOn())
                    .setNotBefore(responseBody.getNotBefore())
                    .setResource(responseBody.getResource())
                    .setAccessToken(responseBody.getAccessToken())
                    .setRefreshToken(responseBody.getRefreshToken())
                    .build();
            responseObserver.onNext(AuthenticationResponse.newBuilder().setData(result).build());
        } else {
            responseObserver.onNext(AuthenticationResponse.newBuilder().setMessage("Unauthorized in the Azure.").build());
        }
        responseObserver.onCompleted();
    }
}
