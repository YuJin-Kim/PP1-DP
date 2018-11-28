package edu.handong.csee.pp1.dp.utils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

public class JavaASTParser {
	CompilationUnit cUnit;
	String source;
	ArrayList<ImportDeclaration> lstImportDeclaration = new ArrayList<ImportDeclaration>();
	ArrayList<MethodDeclaration> lstMethodDeclaration = new ArrayList<MethodDeclaration>();
	ArrayList<MethodDeclaration> lstPublicMethodDeclaration = new ArrayList<MethodDeclaration>();
	ArrayList<MethodInvocation> lstMethodInvocation = new ArrayList<MethodInvocation>();
	ArrayList<FieldDeclaration> lstFieldDeclaration = new ArrayList<FieldDeclaration>();
	ArrayList<FieldDeclaration> lstInstanceVariable = new ArrayList<FieldDeclaration>();
	ArrayList<FieldAccess> lstFieldAccess = new ArrayList<FieldAccess>();
	ArrayList<IfStatement> lstIfStatement = new ArrayList<IfStatement>();
	ArrayList<ForStatement> lstForStatement = new ArrayList<ForStatement>();
	ArrayList<WhileStatement> lstWhileStatement = new ArrayList<WhileStatement>();
	ArrayList<VariableDeclarationFragment> lstVariableDeclarationFragment = new ArrayList<VariableDeclarationFragment>();
	ArrayList<ClassInstanceCreation> lstClassInstanceCreation = new ArrayList<ClassInstanceCreation>();
	ArrayList<SingleVariableDeclaration> lstSingleVariableDeclaration = new ArrayList<SingleVariableDeclaration>();
	ArrayList<SimpleName> lstSimpleName = new ArrayList<SimpleName>();
	ArrayList<TypeDeclaration> lstTypeDeclaration = new ArrayList<TypeDeclaration>();
	ArrayList<InfixExpression> lstInfixExpression = new ArrayList<InfixExpression>();
	ArrayList<ConditionalExpression> lstConditionalExpression = new ArrayList<ConditionalExpression>();


	PackageDeclaration pkgDeclaration;

	public JavaASTParser(String source){
		this.source = source;
		praseJavaFile(source);
	}

	public int getLineNum(int startPosition){
		return cUnit.getLineNumber(startPosition);
	}

	public String getStringCode(){
		return source;
	}

	public CompilationUnit getCompilationUnit(){
		return cUnit;
	}

	public void praseJavaFile(String source){

		ASTParser parser = ASTParser.newParser(AST.JLS8);

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		char[] content = source.toCharArray();
		parser.setSource(content);
		//parser.setUnitName("temp.java");
		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		String[] sources = {};
		String[] classPaths = {};
		parser.setEnvironment(classPaths, sources, null, true);
		parser.setResolveBindings(false);
		parser.setCompilerOptions(options);
		parser.setStatementsRecovery(true);

		try {
			final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
			cUnit = unit;

			// Process the main body
			try {
				unit.accept(new ASTVisitor() {

					public boolean visit(MethodDeclaration node) {
						for(Object element : node.modifiers()) {
							IExtendedModifier modifier = (IExtendedModifier)element;
							if(modifier.isModifier() && modifier.toString().equals("public"))
								lstPublicMethodDeclaration.add(node);
						}
						
						lstMethodDeclaration.add(node);
						return super.visit(node);
					}

					public boolean visit(MethodInvocation node) {
						lstMethodInvocation.add(node);
						return super.visit(node);
					}

					public boolean visit(TypeDeclaration node) {
						lstTypeDeclaration.add(node);
						return super.visit(node);
					}

					public boolean visit(final FieldDeclaration node) {
						lstFieldDeclaration.add(node);
						
						for(Object element : node.modifiers()) {
							IExtendedModifier modifier = (IExtendedModifier)element;
							if(modifier.isModifier()) {
								if(modifier.toString().equals("static")) return super.visit(node);
								else continue;
							}
						}
						
						lstInstanceVariable.add(node);
						return super.visit(node);
					}

					public boolean visit(final SingleVariableDeclaration node) {
						lstSingleVariableDeclaration.add(node);
						return super.visit(node);
					}

					public boolean visit(final VariableDeclarationFragment node) {
						lstVariableDeclarationFragment.add(node);
						return super.visit(node);
					}

					public boolean visit(final ClassInstanceCreation node) {
						lstClassInstanceCreation.add(node);
						return super.visit(node);
					}

					public boolean visit(final FieldAccess node) {
						lstFieldAccess.add(node);
						return super.visit(node);
					}

					public boolean visit(IfStatement node) {
						lstIfStatement.add(node);
						return super.visit(node);
					}

					public boolean visit(ForStatement node) {
						lstForStatement.add(node);
						return super.visit(node);
					}

					public boolean visit(WhileStatement node) {
						lstWhileStatement.add(node);
						return super.visit(node);
					}

					public boolean visit(InfixExpression node) {
						lstInfixExpression.add(node);
						return super.visit(node);
					}

					public boolean visit(SimpleName node) {
						lstSimpleName.add(node);
						return super.visit(node);
					}

					public boolean visit(final ImportDeclaration node) {
						lstImportDeclaration.add(node);
						return super.visit(node);
					}

					public boolean visit(final PackageDeclaration node) {
						pkgDeclaration = node;
						return super.visit(node);
					}

					public boolean visit(final AnonymousClassDeclaration node) {
						//Log.info("AnonymousClassDeclaration");
						//Log.info(node);
						return super.visit(node);
					}

					//Expression ? Expression : Expression
					public boolean visit(final ConditionalExpression node) {
						lstConditionalExpression.add(node);
						return super.visit(node);
					}

					/*public boolean visit(CatchClause node) {
						list.add("CatchClause");
						return super.visit(node);
					}
					public boolean visit(ClassInstanceCreation node) {
						list.add("ClassInstanceCreation");
						list.add(node.getName().toString());
						return super.visit(node);
					}

					public boolean visit(DoStatement node) {
						list.add("DoStatement");

						return super.visit(node);
					}
					public boolean visit(EnumConstantDeclaration node) {
						list.add(node.getName().toString());
						return super.visit(node);
					}
					public boolean visit(EnumDeclaration node) {
						list.add("EnumDeclaration");
						list.add(node.getName().toString());
						return super.visit(node);
					}
					public boolean visit(ForStatement node) {
						list.add("ForStatement");

						return super.visit(node);
					}
					public boolean visit(EnhancedForStatement node) {
						list.add("ForStatement");

						return super.visit(node);
					}

					public boolean visit(SingleVariableDeclaration node) {
						list.add("SingleVariableDeclaration");

						return super.visit(node);
					}

					public boolean visit(SimpleName node) {
						list.add("SimpleName");

						return super.visit(node);
					}


					public boolean visit(IfStatement node) {
						list.add("IfStatement");
						lstIfStatement.add(node);
						return super.visit(node);
					}

					public boolean visit(MethodDeclaration node) {
						lstMethodDeclaration.add(node);
						list.add("METHOD:" + node.getName().toString() + node.parameters().toString() + ":" + node.getStartPosition() + ":" + node.getLength());
						return super.visit(node);
					}

					public boolean visit(AssertStatement node) {
						list.add("AssertStatement");
						return super.visit(node);
					} 
					public boolean visit(ContinueStatement node) {
						list.add("ContinueStatement");
						return super.visit(node);
					}

					public boolean visit(SwitchCase node) {
						list.add("SwitchCase");
						return super.visit(node);
					}
					public boolean visit(SynchronizedStatement node) {
						list.add("SynchronizedStatement");
						return super.visit(node);
					}
					public boolean visit(ThisExpression node) {
						list.add("ThisExpression");
						return super.visit(node);
					}
					public boolean visit(ThrowStatement node) {
						list.add("ThrowStatement");
						return super.visit(node);
					}
					public boolean visit(TryStatement node) {
						list.add("TryStatement");
						return super.visit(node);
					}
					public boolean visit(TypeDeclaration node) {
						list.add(node.getName().toString());
						return super.visit(node);
					}
					public boolean visit(WhileStatement node) {

						list.add("WhileStatement");
						return super.visit(node);
					}
					public boolean visit(final FieldAccess node) {

						lstFieldAccess.add(node);

						return super.visit(node);
					}

					public boolean visit(final FieldDeclaration node) {
						lstFieldDeclaration.add(node);
						return super.visit(node);
					}

					public boolean visit(final Block node) {

						return super.visit(node);
					}

					public boolean visit(final Assignment node) {

						return super.visit(node);
					}

					public boolean visit(final ExpressionStatement node) {

						return super.visit(node);
					}

					public boolean visit(final AnnotationTypeDeclaration node) {
						//Log.info("AnnotationTypeDeclaration");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final AnnotationTypeMemberDeclaration node) {
						//Log.info("AnnotationTypeMemberDeclaration");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final AnonymousClassDeclaration node) {
						//Log.info("AnonymousClassDeclaration");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayAccess node) {
						//Log.info("ArrayAccess");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayCreation node) {
						//Log.info("ArrayCreation");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayInitializer node) {
						//Log.info("ArrayInitializer");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayType node) {
						//Log.info("ArrayType");
						//Log.info(node);
						return super.visit(node);
					}


					public boolean visit(final BlockComment node) {
						//Log.info("BlockComment");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final BooleanLiteral node) {
						//Log.info("BooleanLiteral");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final CastExpression node) {
						//Log.info("CastExpression");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final CharacterLiteral node) {
						//Log.info("CharacterLiteral");
						//Log.info(node);
						return super.visit(node);
					}



					public boolean visit(final CompilationUnit node) {
						//Log.info("CompilationUnit");
						//Log.info(node);
						return super.visit(node);
					}



					public boolean visit(final ConstructorInvocation node) {
						//Log.info("ConstructorInvocation");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final CreationReference node) {
						//Log.info("CreationReference");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final Dimension node) {
						//Log.info("Dimension");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final EmptyStatement node) {
						//Log.info("EmptyStatement");
						//Log.info(node);
						return super.visit(node);
					}

				public boolean visit(final ImportDeclaration node) {
					//Log.info("ImportDeclaration");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final InfixExpression node) {
					//Log.info("InfixExpression");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final Initializer node) {
					//Log.info("Initializer");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final InstanceofExpression node) {
					//Log.info("InstanceofExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final IntersectionType node) {
					//Log.info("IntersectionType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final Javadoc node) {
					//Log.info("Javadoc");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final LabeledStatement node) {
					//Log.info("LabeledStatement");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final LambdaExpression node) {
					//Log.info("LambdaExpression");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final LineComment node) {
					//Log.info("LineComment");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MarkerAnnotation node) {
					//Log.info("MarkerAnnotation");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MemberRef node) {
					//Log.info("MemberRef");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final MemberValuePair node) {
					//Log.info("MemberValuePair");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MethodDeclaration node) {
					//Log.info("MethodDeclaration");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MethodRef node) {
					//Log.info("MethodRef");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MethodRefParameter node) {
					//Log.info("MethodRefParameter");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final Modifier node) {
					//Log.info("Modifier");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final NameQualifiedType node) {
					//Log.info("NameQualifiedType");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final NormalAnnotation node) {
					//Log.info("NormalAnnotation");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final NullLiteral node) {
					//Log.info("NullLiteral");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final NumberLiteral node) {
					//Log.info("NumberLiteral");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final PackageDeclaration node) {
					//Log.info("PackageDeclaration");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final ParameterizedType node) {
					//Log.info("ParameterizedType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final ParenthesizedExpression node) {
					//Log.info("ParenthesizedExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final PostfixExpression node) {
					//Log.info("PostfixExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final PrefixExpression node) {
					//Log.info("PrefixExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final PrimitiveType node) {
					//Log.info("PrimitiveType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final QualifiedName node) {
					//Log.info("QualifiedName");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final QualifiedType node) {
					//Log.info("QualifiedType");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final SimpleType node) {
					//Log.info("SimpleType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SingleMemberAnnotation node) {
					//Log.info("SingleMemberAnnotation");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final StringLiteral node) {
					//Log.info("StringLiteral");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SuperConstructorInvocation node) {
					//Log.info("SuperConstructorInvocation");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SuperFieldAccess node) {
					//Log.info("SuperFieldAccess");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SuperMethodInvocation node) {
					//Log.info("SuperMethodInvocation");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SuperMethodReference node) {
					//Log.info("SuperMethodReference");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TagElement node) {
					//Log.info("TagElement");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TextElement node) {
					//Log.info("TextElement");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final TypeDeclarationStatement node) {
					//Log.info("TypeDeclarationStatement");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TypeLiteral node) {
					//Log.info("TypeLiteral");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TypeMethodReference node) {
					//Log.info("TypeMethodReference");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TypeParameter node) {
					//Log.info("UnionType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final UnionType node) {
					//Log.info("UnionType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final VariableDeclarationExpression node) {
					//Log.info("VariableDeclarationExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final VariableDeclarationFragment node) {
					//Log.info("VariableDeclarationFragment");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final VariableDeclarationStatement node) {
					//Log.info("VariableDeclarationStatement");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final WildcardType node) {
					//Log.info("WildcardType");
					//Log.info(node);
					return super.visit(node);
				}*/
				});
			} catch (Exception e) {
				System.out.println("Problem : " + e.toString());
				e.printStackTrace();
				System.exit(0);
			}

		} catch (Exception e) {
			System.out.println("\nError while executing compilation unit : " + e.toString());
		}

	}

	public ArrayList<MethodDeclaration> getMethodDeclarations() {
		return lstMethodDeclaration;
	}

	public ArrayList<MethodDeclaration> getPublicMethodDeclarations() {
		return lstPublicMethodDeclaration;
	}

	public ArrayList<MethodInvocation> getMethodInvocations() {
		return lstMethodInvocation;
	}

	public ArrayList<TypeDeclaration> getTypeDeclarations() {
		return lstTypeDeclaration;
	}

	public ArrayList<FieldDeclaration> getFieldDeclarations() {
		return lstFieldDeclaration;
	}
	
	public ArrayList<FieldDeclaration> getInstanceVariables() {
		return lstInstanceVariable;
	}

	public HashMap<String,VariableDeclarationFragment> getMapForFieldDeclarations() {

		HashMap<String,VariableDeclarationFragment> maps = new HashMap<String,VariableDeclarationFragment>();

		for(FieldDeclaration fieldDec:lstFieldDeclaration){

			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment> fieldDecFrags = fieldDec.fragments();

			for(VariableDeclarationFragment varDecFrag:fieldDecFrags){
				maps.put(varDecFrag.getName().toString(), varDecFrag);
			}
		}

		return maps;
	}

	public ArrayList<FieldAccess> getFieldAccesses() {
		return lstFieldAccess;
	}

	public ArrayList<IfStatement> getIfStatements() {
		return lstIfStatement;
	}

	public ArrayList<ForStatement> getForStatements() {
		return lstForStatement;
	}

	public ArrayList<WhileStatement> getWhileStatements() {
		return lstWhileStatement;
	}

	public ArrayList<InfixExpression> getInfixExpressions() {
		return lstInfixExpression;
	}

	public ArrayList<SimpleName> getSimpleNames() {
		return lstSimpleName;
	}

	public ArrayList<VariableDeclarationFragment> getVariableDeclarationFragments() {
		return lstVariableDeclarationFragment;
	}

	public ArrayList<ClassInstanceCreation> getClassInstanceCreations() {
		return lstClassInstanceCreation;
	}

	public ArrayList<SingleVariableDeclaration> getSingleVariableDeclarations() {
		return lstSingleVariableDeclaration;
	}

	public ArrayList<ImportDeclaration> getImportDeclarations() {
		return lstImportDeclaration;
	}

	public ArrayList<ConditionalExpression> getConditionalExpressions() {
		return lstConditionalExpression;
	}

	public PackageDeclaration getPackageDeclaration(){
		return pkgDeclaration;
	}

	public String getTypeOfSimpleName(ASTNode astNode,String name) {

		// TODO need to find a target name in a hierarchy but not globally in a file
		final ArrayList<SingleVariableDeclaration> lstSingleVarDecs = new ArrayList<SingleVariableDeclaration>();
		final ArrayList<VariableDeclarationStatement> lstVarDecStmts = new ArrayList<VariableDeclarationStatement>();
		final ArrayList<FieldDeclaration> lstFieldDecs = new ArrayList<FieldDeclaration>();
		final ArrayList<VariableDeclarationExpression> lstVarDecExps = new ArrayList<VariableDeclarationExpression>();

		astNode.accept(new ASTVisitor() {
			public boolean visit(SingleVariableDeclaration node) {
				lstSingleVarDecs.add(node);
				return super.visit(node);
			}
			public boolean visit(VariableDeclarationStatement node) {
				lstVarDecStmts.add(node);
				return super.visit(node);
			}
			public boolean visit(VariableDeclarationExpression node) {
				lstVarDecExps.add(node);
				return super.visit(node);
			}
			public boolean visit(FieldDeclaration node) {
				lstFieldDecs.add(node);
				return super.visit(node);
			}
		}
				);

		for(SingleVariableDeclaration dec:lstSingleVarDecs){
			if (dec.getName().toString().equals(name))
				return dec.getType().toString();
		}
		for(VariableDeclarationStatement dec:lstVarDecStmts){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}
		for(VariableDeclarationExpression dec:lstVarDecExps){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}

		for(FieldDeclaration dec:lstFieldDecs){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}

		if(astNode.getParent() == null)
			return "";

		return getTypeOfSimpleName(astNode.getParent(),name);
	}

	public ArrayList<SimpleName> getSimpleNames(ASTNode node) {

		final ArrayList<SimpleName> simpleNames = new ArrayList<SimpleName>();

		node.accept(new ASTVisitor() {

			public boolean visit(SimpleName node) {
				simpleNames.add(node);
				return super.visit(node);
			}

		});

		return simpleNames;
	}

	public ArrayList<QualifiedName> getQualifiedNames(ASTNode exp) {
		final ArrayList<QualifiedName> qualifiedNames = new ArrayList<QualifiedName>();

		exp.accept(new ASTVisitor() {

			public boolean visit(QualifiedName node) {
				qualifiedNames.add(node);
				return super.visit(node);
			}

		});

		return qualifiedNames;
	}

	public ArrayList<MethodInvocation> getMethodInvocations(ASTNode node) {
		final ArrayList<MethodInvocation> methodInvocations = new ArrayList<MethodInvocation>();

		node.accept(new ASTVisitor() {

			public boolean visit(MethodInvocation node) {
				methodInvocations.add(node);
				return super.visit(node);
			}

		});

		return methodInvocations;
	}

	public ArrayList<InfixExpression> getInfixExpressions(Expression exp) {
		final ArrayList<InfixExpression> infixExps = new ArrayList<InfixExpression>();

		exp.accept(new ASTVisitor() {

			public boolean visit(InfixExpression node) {
				infixExps.add(node);
				return super.visit(node);
			}

		});

		return infixExps;
	}

	public ArrayList<ArrayAccess> getArrayAccesses(ASTNode node) {
		final ArrayList<ArrayAccess> arrayAccesses = new ArrayList<ArrayAccess>();

		node.accept(new ASTVisitor() {

			public boolean visit(ArrayAccess node) {
				arrayAccesses.add(node);
				return super.visit(node);
			}

		});

		return arrayAccesses;
	}

	public ArrayList<ExpressionStatement> getExpressionStatements(ASTNode node) {
		final ArrayList<ExpressionStatement> expStmts = new ArrayList<ExpressionStatement>();

		node.accept(new ASTVisitor() {

			public boolean visit(ExpressionStatement node) {
				expStmts.add(node);
				return super.visit(node);
			}

		});

		return expStmts;
	}

	public ArrayList<VariableDeclarationFragment> getVariableDeclarationFragments(ASTNode node) {
		final ArrayList<VariableDeclarationFragment> varDecFrags = new ArrayList<VariableDeclarationFragment>();

		node.accept(new ASTVisitor() {

			public boolean visit(VariableDeclarationFragment node) {
				varDecFrags.add(node);
				return super.visit(node);
			}

		});

		return varDecFrags;
	}

	public ArrayList<VariableDeclaration> getVariableDeclaration(ASTNode node) {
		final ArrayList<VariableDeclaration> varDecs = new ArrayList<VariableDeclaration>();

		node.accept(new ASTVisitor() {

			public boolean visit(VariableDeclarationFragment node) {
				varDecs.add(node);
				return super.visit(node);
			}

			public boolean visit(SingleVariableDeclaration node) {
				varDecs.add(node);
				return super.visit(node);
			}

		});

		return varDecs;
	}

	public HashMap<String,VariableDeclaration> getMapForVariableDeclaration(ASTNode node) {
		final HashMap<String, VariableDeclaration> mapVarDecs = new HashMap<String,VariableDeclaration>();

		if(node==null) return mapVarDecs;

		node.accept(new ASTVisitor() {

			public boolean visit(VariableDeclarationFragment node) {
				mapVarDecs.put(node.getName().toString(),node);
				return super.visit(node);
			}

			public boolean visit(SingleVariableDeclaration node) {
				mapVarDecs.put(node.getName().toString(),node);
				return super.visit(node);
			}

		});

		return mapVarDecs;
	}

	public ArrayList<String> getFieldNames() {

		ArrayList<String> fieldNames = new ArrayList<String>();

		for(FieldDeclaration fieldDec:getFieldDeclarations()){
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment>  varDecFrags = fieldDec.fragments();
			for(VariableDeclarationFragment varDecFrag:varDecFrags){
				fieldNames.add(varDecFrag.getName().toString());
			}
		}

		return fieldNames;
	}

	public ArrayList<String> getVariableNames(ASTNode node) {
		ArrayList<String> localVarNames = new ArrayList<String>();

		ArrayList<VariableDeclarationFragment> varDecFrags = getVariableDeclarationFragments(node);		
		for(VariableDeclarationFragment verDecFrag:varDecFrags){
			localVarNames.add(verDecFrag.getName().toString());
		}

		ArrayList<SingleVariableDeclaration> singleVarDecs = getSingleVariableDeclarations(node);
		for(SingleVariableDeclaration singleVarDec:singleVarDecs){
			localVarNames.add(singleVarDec.getName().toString());
		}

		return localVarNames;
	}

	private ArrayList<SingleVariableDeclaration> getSingleVariableDeclarations(ASTNode node) {
		final ArrayList<SingleVariableDeclaration> singleVarDecs = new ArrayList<SingleVariableDeclaration>();

		node.accept(new ASTVisitor() {

			public boolean visit(SingleVariableDeclaration node) {
				singleVarDecs.add(node);
				return super.visit(node);
			}

		});

		return singleVarDecs;
	}

	public AbstractTypeDeclaration getTypeDeclationOf(ASTNode node) {

		if(node==null) // null can be happen when there is no TypeDeclaration but EnumDeclaration
			return null;

		if(node.getParent() instanceof AbstractTypeDeclaration)
			return (AbstractTypeDeclaration) node.getParent();

		return getTypeDeclationOf(node.getParent());
	}

	public ArrayList<String> getFieldNames(AbstractTypeDeclaration classWhereMethodExists) {
		ArrayList<String> fieldNames = new ArrayList<String>();

		for(FieldDeclaration fieldDec:getFieldDeclarations()){

			if(!fieldDec.getParent().equals(classWhereMethodExists)) continue;

			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment>  varDecFrags = fieldDec.fragments();
			for(VariableDeclarationFragment varDecFrag:varDecFrags){
				fieldNames.add(varDecFrag.getName().toString());
			}
		}

		return fieldNames;
	}

	public ArrayList<FieldAccess> getFieldAccesses(ASTNode node) {
		final ArrayList<FieldAccess> fieldAccesses = new ArrayList<FieldAccess>();

		node.accept(new ASTVisitor() {

			public boolean visit(FieldAccess node) {
				fieldAccesses.add(node);
				return super.visit(node);
			}

		});

		return fieldAccesses;
	}

	public MethodDeclaration getMethodDec(ASTNode node) {

		if(node.getParent() == null)
			return null;

		if(node.getParent() instanceof MethodDeclaration){
			return (MethodDeclaration) node.getParent();
		}

		return getMethodDec(node.getParent());
	}

	public TypeDeclaration getTypeDeclaration(ASTNode node) {

		if(node.getParent() == null) return null;

		if(node.getParent() instanceof TypeDeclaration)
			return (TypeDeclaration) node.getParent();

		return getTypeDeclaration(node.getParent());
	}

	public MethodDeclaration getMethodDecBelongTo(ASTNode node) {

		if(node.getParent() == null) return null;

		if(node.getParent() instanceof MethodDeclaration)
			return (MethodDeclaration) node.getParent();


		return getMethodDecBelongTo(node.getParent());
	}

	public ArrayList<ThrowStatement> getThrowStatements(ASTNode node) {
		final ArrayList<ThrowStatement> throwStatements = new ArrayList<ThrowStatement>();

		node.accept(new ASTVisitor() {

			public boolean visit(ThrowStatement node) {
				throwStatements.add(node);
				return super.visit(node);
			}

		});

		return throwStatements;
	}

	public ArrayList<Assignment> getAssignments(ASTNode node) {
		final ArrayList<Assignment> assignments = new ArrayList<Assignment>();

		node.accept(new ASTVisitor() {

			public boolean visit(Assignment node) {
				assignments.add(node);
				return super.visit(node);
			}

		});

		return assignments;
	}

	public ASTNode getInterface(ASTNode node) {

		if(node == null) return null;

		if(node.getParent() instanceof TypeDeclaration)
			return node.getParent();

		return getInterface(node.getParent());
	}
}
