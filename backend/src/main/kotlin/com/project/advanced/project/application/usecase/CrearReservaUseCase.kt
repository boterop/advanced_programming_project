package com.project.advanced.project.application.usecase

class CrearReservaUseCase(
    private val reservaRepository: ReservaRepository,
) {
    fun ejecutar(
        id: ID = ID(UUID.randomUUID().toString()),
        codigo: CodigoReserva = CodigoReserva(UUID.randomUUID().toString()),
        apartamento: Apartamento,
        estancia: Estancia,
        estado: EstadoReserva,
        canalOrigen: CanalOrigen,
        idExterno: IdExterno,
        titular: Ocupante,
        ocupantes: List<Ocupante>,
        horaLlegada: HoraLlegada,
        valor: Dinero,
        politica: PoliticaCancelacion,
        motivoCancelacion: String,
    ) {}
}
