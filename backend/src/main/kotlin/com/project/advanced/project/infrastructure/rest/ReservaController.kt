package com.project.advanced.project.infrastructure.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reservas")
class ReservaController {
    private val cancelarReservaUseCase():CancelarReservaUseCase
    private val consultarReservaUseCase(): ConsultarReservaUseCase
    private val crearReservaUseCase(): CrearReservaUseCase
    private val confirmarReservaUseCase(): ConfirmarReservaUseCase
    private val mapper: ReservaMapper

    @GetMapping
    fun listarReservas(@RequestParam estado: EstadoReserva): ResponseEntity<List<ReservaResumenResponse>> {
      List<Reserva> reservas = consultarReservasUseCase.ejecutar(estado)

      if(reservas.isEmpty()) return ResponseEntity.notFound().build()

      return ResponseEntity.ok(mapper.mapList(reservas))
    }

    @GetMapping("/{codigo}")
    fun obtenerReserva(@PathVariable codigo: String): ResponseEntity<ReservaDetalleResponse> {
      Reserva reserva = consultarReservaUseCase.ejecutar(CodigoReserva(codigo))

      if(reserva == null) return ResponseEntity.notFound().build()

      return ResponseEntity.ok(mapper.map(reserva))
    }

    @PostMapping
    fun crearReserva(@Valid @RequestBody body: CrearReservaRequest): ResponseEntity<ReservaDetalleResponse> {
          val (
        idApartamento,
        fechaEntrada,
        fechaSalida,
        canalOrigen,
        titular,
        ocupantes,
    ) = body
      Reserva reserva = crearReservaUseCase.ejecutar(
        apartamento = Apartamento(buscarApartamentoUseCase(idApartamento)),
        estancia = Estancia(fechaEntrada, fechaSalida),
        estado = EstadoReserva.PENDIENTE,
        canalOrigen = canalOrigen,
        idExterno = IdExterno(UUID.randomUUID().toString()),
        titular = titular,
        ocupantes = ocupantes,
        horaLlegada = HoraLlegada(LocalTime.now()),
        valor = Dinero(0),
        politica = PoliticaCancelacion.NO_HACER_CANCERACION,
        motivoCancelacion = "",
      )

      URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}").buildAndExpand(reserva.codigo.valor()).toUri()


      return ResponseEntity.created(location).body(mapper.map(reserva))
    }

    @PutMapping("/{codigo}/cancelar")
    fun cancelarReserva(@PathVariable codigo: String, @Valid @RequestBody body: CancelarReservaRequest): ResponseEntity<ReservaDetalleResponse> {
      Reserva reserva = cancelarReservaUseCase.ejecutar(CodigoReserva(codigo), body.motivo, LocalDate.now())

      return ResponseEntity.ok(mapper.map(reserva))
    }

    @PutMapping("/{codigo}/confirmar")
    fun confirmarReserva(@PathVariable codigo: String, @Valid @RequestBody body: ConfirmarReservaRequest): ResponseEntity<ReservaDetalleResponse> {
      Reserva reserva = confirmarReservaUseCase.ejecutar(CodigoReserva(codigo))

      return ResponseEntity.ok(mapper.map(reserva))
    }
}
