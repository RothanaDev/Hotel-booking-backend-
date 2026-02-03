package com.Rothana.hotel_booking_system.features.Inventory;

import com.Rothana.hotel_booking_system.entity.Inventory;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryCreateRequest;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryResponse;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryUpdateRequest;
import com.Rothana.hotel_booking_system.mapper.InventoryMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventorRepository inventorRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryResponse create(InventoryCreateRequest request) {
        Inventory inventory = inventoryMapper.fromInventoryCreateRequest(request);
        return inventoryMapper.toInventoryResponse(inventorRepository.save(inventory));
    }

    @Override
    public InventoryResponse update(Integer id, InventoryUpdateRequest request) {
        Inventory inventory = inventorRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Inventory not found"));

        if (request.itemName() != null) inventory.setItemName(request.itemName());
        if (request.quantity() != null) inventory.setQuantity(request.quantity());
        if (request.unit() != null) inventory.setUnit(request.unit());

        return inventoryMapper.toInventoryResponse(inventorRepository.save(inventory));
    }

    @Override
    public InventoryResponse findById(Integer id) {
        Inventory inventory = inventorRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Inventory not found"));
        return inventoryMapper.toInventoryResponse(inventory);
    }

    @Override
    public List<InventoryResponse> findAll() {
        List<Inventory> inventors = inventorRepository.findAll();
        return inventoryMapper.toInventoryResponseList(inventors);
    }

    @Override
    public void delete(Integer id) {
    Inventory inventory = inventorRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Inventory not found"));
    inventorRepository.delete(inventory);
    }
}
