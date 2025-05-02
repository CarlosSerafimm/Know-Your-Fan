package backend.model.mapper;

import backend.DTO.RequestFanDTO;
import backend.DTO.ResponseFanDTO;
import backend.model.Fan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FanMapper {
    Fan responseFanToEntity(ResponseFanDTO responseFanDTO);
    ResponseFanDTO entityToResponseFan(Fan fan);
    Fan requestFanToEntity(RequestFanDTO requestFanDTO);

    void updateEntityFromDto(RequestFanDTO dto, @MappingTarget Fan fan);
}
