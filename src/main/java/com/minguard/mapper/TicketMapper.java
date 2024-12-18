package com.minguard.mapper;

import com.minguard.dto.ticket.RegisterTicketRequest;
import com.minguard.dto.ticket.TicketExtendedResponse;
import com.minguard.dto.ticket.TicketResponse;
import com.minguard.dto.ticket.UpdateTicketRequest;
import com.minguard.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    TicketExtendedResponse toExtendedResponse(Ticket incident, List<String> images);

    @Mapping(target = "status.id", source = "statusId")
    @Mapping(target = "urgency.id", source = "urgencyId")
    @Mapping(target = "incident.id", source = "incidentId")
    @Mapping(target = "responsible.id", source = "responsibleId")
    Ticket fromRegisterRequest(RegisterTicketRequest request);

    List<TicketResponse> toResponses(List<Ticket> ticket);

    TicketResponse toResponse(Ticket ticket);

    void fromUpdateRequest(@MappingTarget Ticket ticket, UpdateTicketRequest request);

}
