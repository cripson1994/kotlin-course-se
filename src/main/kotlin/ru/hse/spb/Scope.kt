package ru.hse.spb

import antlr.FunLangParser
import java.util.*
import kotlin.collections.HashMap

class Scope {

    constructor()

    constructor(scope: Scope) {
        val otherScope = scope.nodes
        for (node in otherScope) {
            this.nodes.add(node)
        }
    }

    private class Node {
        val variables = HashMap<String, Int>()
        val functions = HashMap<String, FunctionData>()
    }

    data class FunctionData(val body: FunLangParser.FunctionContext, val scope: Scope)

    private val nodes = LinkedList<Node>()

    fun addNode() {
        val variables = Node()
        nodes.addFirst(variables);
    }

    fun removeNode() {
        nodes.removeFirst();
    }

    fun modifyVariable(symb: String, value: Int) {
        for (node in nodes) {
            if (node.variables.containsKey(symb)) {
                node.variables[symb] = value
                return
            }
        }
        throw ElementNotFoundException()
    }

    fun addVariable(symb: String, value: Int?) {
        if (nodes.first.variables.containsKey(symb)) {
            throw OverrideException()
        }
        val putValue = value ?: 0
        nodes.first.variables.put(symb, putValue)
    }

    fun addFunction(symb: String, body: FunLangParser.FunctionContext) {
        if (nodes.first.functions.containsKey(symb)) {
            throw OverrideException()
        }
        nodes.first.functions[symb] = FunctionData(body, Scope(this))
    }

    fun getValue(symb: String): Int {
        for (node in nodes) {
            val value = node.variables[symb]
            if (value != null) {
                return value
            }
        }
        throw ElementNotFoundException()
    }

    fun getFunction(symb: String): FunctionData {
        for (node in nodes) {
            val functionData = node.functions[symb]
            if (functionData != null) {
                return functionData
            }
        }
        throw ElementNotFoundException()
    }
}