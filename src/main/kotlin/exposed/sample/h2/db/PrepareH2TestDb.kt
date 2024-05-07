package exposed.sample.h2.db

import exposed.sample.h2.db.model.CitiesTable
import exposed.sample.h2.db.model.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object PrepareH2TestDb {

    /**
     * Тестовая БД H2. Создаётся в памяти.
     * Для возможности переиспользования подключения в транзакциях используем дополнительную настройку:
     * [DB_CLOSE_DELAY=-1;](https://jetbrains.github.io/Exposed/database-and-datasource.html#datasource)
     */
    val testH2Db: Database by lazy {
        Database.connect(url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    }

    /** Создать БД для тестовых примеров */
    fun createH2TestDb(database: Database = testH2Db) {
        transaction(database) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(CitiesTable, UsersTable)

            val saintPetersburgId = CitiesTable.insert {
                it[name] = "St. Petersburg"
            } get CitiesTable.id

            UsersTable.insert {
                it[id] = "andrey"
                it[name] = "Andrey"
                it[cityId] = saintPetersburgId
            }

            val kronstadtId = CitiesTable.insert {
                it[name] = "Kotlin"
            } get CitiesTable.id

            UsersTable.insert {
                it[id] = "sergey"
                it[name] = "Sergey"
                it[cityId] = kronstadtId
            }
        }
    }
}