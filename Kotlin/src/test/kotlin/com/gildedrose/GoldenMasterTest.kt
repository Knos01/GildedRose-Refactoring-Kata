package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Golden master: 31 days (0..30) of the upstream texttest fixture, compared byte for byte with
 * src/test/resources/ThirtyDays.approved.txt, a verbatim copy of texttests/ThirtyDays/stdout.gr.
 * On mismatch the actual output is written to build/ThirtyDays.received.txt. Never writes to src.
 */
internal class GoldenMasterTest {

    @Test
    fun `thirty days match the upstream texttest output`() {
        val approvedBytes = javaClass.getResourceAsStream(APPROVED_RESOURCE)?.use { it.readBytes() }
            ?: fail("$APPROVED_RESOURCE not found on the test classpath")
        val received = render(days = 30)
        val receivedBytes = received.toByteArray(Charsets.UTF_8)
        val receivedFile = File(RECEIVED_PATH)

        if (receivedBytes.contentEquals(approvedBytes)) {
            receivedFile.delete()
            return
        }

        receivedFile.parentFile.mkdirs()
        receivedFile.writeBytes(receivedBytes)
        val message = "golden master mismatch; actual output written to ${receivedFile.absolutePath}"
        assertEquals(String(approvedBytes, Charsets.UTF_8), received, message)
        fail<Unit>("$message (decoded text is equal but the bytes differ)")
    }

    /** Same output as TexttestFixture.main(arrayOf("$days")), with '\n' as the line terminator. */
    private fun render(days: Int): String {
        val items = listOf(
            Item("+5 Dexterity Vest", 10, 20),
            Item("Aged Brie", 2, 0),
            Item("Elixir of the Mongoose", 5, 7),
            Item("Sulfuras, Hand of Ragnaros", 0, 80),
            Item("Sulfuras, Hand of Ragnaros", -1, 80),
            Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
            Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
            Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
            Item("Conjured Mana Cake", 3, 6),
        )
        val app = GildedRose(items)
        val out = StringBuilder()
        fun line(text: String) {
            out.append(text).append('\n')
        }

        line("OMGHAI!")
        for (day in 0..days) {
            line("-------- day $day --------")
            line("name, sellIn, quality")
            for (item in items) {
                line(item.toString())
            }
            line("")
            app.updateQuality()
        }
        return out.toString()
    }

    private companion object {
        const val APPROVED_RESOURCE = "/ThirtyDays.approved.txt"
        const val RECEIVED_PATH = "build/ThirtyDays.received.txt"
    }
}
