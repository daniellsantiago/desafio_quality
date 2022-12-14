package com.grupo2.desafioquality.service;

import com.grupo2.desafioquality.dto.CreatePropertyDto;
import com.grupo2.desafioquality.dto.PropertyDto;
import com.grupo2.desafioquality.dto.RoomDto;
import com.grupo2.desafioquality.exception.NotFoundException;
import com.grupo2.desafioquality.model.District;
import com.grupo2.desafioquality.model.Property;
import com.grupo2.desafioquality.model.Room;
import com.grupo2.desafioquality.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/** Classe de serviço da propriedade.
 * @since Release 01 da aplicação.
 */
@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;

    private final DistrictService districtService;

    /** Cria uma propriedade no repositório
     * @param CreatePropertyDto createPropertyDto - DTO de Property
     * @return Property - retorna a propriedade criada
     */
    public Property createProperty(CreatePropertyDto createPropertyDto) {
        District district = districtService.findById(createPropertyDto.getDistrictId())
                .orElseThrow(() -> new NotFoundException("District não encontrado"));

        List<Room> rooms = createPropertyDto.getRooms().stream()
                .map(room -> new Room(room.getName(), room.getLength(), room.getWidth()))
                .collect(Collectors.toList());

        Property property = new Property(UUID.randomUUID(), createPropertyDto.getName(), district, rooms);
        propertyRepository.saveProperty(property);
        return property;
    }

    /** Retorna a área da propriedade com o id passado no parâmetro
     * @param UUID propertyId - ID da propriedade
     * @return PropertyDto - informações da propriedade
     */
    public PropertyDto getPropertyArea(UUID propertyId) {
        Property property = propertyRepository.findPropertyById(propertyId)
                .orElseThrow(() -> new NotFoundException("Property não encontrada"));

        District district = property.getDistrict();

        double area = calculatePropertyArea(property);

        BigDecimal price = calculatePropertyPrice(property, area);

        List<Room> rooms = property.getRooms();
        String largestRoom = calculateLargestRoom(rooms).getName();

        List<RoomDto> roomDtos = rooms.stream()
                .map(room -> RoomDto.fromRoom(room, calculateRoomArea(room)))
                .collect(Collectors.toList());

        return new PropertyDto(
                propertyId,
                district.getName(),
                area,
                price,
                largestRoom,
                roomDtos
        );
    }

    private Room calculateLargestRoom(List<Room> rooms) {
        return rooms.stream()
                .max(Comparator.comparingDouble(this::calculateRoomArea)).orElseThrow();
    }

    private double calculatePropertyArea(Property property) {
        return property.getRooms()
                .stream()
                .map(this::calculateRoomArea)
                .reduce(0D, Double::sum);
    }

    private BigDecimal calculatePropertyPrice(Property property, double area) {
        return property.getDistrict().getSquareMeterValue().multiply(BigDecimal.valueOf(area));
    }

    private double calculateRoomArea(Room room) {
        return room.getLength() * room.getWidth();
    }
}
