package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.productionservice.api.ProductionBatchCreateRequestDto;
import cz.muni.fi.pa165.productionservice.api.ProductionBatchDto;
import cz.muni.fi.pa165.productionservice.data.enums.BatchStatus;
import cz.muni.fi.pa165.productionservice.facade.ProductionBatchFacade;
import cz.muni.fi.pa165.productionservice.util.TestDataFactory;
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
    void createBatch_validRequest_returnsCreated() {
        ProductionBatchCreateRequestDto request = TestDataFactory.productionBatchCreateRequestDto();

        ProductionBatchDto dto = TestDataFactory.productionBatchDto();

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
    void getBatches_noInput_returnsOk() {
        List<ProductionBatchDto> list = TestDataFactory.productionBatchDtos();

        Mockito.when(productionBatchFacade.findAllBatches()).thenReturn(list);

        ResponseEntity<List<ProductionBatchDto>> response =
            productionBatchController.getBatches();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(list);
    }

    @Test
    void getBatch_existingId_returnsOk() {
        UUID id = TestDataFactory.BATCH_ID;
        ProductionBatchDto dto = TestDataFactory.productionBatchDto();

        Mockito.when(productionBatchFacade.findBatch(id)).thenReturn(dto);

        ResponseEntity<ProductionBatchDto> response =
            productionBatchController.getBatch(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void deleteBatch_existingId_returnsNoContent() {
        UUID id = TestDataFactory.BATCH_ID;

        ResponseEntity<Void> response =
            productionBatchController.deleteBatch(id);

        Mockito.verify(productionBatchFacade).deleteBatch(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void updateBatchStatus_validStatus_returnsOk() {
        UUID id = TestDataFactory.BATCH_ID;
        ProductionBatchDto dto = TestDataFactory.productionBatchDto();

        Mockito.when(productionBatchFacade.updateBatchStatus(id, BatchStatus.PLANNED))
            .thenReturn(dto);

        ResponseEntity<ProductionBatchDto> response =
            productionBatchController.updateBatchStatus(id, BatchStatus.PLANNED);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }
}
