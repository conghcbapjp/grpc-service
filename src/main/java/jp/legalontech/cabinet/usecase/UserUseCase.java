package jp.legalontech.cabinet.usecase;

import com.linecorp.armeria.common.annotation.Nullable;
import jp.legalontech.cabinet.infra.UserRepository;
import jp.legalontech.cabinet.infra.entity.AddUserParamEntity;
import jp.legalontech.cabinet.model.User;

public class UserUseCase {
    private final UserRepository repository;

    public UserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Add new a user
     * @param params AddUserParamEntity
     */
    public @Nullable User addUser(AddUserParamEntity params) {
        User existingUser = getUserByEmail(params.mail());
        if (existingUser != null) {
            return null;
        }
        repository.addUser(params);
        return getUserByEmail(params.mail());
    }

    /**
     * Get user by email
     * @param mail mail
     * @return User
     */
    public @Nullable User getUserByEmail(String mail) {
        return repository.getUserByEmail(mail);
    }
}
