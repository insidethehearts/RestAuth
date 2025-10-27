package me.therimuru.RestAuth.mapper;

import me.therimuru.RestAuth.dto.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Mapping(target = "login", source = "username")
    public abstract UserEntity userSignUpDTOToUserEntity(UserSignUpDTO dto);

    @Mapping(target = "username", source = "login")
    public abstract UserSignUpDTO userEntityToUserSignUpDTO(UserEntity entity);

}