package basic

import java.lang.Integer.sum
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Набор базовых тестов")
class BasicTests {

    @Test
    fun `test sum`() {
        val a = 1
        val b = 41
        assertEquals(42, sum(a, b), "Неверный результат sum($a, $b)!")
    }
}