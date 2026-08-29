package com.expedicao.controle.exception;

/** Violacao de regra de negocio: transicao de status invalida, SKU duplicado, etc. */
public class OperacaoInvalidaException extends RuntimeException {

    public OperacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
