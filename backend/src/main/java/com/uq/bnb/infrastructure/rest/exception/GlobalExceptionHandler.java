package com.uq.bnb.infrastructure.rest.exception;
import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.BloqueoNoEncontradoException;
import com.uq.bnb.application.exception.FolioNoEncontradoException;
import com.uq.bnb.application.exception.PoliticaCancelacionNoEncontradaException;
import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.application.exception.TarifaNoEncontradaException;
import com.uq.bnb.application.exception.TemporadaNoEncontradaException;
import com.uq.bnb.domain.exception.ReglaDominioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.List;
import java.util.stream.Stream;
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(BindException ex) {
        Stream<String> deCampos = ex.getBindingResult().getFieldErrors().stream().map(GlobalExceptionHandler::describir);
        Stream<String> globales = ex.getBindingResult().getGlobalErrors().stream().map(error -> error.getDefaultMessage());
        List<String> detalles = Stream.concat(deCampos, globales).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDACION", "La solicitud tiene campos inválidos", detalles));
    }
    private static String describir(FieldError error) {
        if (error.isBindingFailure()) {
            return error.getField() + ": el valor '" + error.getRejectedValue() + "' no tiene el formato esperado";
        }
        return error.getField() + ": " + error.getDefaultMessage();
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> manejarValidacionDeParametros(HandlerMethodValidationException ex) {
        List<String> detalles = ex.getAllErrors().stream().map(error -> error.getDefaultMessage()).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDACION", "La solicitud tiene parámetros inválidos", detalles));
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> manejarParametroFaltante(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.de("SOLICITUD_INCOMPLETA", "Falta el parámetro obligatorio '" + ex.getParameterName() + "'"));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarMensajeIlegible(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.de("SOLICITUD_MALFORMADA", "El cuerpo de la solicitud no es un JSON válido o tiene valores no permitidos"));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarParametroConFormatoInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.de("SOLICITUD_MALFORMADA", "El valor del parámetro '" + ex.getName() + "' no tiene el formato esperado"));
    }
    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> manejarDatoInvalido(RuntimeException ex) {
        String mensaje = ex.getMessage() == null ? "La solicitud tiene un dato obligatorio vacío o inválido" : ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.de("SOLICITUD_INVALIDA", mensaje));
    }
    @ExceptionHandler({
            AlojamientoNoEncontradoException.class,
            TemporadaNoEncontradaException.class,
            ApartamentoNoEncontradoException.class,
            ReservaNoEncontradaException.class,
            FolioNoEncontradoException.class,
            PoliticaCancelacionNoEncontradaException.class,
            BloqueoNoEncontradoException.class,
            TarifaNoEncontradaException.class
    })
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.de("NO_ENCONTRADO", ex.getMessage()));
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRutaInexistente(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.de("NO_ENCONTRADO", "No existe el recurso /" + ex.getResourcePath()));
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponse.de("METODO_NO_PERMITIDO", "El recurso no admite el método " + ex.getMethod()));
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> manejarTipoDeContenido(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ErrorResponse.de("TIPO_NO_SOPORTADO", "El cuerpo de la solicitud debe enviarse como application/json"));
    }
    @ExceptionHandler(ReglaDominioException.class)
    public ResponseEntity<ErrorResponse> manejarReglaDominio(ReglaDominioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.de("REGLA_DOMINIO", ex.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(Exception ex) {
        LOG.error("Error no controlado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.de("ERROR_INTERNO", "Error interno del servidor"));
    }
}
