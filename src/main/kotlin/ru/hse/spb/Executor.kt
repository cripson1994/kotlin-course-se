package ru.hse.spb

import antlr.FunLangBaseVisitor
import antlr.FunLangParser
import java.io.PrintStream
import java.util.stream.Collectors
import kotlin.streams.toList

class Executor : FunLangBaseVisitor<Int> {
    private val out: PrintStream;
    private var scope: Scope = Scope()
    private var returnState: Int? = null
    private var basicExecutor = BasicExecutor()
    private var expressionExecutor = ExpressionExecutor()

    constructor(out: PrintStream) : super() {
        this.out = out
    }

    constructor(scope: Scope, out: PrintStream) : super() {
        this.scope = scope
        this.out = out
    }

    override fun visitFile(ctx: FunLangParser.FileContext): Int? {
        scope.addNode()
        val result = ctx.block().accept(this)
        scope.removeNode()
        return result
    }

    override fun visitBlock(ctx: FunLangParser.BlockContext): Int? {
        for (statement in ctx.statement()) {
            statement.accept(basicExecutor)
            if (returnState != null) {
                return returnState
            }
        }
        return 0
    }

    override fun visitBlock_with_braces(ctx: FunLangParser.Block_with_bracesContext): Int? {
        return ctx.block().accept(this)
    }

    private inner class BasicExecutor : FunLangBaseVisitor<Unit>() {

        override fun visitBlock(ctx: FunLangParser.BlockContext) {
            for (statement in ctx.statement()) {
                statement.accept(basicExecutor)
                if (returnState != null) {
                    return
                }
            }
        }

        override fun visitBlock_with_braces(ctx: FunLangParser.Block_with_bracesContext) {
            ctx.block().accept(this)
        }

        override fun visitStatement(ctx: FunLangParser.StatementContext) {
            val ctxChild = ctx.function()
                    ?: ctx.variable()
                    ?: ctx.expression()
                    ?: ctx.while_expr()
                    ?: ctx.if_expr()
                    ?: ctx.assignment()
                    ?: ctx.return_expr()
                    ?: ctx.println()
            ctxChild.accept(this)
        }

        override fun visitFunction(ctx: FunLangParser.FunctionContext) {
            val funcName = ctx.IDENT().text
            try {
                if (!ArgumentsIsValidity(getArgumentsNames(ctx.parameter_names()))) {
                    out.println("Function ${funcName} contains duplicate argument names")
                    System.exit(1)
                }
                scope.addFunction(funcName, ctx)
            } catch (e: OverrideException) {
                out.println("Function ${funcName} can not be overloaded")
                System.exit(1)
            }
        }

        override fun visitVariable(ctx: FunLangParser.VariableContext) {
            val varName = ctx.IDENT().text
            val varValue = ctx.expression()?.accept(expressionExecutor)
            try {
                scope.addVariable(varName, varValue)
            } catch (e: OverrideException) {
                out.println("Variable ${varName} can not be redefine")
                System.exit(1)
            }
        }

        override fun visitExpression(ctx: FunLangParser.ExpressionContext) {
            ctx.accept(expressionExecutor)
        }

        override fun visitWhile_expr(ctx: FunLangParser.While_exprContext) {
            while (ctx.expression().accept(expressionExecutor) != 0) {
                scope.addNode()
                ctx.block_with_braces().accept(this)
                scope.removeNode()
            }
        }

        override fun visitIf_expr(ctx: FunLangParser.If_exprContext) {
            scope.addNode()
            if (ctx.expression().accept(expressionExecutor) != 0) {
                ctx.block_with_braces(0).accept(this)
            } else if (ctx.TELSE() != null) {
                ctx.block_with_braces(1).accept(this)
            }
            scope.removeNode()
        }

        override fun visitAssignment(ctx: FunLangParser.AssignmentContext) {
            val varName = ctx.IDENT().text
            val varValue = ctx.expression().accept(expressionExecutor)
            try {
                scope.modifyVariable(varName, varValue)
            } catch (e: ElementNotFoundException) {
                out.println("Variable ${varName} not in scope")
                System.exit(1)
            }
        }

        override fun visitReturn_expr(ctx: FunLangParser.Return_exprContext) {
            returnState = ctx.expression().accept(expressionExecutor)
        }

        override fun visitPrintln(ctx: FunLangParser.PrintlnContext) {
            val values = getArgumentsValues(ctx.arguments())
            val result = values.stream().map { x -> x.toString() }.collect(Collectors.joining(" "))
            out.println(result)
        }
    }

    private inner class ExpressionExecutor : FunLangBaseVisitor<Int>() {
        override fun visitExpression(ctx: FunLangParser.ExpressionContext): Int? {
            val ctxChild = ctx.function_call()
                    ?: ctx.arithm_expression()
                    ?: ctx.expression()
            return ctxChild.accept(this)
        }

        override fun visitFunction_call(ctx: FunLangParser.Function_callContext): Int? {
            val funcName = ctx.IDENT().text
            var result: Int? = null
            try {
                val func = scope.getFunction(funcName)
                val argumentsNames = getArgumentsNames(func.body.parameter_names())
                val argumentsValues = getArgumentsValues(ctx.arguments())
                if (argumentsNames.size != argumentsValues.size) {
                    out.println("Function ${funcName} has ${argumentsNames.size} arguments: ${argumentsNames}")
                    System.exit(1)
                }
                scope.addNode()
                for (i in 0 until argumentsNames.size) {
                    scope.addVariable(argumentsNames[i], argumentsValues[i])
                }
                result = func.body.block_with_braces().accept(Executor(scope, out))
                scope.removeNode()
            } catch (e: ElementNotFoundException) {
                out.println("Function ${funcName} not in scope")
                System.exit(1)
            } finally {
                return result
            }
        }

        override fun visitArithmeticPrior1BinaryExpr(ctx: FunLangParser.ArithmeticPrior1BinaryExprContext): Int? {
            val lvalue = ctx.lvalue.accept(this)
            val rvalue = ctx.rvalue.accept(this)
            val operation = ctx.op.text
            return evalArithmeticOperation(lvalue, rvalue, operation)
        }

        override fun visitArithmeticPrior2BinaryExpr(ctx: FunLangParser.ArithmeticPrior2BinaryExprContext): Int? {
            val lvalue = ctx.lvalue.accept(this)
            val rvalue = ctx.rvalue.accept(this)
            val operation = ctx.op.text
            return evalArithmeticOperation(lvalue, rvalue, operation)
        }

        override fun visitLogicalCompareExpr(ctx: FunLangParser.LogicalCompareExprContext): Int? {
            val lvalue = ctx.lvalue.accept(this)
            val rvalue = ctx.rvalue.accept(this)
            val operation = ctx.op.text
            val result = evalLogicalCompareOperation(lvalue, rvalue, operation)
            return castBooleanToInt(result)
        }

        override fun visitLogicalBinaryExpr(ctx: FunLangParser.LogicalBinaryExprContext): Int? {
            val lvalue = castIntToBoolean(ctx.lvalue.accept(this))
            val rvalue = castIntToBoolean(ctx.rvalue.accept(this))
            val operation = ctx.op.text
            val result = evalLogicalBinaryOperation(lvalue, rvalue, operation)
            return castBooleanToInt(result)
        }

        override fun visitArithmeticParensExpr(ctx: FunLangParser.ArithmeticParensExprContext): Int? {
            return ctx.arithm_expression().accept(this)
        }

        override fun visitArithmeticAtomExpr(ctx: FunLangParser.ArithmeticAtomExprContext): Int? {
            if (ctx.TDIGIT() != null) {
                return ctx.TDIGIT().text.toInt()
            }
            if (ctx.IDENT() != null) {
                val varName = ctx.IDENT().text
                try {
                    return scope.getValue(varName)
                } catch (e: ElementNotFoundException) {
                    out.println("Variable ${varName} not in scope")
                    System.exit(1)
                }
            }
            if (ctx.function_call() != null) {
                return ctx.function_call().accept(this)
            }
            return null
        }

        fun evalLogicalCompareOperation(lvalue: Int, rvalue: Int, operation: String): Boolean {
            return when (operation) {
                "==" -> lvalue == rvalue
                "<" -> lvalue < rvalue
                ">" -> lvalue > rvalue
                "<=" -> lvalue <= rvalue
                ">=" -> lvalue >= rvalue
                else -> false
            }
        }

        fun evalLogicalBinaryOperation(lvalue: Boolean, rvalue: Boolean, operation: String): Boolean {
            return when (operation) {
                "||" -> lvalue || rvalue
                "&&" -> lvalue && rvalue
                else -> false
            }
        }

        fun evalArithmeticOperation(lvalue: Int, rvalue: Int, operation: String): Int {
            return when (operation) {
                "+" -> lvalue + rvalue
                "-" -> lvalue - rvalue
                "*" -> lvalue * rvalue
                "/" -> lvalue / rvalue
                "%" -> lvalue % rvalue
                else -> 0
            }
        }
    }

    fun ArgumentsIsValidity(arguments: List<String>): Boolean {
        return arguments.toSet().size == arguments.size
    }

    fun getArgumentsValues(ctx: FunLangParser.ArgumentsContext): List<Int> {
        return ctx.expression().stream().mapToInt { x -> x.accept(expressionExecutor) }.toList()
    }

    fun getArgumentsNames(ctx: FunLangParser.Parameter_namesContext): List<String> {
        return ctx.IDENT().map { x -> x.text }
    }

    fun castBooleanToInt(value: Boolean): Int {
        return if (value) 1 else 0
    }

    fun castIntToBoolean(value: Int): Boolean {
        return value != 0
    }
}