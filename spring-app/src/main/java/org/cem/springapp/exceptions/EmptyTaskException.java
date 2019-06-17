package org.cem.springapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Task field is empty")
public class EmptyTaskException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}