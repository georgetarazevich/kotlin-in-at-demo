package exposed.sample.h2.db.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UsersTable : Table(name = "users") {
    val id: Column<String> = varchar("id", 10)
    val name: Column<String> = varchar("name", length = 50)
    val cityId: Column<Int?> = (integer("city_id") references CitiesTable.id).nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_User_ID") // name is optional here
}