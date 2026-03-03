package com.ifsc.ctds.stinghen.recycle_it_api.controllers;

import com.ifsc.ctds.stinghen.recycle_it_api.dtos.response.FeedbackResponseDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.BadValueException;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.DeniedRequestException;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.InvalidRelationshipException;
import com.ifsc.ctds.stinghen.recycle_it_api.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<FeedbackResponseDTO> handleNotFoundException(NotFoundException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Recurso não encontrado")
                .content("O recurso solicitado não foi encontrado no sistema")
                .isError(true)
                .isAlert(false)
                .additionalInfo(exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadValueException.class)
    public ResponseEntity<FeedbackResponseDTO> handleBadValueException(BadValueException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Valor inválido")
                .content("O valor informado é inválido para esta operação")
                .isError(true)
                .isAlert(false)
                .additionalInfo(exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DeniedRequestException.class)
    public ResponseEntity<FeedbackResponseDTO> handleDeniedRequestException(DeniedRequestException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Requisição negada")
                .content("Sua requisição foi negada pelo sistema")
                .isError(true)
                .isAlert(false)
                .additionalInfo(exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidRelationshipException.class)
    public ResponseEntity<FeedbackResponseDTO> handleInvalidRelationshipException(InvalidRelationshipException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Relacionamento inválido")
                .content("A operação não pode ser concluída devido a um relacionamento inválido")
                .isError(true)
                .isAlert(false)
                .additionalInfo(exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FeedbackResponseDTO> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> 
            errors.add(error.getField() + ": " + error.getDefaultMessage())
        );
        exception.getBindingResult().getGlobalErrors().forEach(error -> 
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage())
        );
        
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Erro de validação")
                .content("Existem erros de validação nos dados enviados")
                .isError(true)
                .isAlert(false)
                .additionalInfo(String.join(", ", errors))
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<FeedbackResponseDTO> handleTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String error = String.format("Parameter '%s' should be of type %s", 
                exception.getName(), 
                exception.getRequiredType() != null ? exception.getRequiredType().getSimpleName() : "unknown");
        
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Tipo de parâmetro inválido")
                .content("O tipo do parâmetro informado não é compatível com o esperado")
                .isError(true)
                .isAlert(false)
                .additionalInfo(error)
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<FeedbackResponseDTO> handleMissingParameterException(MissingServletRequestParameterException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Parâmetro obrigatório ausente")
                .content("Um parâmetro obrigatório não foi informado na requisição")
                .isError(true)
                .isAlert(false)
                .additionalInfo("Parameter '" + exception.getParameterName() + "' is missing")
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<FeedbackResponseDTO> handleMessageNotReadableException(HttpMessageNotReadableException exception) {
        String error = "Malformed JSON request";
        if (exception.getCause() != null) {
            error = exception.getCause().getMessage();
        }
        
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("JSON malformado")
                .content("O corpo da requisição contém um JSON inválido")
                .isError(true)
                .isAlert(false)
                .additionalInfo(error)
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<FeedbackResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        String error = String.format("No handler found for %s %s", 
                exception.getHttpMethod(), 
                exception.getRequestURL());
        
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Endpoint não encontrado")
                .content("O endpoint solicitado não existe na API")
                .isError(true)
                .isAlert(false)
                .additionalInfo(error)
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<FeedbackResponseDTO> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        String error = String.format("Method %s is not supported for this endpoint. Supported methods: %s", 
                exception.getMethod(), 
                Arrays.toString(exception.getSupportedMethods()));
        
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Método HTTP não suportado")
                .content("O método HTTP utilizado não é suportado por este endpoint")
                .isError(true)
                .isAlert(false)
                .additionalInfo(error)
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<FeedbackResponseDTO> handleAccessDeniedException(AccessDeniedException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Acesso negado")
                .content("Você não tem permissão para acessar este recurso")
                .isError(true)
                .isAlert(false)
                .additionalInfo("Access denied: " + exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<FeedbackResponseDTO> handleAuthenticationException(AuthenticationException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Falha na autenticação")
                .content("Não foi possível autenticar sua identidade")
                .isError(true)
                .isAlert(false)
                .additionalInfo("Authentication failed: " + exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<FeedbackResponseDTO> handleBadCredentialsException(BadCredentialsException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Credenciais inválidas")
                .content("As credenciais fornecidas estão incorretas")
                .isError(true)
                .isAlert(false)
                .additionalInfo("Invalid credentials")
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FeedbackResponseDTO> handleIllegalArgumentException(IllegalArgumentException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Argumento ilegal")
                .content("Um argumento inválido foi fornecido para a operação")
                .isError(true)
                .isAlert(false)
                .additionalInfo(exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<FeedbackResponseDTO> handleIllegalStateException(IllegalStateException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Estado ilegal")
                .content("A operação não pode ser executada no estado atual do sistema")
                .isError(true)
                .isAlert(false)
                .additionalInfo(exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
    public ResponseEntity<FeedbackResponseDTO> handleNullPointerException(NullPointerException exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Erro interno do servidor")
                .content("Ocorreu um erro interno inesperado no servidor")
                .isError(true)
                .isAlert(false)
                .additionalInfo("Internal server error: Null pointer exception")
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<FeedbackResponseDTO> handleGenericException(Exception exception) {
        FeedbackResponseDTO responseDTO = FeedbackResponseDTO.builder()
                .mainMessage("Erro não esperado")
                .content("Ocorreu um erro não esperado pelo servidor")
                .isError(true)
                .isAlert(false)
                .additionalInfo(exception.getMessage())
                .build();
        
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
