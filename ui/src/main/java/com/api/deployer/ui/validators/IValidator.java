package com.api.deployer.ui.validators;

public interface IValidator<T> {

	public boolean isValid( T value );
	
}
