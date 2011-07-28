package com.api.deployer.expressions.evaluation;

import com.api.commons.Function;
import com.api.commons.IFunction;
import com.api.commons.ReflectionUtils;
import com.api.deployer.expressions.EvaluationException;
import com.api.deployer.expressions.IEvaluator;
import com.api.deployer.expressions.context.IEvaluationContext;
import com.api.deployer.expressions.language.DeclarationType;
import com.api.deployer.expressions.language.ast.*;
import com.api.deployer.expressions.visitors.ISyntaxTreeVisitor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author nikelin
 * @date 21/04/11
 * @package com.api.deployer.expressions.evaluation
 */
public class SyntaxTreeProcessor implements ISyntaxTreeVisitor {
	private static final Logger log = Logger.getLogger(SyntaxTreeProcessor.class);

	private IEvaluationContext context;
	private IEvaluator evaluatorContext;

	public SyntaxTreeProcessor( IEvaluationContext context ) {
		this.context = context;
	}

	@Override
	public void setEvaluatorContext( IEvaluator evaluator ) {
		this.evaluatorContext = evaluator;
	}

	protected IEvaluator getEvaluatorContext() {
		return this.evaluatorContext;
	}

	protected IEvaluationContext getContext() {
		return this.context;
	}

	@Override
	public <T> T visit(ISyntaxTree tree) throws EvaluationException {
		return this.<T>visit( tree.getRoot() );
	}

	@SuppressWarnings("unchecked")
	protected <V> V visit( ISyntaxTreeNode node ) throws EvaluationException {
		try {
			return (V) this.getClass().getMethod("visit", node.getClass() ).invoke(this, node);
		} catch ( InvocationTargetException e ) {
			log.error( e.getTargetException().getMessage(), e.getTargetException() );
			throw new EvaluationException( e.getTargetException().getMessage(), e.getTargetException() );
		} catch ( Throwable e ) {
			throw new EvaluationException( e.getMessage(), e );
		}
	}

	@Override
	public <T> T visit( DeclarationTreeNode node )  throws EvaluationException {
		T result;
		switch ( node.getType() ) {
			case LIST:
				result = (T) this.visitListDecl(node);
			break;
			case INCLUDE:
				result = (T) this.visitIncludeDecl(node);
			break;
			case LAMBDA:
				result = (T) this.visitLambdaDecl(node);
			break;
			case EVAL:
				result = (T) this.visitEvalDecl( node );
			break;
			case MAP:
				result = (T) this.visitMapDecl(node);
			break;
			case IMPORT:
				result = (T) this.visitImportDecl(node);
			break;
            case WHEN:
                result = (T) this.visitWhenDecl( node );
			case NEW:
				result = (T) this.visitNewDecl(node);
			break;
			case DEFINE:
				result = (T) this.visitDefineDecl(node);
			break;
            case IF:
                result = (T) this.visitIfDecl(node);
			case SET:
				result = (T) this.visitSetDecl(node);
			break;
			default:
				throw new EvaluationException("Unsupported declaration type");
		}

		return result;
	}

    public Object visitWhenDecl( DeclarationTreeNode node ) throws EvaluationException {
        if ( node.getArguments().size() < 2 ) {
            throw new EvaluationException("Wrong (:when [z...zN] y) declaration arguments count!");
        }

        ISyntaxTreeNode defaultChoice = node.getArguments().get( node.getArguments().size() - 1 );

        for ( int choiceNum = 0; choiceNum < node.getArguments().size() - 1; choiceNum++ ) {
            ISyntaxTreeNode choiceNode = node.getArguments().get(choiceNum);
            if ( !choiceNode.isList() ) {
                throw new EvaluationException("Wrong (when) choice format! Must be (:list cond action)");
            }

            List<Object> args = this.visitListDecl( (DeclarationTreeNode) choiceNode );
            if ( !( args.get(0) instanceof Boolean ) ) {
                throw new EvaluationException("(when) choice first argument must be boolean value type!");
            }

            if ( (Boolean) args.get(0) ) {
                return args.get(1);
            }
        }

        return this.visit( defaultChoice );
    }

    public Object visitIfDecl( DeclarationTreeNode node ) throws EvaluationException {
        if ( node.getArguments().size() != 3 ) {
            throw new EvaluationException("Wrong (:if x y z) declaration arguments count");
        }

        ISyntaxTreeNode condition = node.shiftArgument();
        ISyntaxTreeNode defaultAction = node.shiftArgument();
        ISyntaxTreeNode otherwiseAction = node.shiftArgument();

        Object result;
        if ( this.<Boolean>visit(condition) ) {
            result = this.visit( defaultAction );
        } else {
            result = this.visit( otherwiseAction );
        }

        return result;
    }

	public Object visitEvalDecl( DeclarationTreeNode node ) throws EvaluationException {
		if ( node.getArguments().size() != 1 ) {
			throw new EvaluationException("Wrong (:eval x) declaration arguments count!");
		}

		ISyntaxTreeNode argument = node.getArguments().get(0);
		if ( argument.isDeclaration() ) {
			throw new EvaluationException("Wrong (:eval x) value type");
		}

		return this.getEvaluatorContext().evaluate( String.valueOf( this.visit(argument) ) );
	}

	protected <T> T visitIncludeDecl( DeclarationTreeNode node ) throws EvaluationException {
		if ( node.getArguments().size() != 1 ) {
			throw new EvaluationException("Wrong (:include x) declaration arguments count!");
		}

		ISyntaxTreeNode path = node.getArguments().get(0);
		Collection<String> targets = new HashSet<String>();
		if ( path.isDeclaration() ) {
			if ( !path.isList() ) {
				throw new EvaluationException("Only (:list) declaration supported as include path!");
			}

		    targets = this.visitListDecl( (DeclarationTreeNode) path);
		} else {
			targets.add( String.valueOf( this.visit( path ) ) );
		}

		for ( String target : targets ) {
			try {
				this.getEvaluatorContext().addInclude( target );
			} catch ( IOException e ) {
				throw new EvaluationException("Path " + target + " not exists or read access denied!");
			}
		}


		return null;
	}

	protected <T> T visitNewDecl( DeclarationTreeNode node ) throws EvaluationException {
		if ( node.getArguments().size() != 1 && node.getArguments().size() != 2 ) {
			throw new EvaluationException("Illegal (:new x [y] ) declaration arguments count!");
		}

		ISyntaxTreeNode argument = node.shiftArgument();
		if ( argument.isComposed() ) {
			throw new EvaluationException("Argument for instantiation declaration must not be of a complex type!");
		}

		String path = String.valueOf( this.visit(argument) );
		if ( path.isEmpty() || path == null ) {
			throw new EvaluationException("Name of object to be instantiated not provided.");
		}

		Object object = this.getContext().resolve(path);
		if ( !( object instanceof Class ) ) {
			throw new EvaluationException("Only class objects can be instantiated!");
		}

		Object[] arguments = null;
		if ( !node.getArguments().isEmpty() ) {
			ISyntaxTreeNode argumentsNode = node.shiftArgument();
			if ( argumentsNode instanceof DeclarationTreeNode ) {
				if ( ( (DeclarationTreeNode) argumentsNode ).getType().equals( DeclarationType.LIST ) ) {
					Collection<Object> argumentsColleciton
							= this.visitListDecl( (DeclarationTreeNode) argumentsNode );
					arguments = argumentsColleciton.toArray( new Object[ argumentsColleciton.size() ] );
				} else {
					throw new EvaluationException("Illegal declaration type passed as constructor argument!");
				}
			} else {
				arguments = new Object[] { this.visit(argumentsNode ) };
			}
		}

		try {
			if ( arguments == null || arguments.length == 0 ) {
				return ( (Class<T>) object ).newInstance();
			} else {
				return ( (Class<T> ) object ).getConstructor( ReflectionUtils.getTypesList(arguments) )
						.newInstance(arguments);
			}
		} catch ( Throwable e ) {
			throw new EvaluationException("Instantiation failed!");
		}
	}

	protected <T> T visitImportDecl( DeclarationTreeNode node ) throws EvaluationException {
		if ( node.getArguments().size() != 2 && node.getArguments().size() != 1) {
			throw new EvaluationException("Illegal (:import x [y] ) declaration arguments count!");
		}

		ISyntaxTreeNode pathNode = node.shiftArgument();
		if ( !( pathNode instanceof LiteralTreeNode) ) {
			throw new EvaluationException("Importing path argument must be literal type!");
		}

		String path = this.visit(pathNode);
		String alias = path;
		if ( node.getArguments().size() != 0 ) {
			ISyntaxTreeNode aliasNode = node.shiftArgument();
			if ( !( aliasNode instanceof LiteralTreeNode ) ) {
				throw new EvaluationException("Import alias must literal type!");
			}

			alias = this.visit( aliasNode );
		}

		try {
			this.getContext().exportClass( alias, Class.forName( path ) );
		} catch ( ClassNotFoundException e ) {
			throw new EvaluationException("Class `" + path + "` not exists!", e );
		}

		return null;
	}

	protected <T> T visitDefineDecl( DeclarationTreeNode node ) throws EvaluationException {
		if ( node.getArguments().size() != 3 ) {
			throw new EvaluationException("Illegal (:define x y z) declaration arguments count!");
		}

		ISyntaxTreeNode identifierNode = node.shiftArgument();
		if ( !( identifierNode instanceof LiteralTreeNode ) ) {
			throw new EvaluationException("Function identifier must be a literal type!");
		}

		this.getContext().exportFunction(
				String.valueOf(((LiteralTreeNode) identifierNode).getValue()),
				this.visitLambdaDecl( node ) );

		return null;
	}

	protected IFunction<?, ?> visitLambdaDecl( DeclarationTreeNode node ) throws EvaluationException {
		return this.visitLambdaDecl( null, node );
	}

	protected IFunction<?, ?> visitLambdaDecl( final String name, DeclarationTreeNode node ) throws EvaluationException {
		if ( node.getArguments().size() != 2 ) {
			throw new EvaluationException("Illegal (:lambda x y) declaration arguments count!");
		}

		ISyntaxTreeNode argumentsList = node.getArguments().get(0);
		if ( !( argumentsList instanceof DeclarationTreeNode ) ) {
			throw new EvaluationException("Wrong arguments list type");
		}

		final ISyntaxTreeNode body = node.getArguments().get(1);

		Collection<String> arguments = this.<Collection<String>>visit( argumentsList );

		return new Lambda( name,
				this, body,
				arguments.toArray( new String[ arguments.size() ] ) );
	}

	protected <T> T visitSetDecl( DeclarationTreeNode node ) throws EvaluationException {
		if ( node.getArguments().size() != 2 ) {
			throw new EvaluationException("Illegal (:set) declaration arguments count!");
		}

		ISyntaxTreeNode identifierNode = node.getArguments().get(0);
		if ( !( identifierNode instanceof LiteralTreeNode ) ) {
			throw new EvaluationException("Identifier name must be literal!");
		}

		ISyntaxTreeNode valueNode = node.getArguments().get(1);

		Object value = this.visit(valueNode);
		String identifier = String.valueOf( this.visit(identifierNode) );
		if ( this.isPrimitiveType( value ) ) {
			this.getContext().exportValue( identifier, value );
		} else if ( this.isClassType(value) ) {
			this.getContext().exportClass( identifier, (Class<?>) value );
		} else {
            this.getContext().exportBean( identifier, value != null ? value.getClass() : null, value );
		}

		return null;
	}

	protected boolean isPrimitiveType( Object value ) {
		return value instanceof Collection
				|| value instanceof Map
					|| value instanceof String
						|| value instanceof Number
							|| value instanceof Boolean;
	}

	protected boolean isClassType( Object value ) {
		return value instanceof Class;
	}

	protected <T> T visitMapDecl( DeclarationTreeNode node ) throws EvaluationException {
		Map<Object, Object> result = new HashMap<Object, Object>();

		for ( ISyntaxTreeNode argument : node.getArguments() ) {

			if ( !( argument instanceof DeclarationTreeNode ) ) {
				throw new EvaluationException("Illegal map item structure");
			}

			final DeclarationTreeNode declNode = (DeclarationTreeNode) argument;
			if ( !this.isValidMapItem(declNode ) ) {
				throw new EvaluationException("Illegal map item structure");
			}

			result.put( this.visit( declNode.getArguments().get(0) ),
					this.visit( declNode.getArguments().get(1) ) );
		}

		return (T) result;
	}

	protected boolean isValidMapItem( DeclarationTreeNode node ) throws EvaluationException {
		if ( !node.getType().equals( DeclarationType.LIST ) ) {
			return false;
		}

		if ( 2 != node.getArguments().size() ) {
			return false;
		}

		return true;
	}

	protected <T> List<T> visitListDecl( DeclarationTreeNode node ) throws EvaluationException {
		List<Object> result = new ArrayList<Object>();
		for ( ISyntaxTreeNode argument : node.getArguments() ) {
			result.add(this.visit(argument));
		}

		return (List<T>) result;
	}

	protected void preprocessDeclarations( ScopeTreeNode scope,
										   List<ISyntaxTreeNode> nodes ) throws EvaluationException {
		for ( int  i = 0; i < nodes.size(); i++ ) {
			ISyntaxTreeNode node = nodes.get(i);
			if ( node instanceof DeclarationTreeNode ) {
				final DeclarationTreeNode declarationTreeNode = (DeclarationTreeNode) node;

				boolean visited = false;
				switch ( declarationTreeNode.getType() ) {
					case DEFINE:
						this.visitDefineDecl(declarationTreeNode);
						visited = true;
					break;
					case IMPORT:
						this.visitImportDecl(declarationTreeNode);
						visited = true;
					break;
				}

				if ( visited ) {
					scope.removeArgument( node );
				}
			}
		}
	}

	@Override
	public <T> T visit( ScopeTreeNode node ) throws EvaluationException {
		List<Object> values = new ArrayList<Object>();

		this.preprocessDeclarations( node, node.getArguments() );

		for ( ISyntaxTreeNode childNode : node.getArguments() ) {
			values.add( this.visit(childNode) );
		}

		return (T) values;
	}

	@Override
	public <T> T visit(FunctionTreeNode node) throws EvaluationException {
		try {
			Object[] arguments = new Object[ node.getArguments().size() ];
			int i = 0;
			for ( ISyntaxTreeNode argNode : node.getArguments() ) {
				arguments[i++] = this.visit( argNode );
			}

			IFunction<?, T> function = this.getContext().resolveFunction(node.getName(), node.getArguments().size(), ReflectionUtils.getTypesList(arguments) );
			if ( function == null ) {
				throw new EvaluationException("Function " + node.getName() + " is not defined!");
			}

			return function.invoke( arguments );
		} catch ( InvocationTargetException e ) {
			throw new EvaluationException( "Error in function " + node.getName() + " ( " + node.toString() + ")", e.getTargetException() );
		} catch ( Throwable e ) {
			throw new EvaluationException( "Error in function " + node.getName(), e );
		}
	}

	@Override
	public Object visit(VariableTreeNode node) throws EvaluationException {
		try {
			return this.getContext().resolve(node.getName());
		} catch ( EvaluationException e  ) {
			throw new EvaluationException( e.getMessage(), e );
		}
	}

	@Override
	public Object visit(LiteralTreeNode node) {
		return node.getValue();
	}

	public static class Lambda extends Function<Object, Object> {
		private SyntaxTreeProcessor context;
		private String[] arguments;
		private ISyntaxTreeNode body;
		private String prefix;

		public Lambda( String name, SyntaxTreeProcessor context, ISyntaxTreeNode body, String[] arguments ) {
			this.context = context;
			this.body = body;
			this.arguments = arguments;
			this.prefix = "func. " + ( name == null ? UUID.randomUUID().toString() : name) + ".";

			this.init();
		}

		protected String getPrefix() {
			return this.prefix;
		}

		protected void init() {
			this.preProcessStatements( this.getPrefix(), this.body );
		}

		protected SyntaxTreeProcessor getContext() {
			return this.context;
		}

		protected void preProcessVariable( VariableTreeNode variable ) {
			if ( !variable.getName().startsWith(prefix) ) {
				variable.setName( prefix + variable.getName() );
			}
		}

		protected ISyntaxTreeNode preProcessStatements( String prefix, ISyntaxTreeNode parent ) {
			if ( parent.isComposed() ) {
				for ( ISyntaxTreeNode childNode : parent.asComposedNode().getArguments() ) {
					if ( !childNode.isComposed() ) {
						if ( childNode instanceof VariableTreeNode ) {
							this.preProcessVariable( (VariableTreeNode) childNode );
						}
					} else {
						this.preProcessStatements( prefix, childNode.asComposedNode() );
					}
				}
			} else if ( parent instanceof VariableTreeNode ) {
				this.preProcessVariable( (VariableTreeNode) parent);
			}

			return parent;
		}

		@Override
		public Object invoke( Object... args ) throws InvocationTargetException {
			try {
				if ( args.length < this.arguments.length ) {
					throw new IllegalArgumentException("Wrong function arguments count!");
				}

				Collection<String> created = new HashSet<String>();
				int i =  0;
				for ( Object argument : this.arguments ) {
					String name = this.getPrefix() + String.valueOf(argument);
					created.add(name);
					this.getContext().getContext().exportValue( name, args[i++] );
				}

				return this.getContext().visit( this.body );
			} catch ( Throwable e ) {
				throw new InvocationTargetException( e );
			}
		}
	}
}
