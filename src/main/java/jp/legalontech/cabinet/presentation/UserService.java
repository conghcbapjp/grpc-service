package jp.legalontech.cabinet.presentation;

import io.grpc.stub.StreamObserver;
import jp.legalontech.cabinet.UserServiceGrpc;
import jp.legalontech.cabinet.UserServiceOuterClass;
import jp.legalontech.cabinet.UserServiceOuterClass.AddUserResponse;
import jp.legalontech.cabinet.UserServiceOuterClass.AddUserRequest;
import jp.legalontech.cabinet.infra.UserRepository;
import jp.legalontech.cabinet.infra.entity.AddUserParamEntity;
import jp.legalontech.cabinet.model.User;
import jp.legalontech.cabinet.usecase.UserUseCase;

public class UserService extends UserServiceGrpc.UserServiceImplBase {
    private final UserUseCase useCase = new UserUseCase(new UserRepository());

    /**
     * Add new a user
     * @param request AddUserRequest
     * @param responseObserver UserServiceOuterClass.User
     */
    @Override
    public void addUser(AddUserRequest request, StreamObserver<AddUserResponse> responseObserver) {
        User user = useCase.addUser(new AddUserParamEntity(
                request.getMail(),
                request.getPassword(),
                request.getRole()
        ));

        if (user != null) {
            UserServiceOuterClass.User userInserted = UserServiceOuterClass.User.newBuilder()
                    .setId(user.id())
                    .setMail(user.mail())
                    .setRole(user.role())
                    .build();

            AddUserResponse result = AddUserResponse.newBuilder()
                    .setData(userInserted)
                    .setMessage("Create successfully.")
                    .build();
            responseObserver.onNext(result);
        } else {
            AddUserResponse resultEmpty = AddUserResponse.newBuilder()
                    .setMessage("Create failure.")
                    .build();
            responseObserver.onNext(resultEmpty);
        }

        responseObserver.onCompleted();
    }
}
