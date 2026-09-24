package br.com.fiap3ESA.ford.service;

import br.com.fiap3ESA.ford.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap3ESA.ford.dto.VehicleRequestDTO;
import br.com.fiap3ESA.ford.dto.VehicleResponseDTO;

import br.com.fiap3ESA.ford.exception.VehicleNotFoundException;

import br.com.fiap3ESA.ford.dto.VehicleComparisonItemDTO;
import br.com.fiap3ESA.ford.dto.VehicleComparisonRequestDTO;
import br.com.fiap3ESA.ford.dto.VehicleComparisonResponseDTO;

import java.util.List;

class VehicleServiceTest {

    @Mock
    private CompetitorIntegrationService integrationService;

    @Mock
    private SpecificationNormalizationService normalizationService;

    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicleService = new VehicleService(
                integrationService,
                normalizationService
        );
    }

    @Test
    void deveCadastrarVeiculoComSucesso() {

        Vehicle vehicle = Vehicle.builder()
                .id(99L)
                .marca("Ford")
                .modelo("Maverick")
                .versao("Lariat")
                .motor("2.0 Turbo")
                .potencia("253 cv")
                .torque("38,7 kgfm")
                .cambio("Automático")
                .tracao("AWD")
                .combustivel("Gasolina")
                .capacidadeCarga("Teste")
                .build();

        when(integrationService.save(any(Vehicle.class)))
                .thenAnswer(invocation -> {
                    Vehicle salvo = invocation.getArgument(0);
                    salvo.setId(4L);
                    return salvo;
                });

        Vehicle resultado = vehicleService.createVehicle(vehicle);

        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        assertEquals("Ford", resultado.getMarca());
        assertEquals("Maverick", resultado.getModelo());
        assertEquals("Lariat", resultado.getVersao());

        verify(integrationService).save(vehicle);
    }

    @Test
    void deveRetornarEspecificacoesSolicitadas() {

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .marca("Ford")
                .modelo("Ranger")
                .versao("Raptor")
                .motor("3.0 V6")
                .potencia("397 cv")
                .torque("59,4 kgfm")
                .cambio("Automático")
                .tracao("4x4")
                .combustivel("Gasolina")
                .capacidadeCarga("Teste")
                .build();

        VehicleRequestDTO request = new VehicleRequestDTO();
        request.setMarca("Ford");
        request.setModelo("Ranger");
        request.setVersao("Raptor");
        request.setAtributos(List.of("motor", "potencia"));

        when(integrationService.fetchVehicleData(
                "Ford",
                "Ranger",
                "Raptor"
        )).thenReturn(vehicle);

        when(normalizationService.normalize("3.0 V6"))
                .thenReturn("3.0 V6");

        when(normalizationService.normalize("397 cv"))
                .thenReturn("397 cv");

        VehicleResponseDTO resultado =
                vehicleService.getSpecifications(request);

        assertNotNull(resultado);

        assertEquals("Ford", resultado.getMarca());
        assertEquals("Ranger", resultado.getModelo());
        assertEquals("Raptor", resultado.getVersao());

        assertEquals(
                "3.0 V6",
                resultado.getEspecificacoes().get("motor")
        );

        assertEquals(
                "397 cv",
                resultado.getEspecificacoes().get("potencia")
        );

        assertEquals(2, resultado.getEspecificacoes().size());
    }

    @Test
    void deveLancarErroQuandoVeiculoNaoForEncontrado() {

        VehicleRequestDTO request = new VehicleRequestDTO();
        request.setMarca("Ford");
        request.setModelo("Inexistente");
        request.setVersao("Teste");
        request.setAtributos(List.of("motor"));

        when(integrationService.fetchVehicleData(
                "Ford",
                "Inexistente",
                "Teste"
        )).thenReturn(null);

        VehicleNotFoundException exception = assertThrows(
                VehicleNotFoundException.class,
                () -> vehicleService.getSpecifications(request)
        );

        assertEquals(
                "Veículo não encontrado",
                exception.getMessage()
        );

        verify(normalizationService, never())
                .normalize(any());
    }

    @Test
    void deveAtualizarVeiculoComSucesso() {

        Vehicle existente = Vehicle.builder()
                .id(1L)
                .marca("Ford")
                .modelo("Ranger")
                .versao("Raptor")
                .motor("3.0 V6")
                .potencia("397 cv")
                .torque("59,4 kgfm")
                .cambio("Automático")
                .tracao("4x4")
                .combustivel("Gasolina")
                .capacidadeCarga("Teste")
                .build();

        Vehicle atualizado = Vehicle.builder()
                .marca("Ford")
                .modelo("Ranger")
                .versao("Raptor Atualizada")
                .motor("3.0 V6")
                .potencia("400 cv")
                .torque("60 kgfm")
                .cambio("Automático")
                .tracao("4x4")
                .combustivel("Gasolina")
                .capacidadeCarga("Teste")
                .build();

        when(integrationService.findById(1L))
                .thenReturn(existente);

        when(integrationService.save(any(Vehicle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Vehicle resultado =
                vehicleService.updateVehicle(1L, atualizado);

        assertEquals(1L, resultado.getId());
        assertEquals("Raptor Atualizada", resultado.getVersao());
        assertEquals("400 cv", resultado.getPotencia());
        assertEquals("60 kgfm", resultado.getTorque());

        verify(integrationService).findById(1L);
        verify(integrationService).save(existente);
    }

    @Test
    void deveExcluirVeiculoComSucesso() {

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .marca("Ford")
                .modelo("Ranger")
                .versao("Raptor")
                .build();

        when(integrationService.findById(1L))
                .thenReturn(vehicle);

        vehicleService.deleteVehicle(1L);

        verify(integrationService).findById(1L);
        verify(integrationService).delete(vehicle);
    }

    @Test
    void deveCompararVeiculosComSucesso() {

        Vehicle ranger = Vehicle.builder()
                .marca("Ford")
                .modelo("Ranger")
                .versao("Raptor")
                .motor("3.0 V6")
                .potencia("397 cv")
                .build();

        Vehicle hilux = Vehicle.builder()
                .marca("Toyota")
                .modelo("Hilux")
                .versao("GR-Sport")
                .motor("2.8 Turbo Diesel")
                .potencia("224 cv")
                .build();

        VehicleComparisonItemDTO item1 = new VehicleComparisonItemDTO();
        item1.setMarca("Ford");
        item1.setModelo("Ranger");
        item1.setVersao("Raptor");

        VehicleComparisonItemDTO item2 = new VehicleComparisonItemDTO();
        item2.setMarca("Toyota");
        item2.setModelo("Hilux");
        item2.setVersao("GR-Sport");

        VehicleComparisonRequestDTO request =
                new VehicleComparisonRequestDTO();

        request.setVeiculos(List.of(item1, item2));
        request.setAtributos(List.of("motor", "potencia"));

        when(integrationService.fetchVehicleData(
                "Ford", "Ranger", "Raptor"
        )).thenReturn(ranger);

        when(integrationService.fetchVehicleData(
                "Toyota", "Hilux", "GR-Sport"
        )).thenReturn(hilux);

        when(normalizationService.normalize(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<VehicleComparisonResponseDTO> resultado =
                vehicleService.compareVehicles(request);

        assertEquals(2, resultado.size());

        assertEquals(
                "Ford Ranger Raptor",
                resultado.get(0).getVeiculo()
        );

        assertEquals(
                "Toyota Hilux GR-Sport",
                resultado.get(1).getVeiculo()
        );

        assertEquals(
                "3.0 V6",
                resultado.get(0).getEspecificacoes().get("motor")
        );

        assertEquals(
                "224 cv",
                resultado.get(1).getEspecificacoes().get("potencia")
        );
    }
}