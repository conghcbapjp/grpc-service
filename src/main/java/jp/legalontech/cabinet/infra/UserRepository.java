package jp.legalontech.cabinet.infra;

import com.linecorp.armeria.common.annotation.Nullable;
import jp.legalontech.cabinet.Main;
import jp.legalontech.cabinet.infra.entity.AddUserParamEntity;
import jp.legalontech.cabinet.infra.mapper.UserMapper;
import jp.legalontech.cabinet.model.User;
import org.apache.ibatis.session.SqlSession;

public class UserRepository {
    /**
     * Get user by email
     * @param mail email
     * @return User
     */
    public @Nullable User getUserByEmail(String mail) {
        try (SqlSession session = Main.getInstance().getSqlSessionFactory().openSession()) {
            return session.getMapper(UserMapper.class).getUserByEmail(mail);
        }
    }

    /**
     * Add new a user
     * @param params AddUserParamEntity
     */
    public void addUser(AddUserParamEntity params) {
        try (SqlSession session = Main.getInstance().getSqlSessionFactory().openSession()) {
            session.getMapper(UserMapper.class).addUser(
                    params.mail(),
                    params.password(),
                    params.role()
            );

            session.commit();
        }
    }
}
