package exposed.sample.h2.db.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object CitiesTable : Table(name = "cities") {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50)

    override val primaryKey = PrimaryKey(id, name = "PK_Cities_ID")
}