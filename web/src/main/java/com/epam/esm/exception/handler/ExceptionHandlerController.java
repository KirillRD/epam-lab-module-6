package com.epam.esm.exception.handler;

import com.epam.esm.exception.AuthorizationException;
import com.epam.esm.exception.response.Error;
import com.epam.esm.exception.response.ErrorResponse;
import com.epam.esm.exception.ControllerException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.response.ErrorUnauthorizedResponse;
import com.epam.esm.util.SecurityUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@RestControllerAdvice
public class ExceptionHandlerController {

    private final MessageSource messageSource;
    private final SecurityUtil securityUtil;

    @Autowired
    public ExceptionHandlerController(MessageSource messageSource, SecurityUtil securityUtil) {
        this.messageSource = messageSource;
        this.securityUtil = securityUtil;
    }

    // All exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generalExceptionHandler(Exception exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return new ResponseEntity<>(new ErrorResponse(
                messageSource.getMessage(Error.GENERAL.getMessage(), null, locale),
                Error.GENERAL.getCode()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorResponse>> constraintViolationExceptionHandler(ConstraintViolationException exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        List<ErrorResponse> exceptions = new ArrayList<>();
        for (ConstraintViolation<?> e : exception.getConstraintViolations()) {
            exceptions.add(new ErrorResponse(
                    messageSource.getMessage(e.getMessage(), new Object[]{e.getPropertyPath()}, locale),
                    Error.getCodeByMessage(e.getMessage())
            ));
        }
        return new ResponseEntity<>(exceptions, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // JSON invalid format of entity
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception, HttpServletRequest request) {
        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException e = (InvalidFormatException) exception.getCause();
            Locale locale = RequestContextUtils.getLocale(request);
            return new ResponseEntity<>(new ErrorResponse(
                    messageSource.getMessage(Error.PARSE_FORMAT.getMessage(), new Object[]{e.getPath().get(0).getFieldName(), e.getValue()}, locale),
                    Error.PARSE_FORMAT.getCode()
            ), HttpStatus.BAD_REQUEST);
        } else {
            return generalExceptionHandler(exception, request);
        }
    }

    // Duplicate entry
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception, HttpServletRequest request) {
        if (exception.getMostSpecificCause() instanceof SQLIntegrityConstraintViolationException) {
            SQLIntegrityConstraintViolationException e = (SQLIntegrityConstraintViolationException) exception.getMostSpecificCause();
            if (e.getErrorCode() != 1062) {
                return generalExceptionHandler(exception, request);
            }
            Locale locale = RequestContextUtils.getLocale(request);
            String[] messages = e.getMessage().split("'");
            String[] constraint = messages[3].split("\\.");
            return new ResponseEntity<>(new ErrorResponse(
                    messageSource.getMessage(
                            Error.DUPLICATE.getMessage(),
                            new Object[]{
                                    constraint[1].substring(0, constraint[1].contains("_") ? constraint[1].indexOf('_') : constraint[1].length()),
                                    messages[1],
                                    constraint[0]
                            },
                            locale),
                    Error.DUPLICATE.getCode()
            ), HttpStatus.CONFLICT);
        } else {
            return generalExceptionHandler(exception, request);
        }
    }

    // URL is not exists
    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> noHandlerFoundExceptionHandler(ServletException exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return new ResponseEntity<>(new ErrorResponse(
                messageSource.getMessage(Error.NOT_FOUND.getMessage(), new Object[]{request.getRequestURL(), request.getMethod()}, locale),
                Error.NOT_FOUND.getCode()
        ), HttpStatus.NOT_FOUND);
    }

    // Custom controller exception (invalid page format)
    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<ErrorResponse> controllerExceptionHandler(ControllerException exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return new ResponseEntity<>(new ErrorResponse(
                messageSource.getMessage(exception.getMessage(), null, locale),
                Error.getCodeByMessage(exception.getMessage())
        ), HttpStatus.BAD_REQUEST);
    }

    // Custom service exception (not found entity)
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> serviceExceptionHandler(ServiceException exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return new ResponseEntity<>(new ErrorResponse(
                messageSource.getMessage(exception.getMessage(), new Object[]{exception.getFieldName(), exception.getFieldValue()}, locale),
                Error.getCodeByMessage(exception.getMessage())
        ), HttpStatus.BAD_REQUEST);
    }

    // Not authenticated
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDeniedExceptionHandler(AccessDeniedException exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);

        if (securityUtil.isAnonymous()) {
            return new ResponseEntity<>(new ErrorUnauthorizedResponse(
                    messageSource.getMessage(Error.UNAUTHENTICATED.getMessage(), null, locale),
                    "http://127.0.0.1:8081/rest-api-web/login",
                    "http://127.0.0.1:8081/rest-api-web/oauth2/authorization/my-application-oidc",
                    "http://127.0.0.1:8081/rest-api-web/registration",
                    Error.UNAUTHENTICATED.getCode()
            ), HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(new ErrorResponse(
                    messageSource.getMessage(Error.UNAUTHORIZED.getMessage(), null, locale),
                    Error.UNAUTHORIZED.getCode()
            ), HttpStatus.FORBIDDEN);
        }
    }

    // Authentication exception
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationExceptionHandler(AuthenticationException exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return new ResponseEntity<>(new ErrorResponse(
                messageSource.getMessage(Error.LOGIN.getMessage(), null, locale),
                Error.LOGIN.getCode()
        ), HttpStatus.FORBIDDEN);
    }

    // Not authorized
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authenticationExceptionHandler(AuthorizationException exception, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return new ResponseEntity<>(new ErrorResponse(
                messageSource.getMessage(Error.UNAUTHORIZED.getMessage(), null, locale),
                Error.UNAUTHORIZED.getCode()
        ), HttpStatus.FORBIDDEN);
    }
}
