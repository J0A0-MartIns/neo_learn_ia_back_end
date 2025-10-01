package br.ueg.progwebi.collegeapi.service.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private int codeError = 400;
    public BusinessException(String message, Throwable e, int codeError){
        super(message, e);
        this.codeError = codeError;
    }
    public BusinessException(String message, Throwable e){
        super(message, e);
    }
    public BusinessException(String message, int codeError){
        super(message);
        this.codeError = codeError;
    }
    public BusinessException(String message){
        super(message);
    }

}
