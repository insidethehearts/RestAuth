package me.therimuru.RestAuth.mapper;

import me.therimuru.RestAuth.dto.UserSignUpDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserEntity userSignUpDTOToUserEntity(UserSignUpDTO dto);

    UserSignUpDTO userEntityToUserSignUpDTO(UserEntity entity);

}