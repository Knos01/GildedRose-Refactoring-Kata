package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * Conjured rule (my decision, recorded in AI-LOG.md): a name starting with "Conjured " (with the space, case-sensitive), checked after
 * the exact special names, overrides the base item's rules and degrades twice as fast as a normal item:
 * -2 before the sell-by date, -4 after it, each step guarded at 0 like a normal item.
 * Expected values are derived from that rule, not from code.
 */
internal class ConjuredTest {

    @Disabled("pending Conjured")
    @Test
    fun `conjured item degrades by 2 before the sell-by date`() {
        val items = listOf(Item("Conjured Mana Cake", 3, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Mana Cake, 2, 4", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured item degrades by 4 once the sell-by date has passed`() {
        val items = listOf(Item("Conjured Mana Cake", 0, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Mana Cake, -1, 2", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured item quality never goes below 0`() {
        val items = listOf(Item("Conjured Mana Cake", 1, 1))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Mana Cake, 0, 0", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured item quality never goes below 0 past the sell-by date`() {
        val items = listOf(Item("Conjured Mana Cake", 0, 3))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Mana Cake, -1, 0", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured item above 50 degrades by 2 without clamping`() {
        val items = listOf(Item("Conjured Mana Cake", 5, 55))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Mana Cake, 4, 53", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured item with negative quality stays negative`() {
        val items = listOf(Item("Conjured Mana Cake", 5, -1))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Mana Cake, 4, -1", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured aged brie follows conjured rules`() {
        val items = listOf(Item("Conjured Aged Brie", 5, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Aged Brie, 4, 8", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured aged brie follows conjured rules past the sell-by date`() {
        val items = listOf(Item("Conjured Aged Brie", 0, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Aged Brie, -1, 6", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured sulfuras is sold and degrades`() {
        val items = listOf(Item("Conjured Sulfuras, Hand of Ragnaros", 5, 80))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Sulfuras, Hand of Ragnaros, 4, 78", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured not at the start of the name is a normal item`() {
        val items = listOf(Item("Mana Cake (Conjured)", 5, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Mana Cake (Conjured), 4, 9", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `lowercase conjured is a normal item`() {
        val items = listOf(Item("conjured mana cake", 5, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("conjured mana cake, 4, 9", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured backstage pass degrades by 2 before the concert`() {
        val items = listOf(Item("Conjured Backstage passes to a TAFKAL80ETC concert", 5, 20))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Backstage passes to a TAFKAL80ETC concert, 4, 18", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured backstage pass degrades by 4 after the concert instead of dropping to 0`() {
        val items = listOf(Item("Conjured Backstage passes to a TAFKAL80ETC concert", 0, 20))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Conjured Backstage passes to a TAFKAL80ETC concert, -1, 16", app.items[0].toString())
    }

    @Disabled("pending Conjured")
    @Test
    fun `conjured without the following space is a normal item`() {
        val items = listOf(Item("ConjuredX", 5, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("ConjuredX, 4, 9", app.items[0].toString())
    }
}
