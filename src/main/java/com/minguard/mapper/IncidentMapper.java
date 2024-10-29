package com.minguard.mapper;

import com.minguard.dto.incident.IncidentResponse;
import com.minguard.dto.incident.RegisterIncidentRequest;
import com.minguard.dto.incident.RegisterIncidentResponse;
import com.minguard.dto.incident.UpdateIncidentRequest;
import com.minguard.entity.Incident;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IncidentMapper {

    IncidentMapper INSTANCE = Mappers.getMapper(IncidentMapper.class);

    @Mapping(target = "urgency", ignore = true)
    @Mapping(target = "reporter", ignore = true)
    Incident fromRegisterRequest(RegisterIncidentRequest request);

    RegisterIncidentResponse toRegisterResponse(Incident incident);

    IncidentResponse toResponse(Incident incident);

    List<IncidentResponse> toResponses(List<Incident> incident);

    @Mapping(target = "urgency", ignore = true)
    @Mapping(target = "reporter", ignore = true)
    void fromUpdateRequest(@MappingTarget Incident incident, UpdateIncidentRequest request);

}
