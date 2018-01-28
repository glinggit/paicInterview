package com.paic.arch.exception;

public class NoMessageReceivedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoMessageReceivedException(String reason) {
        super(reason);
    }
}
