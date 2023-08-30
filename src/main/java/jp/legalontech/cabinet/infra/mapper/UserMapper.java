package jp.legalontech.cabinet.infra.mapper;

import jp.legalontech.cabinet.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    /**
     * Get user by email
     * @param mail email address
     * @return User
     */
    User getUserByEmail(@Param("mail") String mail);

    /**
     * Add new a user
     * @param mail email address
     * @param password password
     * @param role role
     */
    void addUser(@Param("mail") String mail, @Param("password") String password, @Param("role") int role);
}
