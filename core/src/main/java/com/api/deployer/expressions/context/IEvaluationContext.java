package com.api.deployer.expressions.context;

import java.util.Map;

import com.api.commons.IFunction;
import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.context.items.FunctionItem;

public interface IEvaluationContext {

	/**
	 * Get registered objects count
	 * @return
	 */
	public Integer getObjectsCount();

	/**
	 * Remove exported object from context
	 */
	public void unexportValue( String name );


	/**
	 * Remove all exported objects and aliases
	 */
	public void reset();

	/**
	 * Export simple variable to current context
	 * 
	 * @usage
	 * ${currentContext}.${name}
	 * 
	 * @param name
	 * @param value
	 */
	public void exportValue( String name, Object value ) throws EvaluationException ;
	
	/**
	 * Export @see values with subcontext @see name creation
	 * 
	 * @usage
	 * ${currentContext}.${key from values}
	 * 
	 * @param name
	 * @param values
	 */
	public void exportMap( String name, Map<String, ?> values ) throws EvaluationException ;
	
	/**
	 * Export object with subcontext creation
	 * 
	 * @usage
	 * ${currentContext}.${name}.${field from description}
	 * 
	 * @param name
	 * @param description
	 * @param value
	 */
	public void exportBean( String name, Class<?> description, Object value ) throws EvaluationException ;
	
	public void exportFunction( String name, IFunction<?,?> fn ) throws EvaluationException ;

	public void exportFunctionAlias( String originalName, String alias ) throws EvaluationException;

	public void exportClass( String name, Class<?> clazz ) throws EvaluationException;

	public void exportContext( String name, IEvaluationContext context ) throws EvaluationException ;
	
	/**
	 * Get item registered in current context
	 * 
	 * @param name
	 * @return
	 */
	public IEvaluationContextItem get( String name );
	
	/**
	 * Get function from context by name
	 */
	public <V, T> IFunction<V, T> resolveFunction( String name, int argumentsCount )
			throws EvaluationException;

	public <V, T> IFunction<V, T> resolveFunction( String name, int argumentsCount, Class<?>[] types )
			throws EvaluationException;

	/**
	 * Resolve path to context child or subcontext element
	 */
	public <V> V resolve( String name ) throws EvaluationException;

    /**
        * List all declared functions
        * @return
        */
    public Map<String, FunctionItem> listFunctions()  throws EvaluationException;

    public Map<String, FunctionItem> listFunctions( IEvaluationContext context ) throws EvaluationException;

    public Map<String, IEvaluationContextItem> getItems();

}
