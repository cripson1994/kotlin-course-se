// Generated from /Users/vladislavkalinin/IdeaProjects/kotlin-course-se/src/main/antlr/FunLang.g4 by ANTLR 4.7
package antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FunLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FunLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FunLangParser#file}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFile(FunLangParser.FileContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(FunLangParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#block_with_braces}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_with_braces(FunLangParser.Block_with_bracesContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(FunLangParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(FunLangParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(FunLangParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#parameter_names}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_names(FunLangParser.Parameter_namesContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#while_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_expr(FunLangParser.While_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#if_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_expr(FunLangParser.If_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(FunLangParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#return_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_expr(FunLangParser.Return_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(FunLangParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#println}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintln(FunLangParser.PrintlnContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(FunLangParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(FunLangParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArithmeticParensExpr}
	 * labeled alternative in {@link FunLangParser#arithm_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticParensExpr(FunLangParser.ArithmeticParensExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalCompareExpr}
	 * labeled alternative in {@link FunLangParser#arithm_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalCompareExpr(FunLangParser.LogicalCompareExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArithmeticPrior2BinaryExpr}
	 * labeled alternative in {@link FunLangParser#arithm_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticPrior2BinaryExpr(FunLangParser.ArithmeticPrior2BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArithmeticAtomExpr}
	 * labeled alternative in {@link FunLangParser#arithm_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticAtomExpr(FunLangParser.ArithmeticAtomExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalBinaryExpr}
	 * labeled alternative in {@link FunLangParser#arithm_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalBinaryExpr(FunLangParser.LogicalBinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArithmeticPrior1BinaryExpr}
	 * labeled alternative in {@link FunLangParser#arithm_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticPrior1BinaryExpr(FunLangParser.ArithmeticPrior1BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#arithm_prior1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithm_prior1(FunLangParser.Arithm_prior1Context ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#arithm_prior2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithm_prior2(FunLangParser.Arithm_prior2Context ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#compare_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompare_op(FunLangParser.Compare_opContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunLangParser#logic_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogic_op(FunLangParser.Logic_opContext ctx);
}