package jp.legalontech.cabinet.presentation;

import io.grpc.stub.StreamObserver;
import jp.legalontech.cabinet.AzureServiceOuterClass;
import jp.legalontech.cabinet.LoginServiceGrpc;
import jp.legalontech.cabinet.LoginServiceOuterClass.LoginRequest;
import jp.legalontech.cabinet.LoginServiceOuterClass.LoginResponse;
import jp.legalontech.cabinet.UserServiceOuterClass;
import jp.legalontech.cabinet.infra.UserRepository;
import jp.legalontech.cabinet.infra.entity.AuthenticationAzureParamEntity;
import jp.legalontech.cabinet.infra.entity.LoginParamEntity;
import jp.legalontech.cabinet.model.AccessToken;
import jp.legalontech.cabinet.model.User;
import jp.legalontech.cabinet.usecase.AzureUseCase;
import jp.legalontech.cabinet.usecase.LoginUseCase;

public class LoginService extends LoginServiceGrpc.LoginServiceImplBase {
    private final LoginUseCase useCase = new LoginUseCase(new UserRepository());
    private final AzureUseCase azureUseCase = new AzureUseCase();

    /**
     * Login service
     * @param request RequestLogin
     * @param responseObserver ResponseLogin
     */
    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        User user = useCase.login(new LoginParamEntity(request.getMail(), request.getPassword()));
        if (user != null) {
            AccessToken accessTokenAzure = azureUseCase.getAccessToken(new AuthenticationAzureParamEntity(request.getMail(), request.getPassword()));
            if (accessTokenAzure != null) {
                AzureServiceOuterClass.GetAccessTokenResponse responseAzure = AzureServiceOuterClass.GetAccessTokenResponse.newBuilder()
                        .setTokenType(accessTokenAzure.getTokenType())
                        .setScope(accessTokenAzure.getScope())
                        .setExpiresIn(accessTokenAzure.getExpiresIn())
                        .setExtExpiresIn(accessTokenAzure.getExtExpiresIn())
                        .setExpiresOn(accessTokenAzure.getExpiresOn())
                        .setNotBefore(accessTokenAzure.getNotBefore())
                        .setResource(accessTokenAzure.getResource())
                        .setAccessToken(accessTokenAzure.getAccessToken())
                        .setRefreshToken(accessTokenAzure.getRefreshToken())
                        .build();

                UserServiceOuterClass.User data = UserServiceOuterClass.User.newBuilder()
                        .setId(user.id())
                        .setMail(user.mail())
                        .setRole(user.role())
                        .build();
                LoginResponse result = LoginResponse.newBuilder()
                        .setData(data)
                        .setAzureInfo(responseAzure)
                        .setMessage("Login successfully.")
                        .build();

                responseObserver.onNext(result);
            }
        } else {
            LoginResponse emptyResult = LoginResponse.newBuilder()
                    .setMessage("Login failure.")
                    .build();
            responseObserver.onNext(emptyResult);
        }

        responseObserver.onCompleted();
    }
}
