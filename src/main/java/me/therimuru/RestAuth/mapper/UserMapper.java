package me.therimuru.RestAuth.mapper;

import me.therimuru.RestAuth.dto.requests.UserSignUpDTO;
import me.therimuru.RestAuth.dto.responses.RegisteredUserDTO;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.object.JwtInformationWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Mapping(target = "id", ignore = true)
    public abstract UserEntity userSignUpDTOToUserEntity(UserSignUpDTO dto);

    public abstract UserSignUpDTO userEntityToUserSignUpDTO(UserEntity entity);

    public abstract RegisteredUserDTO userEntityToRegisteredUserDTO(UserEntity userEntity);

    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "issuedAt", ignore = true)
    public abstract JwtInformationWrapper userEntityToJwtInformationWrapper(UserEntity userEntity);

}