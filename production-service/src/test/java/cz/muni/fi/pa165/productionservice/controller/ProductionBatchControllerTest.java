package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateRequestDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.facade.ProductionBatchFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductionBatchControllerTest {

    @Mock
    private ProductionBatchFacade productionBatchFacade;

    @InjectMocks
    private ProductionBatchController productionBatchController;

    @Test
    void createBatch_returnsCreated() {
        ProductionBatchCreateRequestDto request = new ProductionBatchCreateRequestDto();
        request.setBatch(new ProductionBatchCreateDto());
        request.setHarvestLots(List.of());

        ProductionBatchDto dto = new ProductionBatchDto();

        Mockito.when(productionBatchFacade.createBatch(
            request.getBatch(),
            request.getHarvestLots()
        )).thenReturn(dto);

        ResponseEntity<ProductionBatchDto> response =
            productionBatchController.createBatch(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getBatches_returnsOk() {
        List<ProductionBatchDto> list = List.of(
            new ProductionBatchDto(),
            new ProductionBatchDto()
        );

        Mockito.when(productionBatchFacade.findAllBatches()).thenReturn(list);

        ResponseEntity<List<ProductionBatchDto>> response =
            productionBatchController.getBatches();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(list);
    }

    @Test
    void getBatch_returnsOk() {
        UUID id = UUID.randomUUID();
        ProductionBatchDto dto = new ProductionBatchDto();

        Mockito.when(productionBatchFacade.findBatch(id)).thenReturn(dto);

        ResponseEntity<ProductionBatchDto> response =
            productionBatchController.getBatch(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void deleteBatch_returnsNoContent() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> response =
            productionBatchController.deleteBatch(id);

        Mockito.verify(productionBatchFacade).deleteBatch(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void updateBatchStatus_returnsOk() {
        UUID id = UUID.randomUUID();
        ProductionBatchDto dto = new ProductionBatchDto();

        Mockito.when(productionBatchFacade.updateBatchStatus(id, BatchStatus.PLANNED))
            .thenReturn(dto);

        ResponseEntity<ProductionBatchDto> response =
            productionBatchController.updateBatchStatus(id, BatchStatus.PLANNED);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }
}
