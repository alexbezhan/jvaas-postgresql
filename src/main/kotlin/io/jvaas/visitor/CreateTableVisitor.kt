package io.jvaas.visitor

import io.jvaas.gen.SQLParser
import io.jvaas.gen.SQLParserBaseVisitor
import io.jvaas.type.Column
import io.jvaas.type.Model
import io.jvaas.type.Table

class CreateTableVisitor(val model: Model) : SQLParserBaseVisitor<Unit>() {

	override fun visitCreate_table_statement(ctx: SQLParser.Create_table_statementContext?) {
		var createTableVisited = false
		ctx?.children?.forEach { child ->
			if (child.payload.javaClass == SQLParser.Schema_qualified_nameContext::class.java) {
				model.tables.add(Table(name = child.text))
				createTableVisited = true
			}
		}
		super.visitCreate_table_statement(ctx)
	}

	override fun visitDefine_columns(ctx: SQLParser.Define_columnsContext?) {
		ctx?.children?.forEach { columnDefinition ->
			if (columnDefinition.payload.javaClass == SQLParser.Table_column_defContext::class.java) {
				(0 until columnDefinition.childCount).map {
					columnDefinition.getChild(it)
				}.forEach { columnDefContext ->
					(0 until columnDefContext.childCount).map {
						columnDefContext.getChild(it)
					}.forEach { columnDefContextToken ->
						when (columnDefContextToken) {
							is SQLParser.IdentifierContext -> {
								model.lastTable.columns.add(Column(name = columnDefContextToken.text))
							}
							is SQLParser.Data_typeContext -> {
								model.lastColumn.type = columnDefContextToken.text
							}
							is SQLParser.Constraint_commonContext -> {
								columnDefContextToken.children.forEach { columnDefContextTokenConstraint ->
									if (columnDefContextTokenConstraint.text == "notnull") {
										model.lastColumn.nullable = false
									} else if (columnDefContextTokenConstraint.text.startsWith("default")) {
										model.lastColumn.default =
											columnDefContextTokenConstraint.text.replaceFirst("default", "")
									}
								}
							}
							else -> println(columnDefContextToken.payload::class.java)
						}
					}
				}
			}
			super.visitDefine_columns(ctx)
		}


	}
}