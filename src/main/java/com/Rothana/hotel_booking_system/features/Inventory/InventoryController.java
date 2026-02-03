package com.Rothana.hotel_booking_system.features.Inventory;

import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryCreateRequest;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryResponse;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@AllArgsConstructor
public class InventoryController {
    private  final  InventoryService inventoryService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public InventoryResponse create(@Valid @RequestBody InventoryCreateRequest request){
        return inventoryService.create(request);
    }
    @PutMapping("/update/{id}")
    public InventoryResponse update(@PathVariable("id") Integer id, @Valid @RequestBody InventoryUpdateRequest request){
        return inventoryService.update(id,request);
    }
    @GetMapping("/findAll")
    public List<InventoryResponse> findAll(){
        return inventoryService.findAll();
    }
    @GetMapping("/findById/{id}")
    public InventoryResponse findById(@PathVariable("id") Integer id){
        return inventoryService.findById(id);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Integer id){
        inventoryService.delete(id);
    }
}
