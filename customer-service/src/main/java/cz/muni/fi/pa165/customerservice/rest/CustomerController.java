package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.CustomerCreateDto;
import cz.muni.fi.pa165.customerservice.api.CustomerDto;
import cz.muni.fi.pa165.customerservice.api.CustomerUpdateDto;
import cz.muni.fi.pa165.customerservice.facade.CustomerFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerFacade customerFacade;

    public CustomerController(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerCreateDto dto) {
        CustomerDto created = customerFacade.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable UUID id) {
        CustomerDto customer = customerFacade.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerFacade.getAllCustomers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
        @PathVariable UUID id,
        @Valid @RequestBody CustomerUpdateDto dto) {

        CustomerDto updated = customerFacade.updateCustomer(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerFacade.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
