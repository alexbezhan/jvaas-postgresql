package io.jvaas.visitor

import io.jvaas.gen.SQLParser
import io.jvaas.gen.SQLParserBaseVisitor
import io.jvaas.mapper.SQLToKotlinTypeMapper
import io.jvaas.mapper.StringMapper.snakeToLowerCamelCase
import io.jvaas.type.*
import org.antlr.v4.runtime.tree.ParseTree
import java.util.*

class Visitor(val model: Model) : SQLParserBaseVisitor<Unit>() {

	var lastFun: String? = null
	var lastSQL: String? = null

	val lastTable
		get() = model.tables.last()

	val lastColumn
		get() = model.tables.last().columns.last()

	val lastQuery
		get() = model.queries.last()



	override fun visitStatement(ctx: SQLParser.StatementContext?) {
		lastSQL = Extractor(ctx).extractSQL(debug = false)
		super.visitStatement(ctx)
	}

	override fun visitLineComment(ctx: SQLParser.LineCommentContext?) {
		val parts = ctx?.text?.split(" ")
		var foundFun = false
		parts?.forEach { part ->
			if (part.trim() == "fun") {
				foundFun = true
			} else if (foundFun) {
				lastFun = part.trim()
			}
		}
		super.visitLineComment(ctx)
	}

	override fun visitCreateTableStatement(ctx: SQLParser.CreateTableStatementContext?) {
		var createTableVisited = false
		ctx?.children?.forEach { child ->
			if (child.payload.javaClass == SQLParser.SchemaQualifiedNameContext::class.java) {
				model.tables.add(Table(name = child.text))
				createTableVisited = true
			}
		}
		super.visitCreateTableStatement(ctx)
	}

	override fun visitDefineColumns(ctx: SQLParser.DefineColumnsContext?) {
		ctx?.children?.forEach { columnDefinition ->
			if (columnDefinition.payload.javaClass == SQLParser.TableColumnDefContext::class.java) {
				(0 until columnDefinition.childCount).map {
					columnDefinition.getChild(it)
				}.forEach { columnDefContext ->
					(0 until columnDefContext.childCount).map {
						columnDefContext.getChild(it)
					}.forEach { columnDefContextToken ->
						when (columnDefContextToken) {
							is SQLParser.IdentifierContext -> {
								lastTable.columns.add(Column(
									name = columnDefContextToken.text,
									kotlinName = columnDefContextToken.text.snakeToLowerCamelCase(),
									table = lastTable
								))
							}
							is SQLParser.DataTypeContext -> {
								lastColumn.type = columnDefContextToken.text
							}
							is SQLParser.ConstraintCommonContext -> {
								columnDefContextToken.children.forEach { columnDefContextTokenConstraint ->
									if (columnDefContextTokenConstraint.text.equals("notnull", ignoreCase = true)) {
										lastColumn.nullable = false
									} else if (columnDefContextTokenConstraint.text.startsWith("default")) {
										lastColumn.default =
											columnDefContextTokenConstraint.text.replaceFirst("default", "")
									}
								}
							}
							else -> println(columnDefContextToken.payload::class.java)
						}
					}
				}
			}
		}

		lastTable.columns.forEach { column ->
			column.kotlinType = SQLToKotlinTypeMapper.map(column.type, column.nullable)
		}

		super.visitDefineColumns(ctx)
	}


	override fun visitInsertStmtForPsql(ctx: SQLParser.InsertStmtForPsqlContext?) {


//		ctx?.children?.forEach { child ->
//			println(child.payload.javaClass)
//			println(child.text)
//		}
//
//		println(ctx?.children)

		super.visitInsertStmtForPsql(ctx)
	}

	// UPDATE
	override fun visitUpdateStmtForPsql(ctx: SQLParser.UpdateStmtForPsqlContext?) {

		model.queries.add(Query(
			sql = lastSQL ?: "",
			name = lastFun ?: UUID.randomUUID().toString().replace("-", "")
		))
		lastFun = null

		println("========================")
		println(lastSQL)
		println(Extractor(ctx).extract<SQLParser.IdentifierContext>())
		println("========================")

//		println("========================")
//		println(ctx?.let { Extractor(it).extractSQL() })
//		println("---------------------")
//		println(model.queries.last().sql)
//		println(ctx?.let { Extractor(it).extractColumns() })
//		println("========================")

//		var tableScope: Table? = null
//		var columnScope: Column? = null
//
//		ctx?.children?.forEach { child ->
//			when (child) {
//				is SQLParser.SchemaQualifiedNameContext -> {
//					model.tables.forEach { table ->
//						if (table.name.equals(child.text, ignoreCase = true)) {
//							tableScope = table
//						}
//					}
//				}
//				is SQLParser.UpdateSetContext -> {
//					@Suppress("NAME_SHADOWING")
//					child.children.forEach { child ->
//						when (child) {
//							is SQLParser.IndirectionIdentifierContext -> {
//								tableScope?.columns?.filter { column ->
//									column.name.equals(child.text, ignoreCase = true)
//								}?.firstOrNull()?.let { column ->
//									columnScope = column
//								}
//							}
//							is SQLParser.VexContext -> {
//								columnScope?.let {
//									lastQuery.columns.add(ColumnWithValue(
//										column = it,
//										value = child.text
//									))
//								} ?: run {
//									throw Exception("ColumnScope is null for value ${child.text}")
//								}
//							}
//							else -> {
//								println(child.javaClass)
//							}
//						}
//
//
//					}
//					//println("set:${child.text}")
//					println("======")
//				}
//				is SQLParser.VexContext -> {
//					@Suppress("NAME_SHADOWING")
//					child.children.forEach { child ->
//						when (child) {
//							is SQLParser.VexContext -> {
//								println(child.text)
//							}
//							else -> {
//								println(child.javaClass)
//							}
//						}
//					}
//				}
//			}
//		}
//		if (tableScope == null) {
//			throw Exception("""
//				Not a valid table name for query: ${lastQuery.sql}"
//				Available table names are: ${model.tables.joinToString { it.name }}
//			""".trimIndent())
//		}


		super.visitUpdateStmtForPsql(ctx)
	}

}