package jp.legalontech.cabinet.infra.entity;

import com.linecorp.armeria.common.annotation.Nullable;

public record AddUserParamEntity(String mail, @Nullable String password, int role) {
}
