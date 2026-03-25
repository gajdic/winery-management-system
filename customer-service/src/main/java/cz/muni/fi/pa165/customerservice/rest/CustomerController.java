package cz.muni.fi.pa165.customerservice.rest;

import cz.muni.fi.pa165.customerservice.api.CustomerCreateDto;
import cz.muni.fi.pa165.customerservice.api.CustomerDto;
import cz.muni.fi.pa165.customerservice.api.CustomerUpdateDto;
import cz.muni.fi.pa165.customerservice.facade.CustomerFacade;
import jakarta.validation.Valid;
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
    public CustomerDto createCustomer(@Valid @RequestBody CustomerCreateDto dto) {
        return customerFacade.createCustomer(dto);
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable UUID id) {
        return customerFacade.getCustomer(id);
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerFacade.getAllCustomers();
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable UUID id,
                                      @Valid @RequestBody CustomerUpdateDto dto) {
        return customerFacade.updateCustomer(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable UUID id) {
        customerFacade.deleteCustomer(id);
    }
}
