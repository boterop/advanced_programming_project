package com.project.advanced.project.aplication.usercase;



public class ConfirmarReservaUseCase {

    private final ReservaRepository reservaRepository;

    public ConfirmarReservaUseCase(ReservRepository reservaRepository){
        this.reservaRepository = reservaRepository;
    }

    public void ejecutar(CodigoReserva codigo){
        Reserva reserva = reservaRepository.obtenerPorCodigo(codigo)
                .orElseThrow(()-> new ReservaNoEncontradaException(codigo));

        reserva.confirmar();
        reservaRepository.guardar(reserva);
    }


}
