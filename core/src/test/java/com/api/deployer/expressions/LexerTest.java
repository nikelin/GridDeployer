package com.api.deployer.expressions;

import com.api.deployer.AbstractTest;
import com.redshape.ascript.EvaluationException;
import com.redshape.ascript.IEvaluator;
import com.redshape.ascript.context.IEvaluationContext;
import com.redshape.ascript.evaluation.ExpressionEvaluator;
import com.redshape.ascript.language.ast.FunctionTreeNode;
import com.redshape.ascript.language.ast.ISyntaxTree;
import com.redshape.ascript.language.impl.Lexer;
import com.redshape.ascript.language.impl.LexerException;
import com.redshape.ascript.language.impl.Tokenizer;
import com.redshape.utils.Function;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class LexerTest extends AbstractTest<LexerTest.Attribute> {
	public enum Attribute {
		EXPRESSION
	}
	
	private static final Logger log = Logger.getLogger( LexerTest.class );
	
	public LexerTest() {
		super();
		
		this.setAttribute( LexerTest.Attribute.EXPRESSION, "(afla.x (afla.flag #1) (afla.flag 2) (afla.xdo (:list x a) (substr #x 0 4) (afla.a 25 25 33) afla.d) );");
	}
	
	@Test
	public void testMain() throws LexerException {
		String expression = this.getAttribute( LexerTest.Attribute.EXPRESSION );
		Lexer lexer = new Lexer(expression, new Tokenizer() );
		
		ISyntaxTree tree = lexer.process();
		log.info( tree );
		assertTrue( tree.getRoot() instanceof FunctionTreeNode);
	}
	
	@Test
	public void testEvaluator() throws EvaluationException {
		IEvaluator evaluator = new ExpressionEvaluator();
		
		IEvaluationContext context = evaluator.getRootContext();
		context.exportBean( "afla", Mock.class, new Mock() );
		context.exportValue("1", 23);
		context.exportFunction("substr", new Function<Object, Object>() {
			@Override
			public Object invoke( Object... args ) {
				if ( args.length == 0 ) {
					return null;
				}
				
				if ( args.length < 3 ) {
					throw new IllegalArgumentException("Wrong arguments count");
				}
				
				String arg = (String) args[0];
				Integer from = Integer.valueOf( (String) args[1] );
				Integer length = Integer.valueOf( (String) args[2] );
				
				log.info("String: " + arg );
				log.info("Substring result: " + arg.substring(from, length) );
				
				return arg.substring(from, length);
			}
		});
		
		context.exportValue("x", "Test value");
		
		Map<String, Object> value = evaluator.evaluate( this.<String>getAttribute( LexerTest.Attribute.EXPRESSION ) );
		assertNotNull( value );
		assertTrue( value.containsKey("flag1") );
		assertTrue( value.containsKey("flag2") );
		assertTrue( value.containsKey("xdo") );
		assertTrue( value.get("xdo") instanceof Map );
		
		Map<String, Object> xdo = (Map<String, Object>) value.get("xdo");
		assertTrue( xdo.containsKey("substr") );
		assertTrue( xdo.containsKey("a") );
		assertTrue( xdo.containsKey("d") );
		assertNotNull( xdo.get("substr") );
		assertEquals( 4, ( (String) xdo.get("substr") ).length() );
	}
	
	public class Mock {
		public Map<String, Object> flag( Object x ) {
			log.info("Flag method in action!");
			log.info("flag: " + x );
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("x", x);
			return map;
		}
		
		public Map<String, Object> xdo( Object x, Object y, Object c ) {
			log.info("xdo handler: x:" + x + ":y:" + y + ":c:" + c);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("substr", x);
			map.put("a", y);
			map.put("d", c);
			
			return map;
		}
		
		public Map<String, Object> a( Object x, Object y, Object c ) {
			log.info("a handler: x:" + x + ":y:" + y + ":c:" + c);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("x", x);
			map.put("y", y);
			map.put("c", c);
			
			return map;
		}
		
		public Map<String, Object> x( Object x, Object y, Object z ) {
			log.info("x handler:x:" + x + ":y:" + y + ":z:" + z);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag1", x);
			map.put("flag2", y);
			map.put("xdo", z);
			
			return map;
		}
	}
	
}
