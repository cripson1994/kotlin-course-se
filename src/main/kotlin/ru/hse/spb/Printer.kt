package ru.hse.spb

import antlr.FunLangBaseVisitor
import antlr.FunLangParser
import java.io.PrintStream
import java.util.stream.Collectors

class Printer(private val out: PrintStream) : FunLangBaseVisitor<Unit>() {
    private var prefix = 0
    private val indent = 4;

    private fun printWithPrefix(msg: String) {
        if (prefix > 0) {
            val ws = "%${prefix}s".format("")
            out.print(ws + msg)
        } else {
            out.print(msg)
        }
    }

    override fun visitFile(ctx: FunLangParser.FileContext) {
        ctx.block().accept(this)
    }

    override fun visitBlock(ctx: FunLangParser.BlockContext) {
        for (statement in ctx.statement()) {
            statement.accept(this)
        }
    }

    override fun visitBlock_with_braces(ctx: FunLangParser.Block_with_bracesContext) {
        out.print(" {\n")
        prefix += indent
        ctx.block().accept(this)
        prefix -= indent
        printWithPrefix("}")
    }

    override fun visitStatement(ctx: FunLangParser.StatementContext) {
        printWithPrefix("");
        val ctxChild = ctx.function()
                ?: ctx.variable()
                ?: ctx.expression()
                ?: ctx.while_expr()
                ?: ctx.if_expr()
                ?: ctx.assignment()
                ?: ctx.return_expr()
                ?: ctx.println()
        ctxChild.accept(this)
        out.print("\n")
    }

    override fun visitFunction(ctx: FunLangParser.FunctionContext) {
        out.print("${ctx.TFUN().text} ${ctx.IDENT().text}(")
        ctx.parameter_names().accept(this)
        out.print(")")
        ctx.block_with_braces().accept(this)
        out.print("\n")
    }

    override fun visitParameter_names(ctx: FunLangParser.Parameter_namesContext) {
        val args = ctx.IDENT().stream().map { x -> x.text }.collect(Collectors.joining(", "))
        out.print(args)
    }

    override fun visitVariable(ctx: FunLangParser.VariableContext) {
        out.print("${ctx.TVAR()} ${ctx.IDENT()}")
        if (ctx.TAS() != null) {
            out.print(" = ")
            ctx.expression().accept(this)
        }
    }

    override fun visitExpression(ctx: FunLangParser.ExpressionContext) {
        val ctxChild = ctx.function_call()
                ?: ctx.arithm_expression()
                ?: ctx.expression()
        ctxChild.accept(this)
    }

    override fun visitFunction_call(ctx: FunLangParser.Function_callContext) {
        out.print("${ctx.IDENT()}(")
        ctx.arguments().accept(this)
        out.print(")")
    }

    override fun visitArguments(ctx: FunLangParser.ArgumentsContext) {
        val args = ctx.expression()
        if (args.size > 0) {
            for (i in 0 until args.size - 1) {
                args[i].accept(this)
                out.print(", ")
            }
            args[args.size - 1].accept(this)
        }
    }

    private fun printArithmetic(ctx: FunLangParser.Arithm_expressionContext) {
        ctx.children[0].accept(this)
        out.print(" ${ctx.children[1].text} ")
        ctx.children[2].accept(this)
    }

    override fun visitArithmeticPrior1BinaryExpr(ctx: FunLangParser.ArithmeticPrior1BinaryExprContext) {
        printArithmetic(ctx)
    }

    override fun visitArithmeticPrior2BinaryExpr(ctx: FunLangParser.ArithmeticPrior2BinaryExprContext) {
        printArithmetic(ctx)
    }

    override fun visitLogicalCompareExpr(ctx: FunLangParser.LogicalCompareExprContext) {
        printArithmetic(ctx)
    }

    override fun visitLogicalBinaryExpr(ctx: FunLangParser.LogicalBinaryExprContext) {
        printArithmetic(ctx)
    }

    override fun visitArithmeticParensExpr(ctx: FunLangParser.ArithmeticParensExprContext) {
        out.print("(")
        ctx.arithm_expression().accept(this)
        out.print(")")
    }

    override fun visitArithmeticAtomExpr(ctx: FunLangParser.ArithmeticAtomExprContext) {
        if (ctx.function_call() != null) {
            ctx.function_call().accept(this)
        } else {
            out.print(ctx.children[0].text)
        }
    }

    override fun visitWhile_expr(ctx: FunLangParser.While_exprContext) {
        out.print("${ctx.TWHILE().text} (")
        ctx.expression().accept(this)
        out.print(")")
        ctx.block_with_braces().accept(this)
    }

    override fun visitIf_expr(ctx: FunLangParser.If_exprContext) {
        out.print("${ctx.TIF().text} (")
        ctx.expression().accept(this)
        out.print(")")
        ctx.block_with_braces(0).accept(this)
        if (ctx.TELSE() != null) {
            out.print(" else")
            ctx.block_with_braces(1).accept(this)
        }
    }

    override fun visitAssignment(ctx: FunLangParser.AssignmentContext) {
        out.print("${ctx.IDENT()} ${ctx.TAS()} ")
        ctx.expression().accept(this)
    }

    override fun visitReturn_expr(ctx: FunLangParser.Return_exprContext) {
        out.print("${ctx.TRETURN()} ")
        ctx.expression().accept(this)
    }

    override fun visitPrintln(ctx: FunLangParser.PrintlnContext) {
        out.print("${ctx.TPRINT()}(")
        ctx.arguments().accept(this)
        out.print(")")
    }

}