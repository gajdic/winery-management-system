package cz.muni.fi.pa165.productionservice.controller;

import cz.muni.fi.pa165.productionservice.api.ProductionPlanDto;
import cz.muni.fi.pa165.productionservice.api.ProductionPlanRequestDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationDto;
import cz.muni.fi.pa165.productionservice.api.QualityEvaluationRequestDto;
import cz.muni.fi.pa165.productionservice.facade.ProductionPlanningFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductionPlanningControllerTest {

    @Mock
    private ProductionPlanningFacade productionPlanningFacade;

    @InjectMocks
    private ProductionPlanningController productionPlanningController;

    @Test
    void planProduction_validRequest_returnsOk() {
        ProductionPlanRequestDto request = new ProductionPlanRequestDto();
        ProductionPlanDto responseDto = new ProductionPlanDto();

        Mockito.when(productionPlanningFacade.planProduction(request))
            .thenReturn(responseDto);

        ResponseEntity<ProductionPlanDto> response =
            productionPlanningController.planProduction(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }

    @Test
    void evaluateQuality_validRequest_returnsOk() {
        QualityEvaluationRequestDto request = new QualityEvaluationRequestDto();
        QualityEvaluationDto responseDto = new QualityEvaluationDto();

        Mockito.when(productionPlanningFacade.evaluateQuality(request))
            .thenReturn(responseDto);

        ResponseEntity<QualityEvaluationDto> response =
            productionPlanningController.evaluateQuality(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDto);
    }
}
