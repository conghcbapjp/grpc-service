package jp.legalontech.cabinet.usecase;

import com.linecorp.armeria.common.annotation.Nullable;
import jp.legalontech.cabinet.infra.UserRepository;
import jp.legalontech.cabinet.infra.entity.LoginParamEntity;
import jp.legalontech.cabinet.model.User;

public class LoginUseCase {
    private final UserRepository repository;

    public LoginUseCase(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Handle login
     * @param params LoginParamEntity
     * @return User
     */
    public @Nullable User login(LoginParamEntity params) {
        User user = repository.getUserByEmail(params.mail());

        if (user != null && user.password().equals(params.password())) {
            return new User(user.id(), user.mail(), user.role(), user.password());
        }

        return null;
    }
}
