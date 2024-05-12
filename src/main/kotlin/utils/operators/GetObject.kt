package utils.operators

/**
 * Функциональный (SAM) интерфейс для динамического создания объекта с переопределенным оператором get().
 * [V] - тип объекта value для получения объекта-результата [R]
 */
fun interface GetObject<V, R> {
    operator fun get(value: V): R
}