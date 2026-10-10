package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Pins what GildedRose.kt does today for inputs the golden master does not cover.
 * Expected values come from the current code, not from GildedRoseRequirements.md: quirks are pinned, not fixed.
 */
internal class CharacterizationTest {

    @Test
    fun `aged brie past sell-by gains 2`() {
        val items = listOf(Item("Aged Brie", 0, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Aged Brie, -1, 12", app.items[0].toString())
    }

    @Test
    fun `backstage pass on concert day drops to 0`() {
        val items = listOf(Item("Backstage passes to a TAFKAL80ETC concert", 0, 20))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Backstage passes to a TAFKAL80ETC concert, -1, 0", app.items[0].toString())
    }

    @Test
    fun `aged brie above 50 keeps its quality`() {
        val items = listOf(Item("Aged Brie", 5, 55))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Aged Brie, 4, 55", app.items[0].toString())
    }

    @Test
    fun `backstage pass above 50 keeps its quality`() {
        val items = listOf(Item("Backstage passes to a TAFKAL80ETC concert", 5, 55))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Backstage passes to a TAFKAL80ETC concert, 4, 55", app.items[0].toString())
    }

    @Test
    fun `normal item above 50 degrades by 1 without clamping`() {
        val items = listOf(Item("foo", 5, 60))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("foo, 4, 59", app.items[0].toString())
    }

    @Test
    fun `normal item with negative quality stays negative`() {
        val items = listOf(Item("foo", 5, -1))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("foo, 4, -1", app.items[0].toString())
    }

    @Test
    fun `sulfuras keeps a quality other than 80`() {
        val items = listOf(Item("Sulfuras, Hand of Ragnaros", 5, 50))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Sulfuras, Hand of Ragnaros, 5, 50", app.items[0].toString())
    }

    @Test
    fun `normal item at quality 1 on sell-by day stops at 0`() {
        val items = listOf(Item("foo", 0, 1))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("foo, -1, 0", app.items[0].toString())
    }

    @Test
    fun `aged brie at 49 on sell-by day stops at 50`() {
        val items = listOf(Item("Aged Brie", 0, 49))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Aged Brie, -1, 50", app.items[0].toString())
    }

    @Test
    fun `lowercase aged brie is a normal item`() {
        val items = listOf(Item("Aged brie", 5, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Aged brie, 4, 9", app.items[0].toString())
    }

    @Test
    fun `short sulfuras name is a normal item`() {
        val items = listOf(Item("Sulfuras", 5, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Sulfuras, 4, 9", app.items[0].toString())
    }

    @Test
    fun `backstage pass to another concert is a normal item`() {
        val items = listOf(Item("Backstage passes to another concert", 5, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Backstage passes to another concert, 4, 9", app.items[0].toString())
    }

    @Test
    fun `lowercase aged brie past sell-by is a normal item`() {
        val items = listOf(Item("Aged brie", 0, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Aged brie, -1, 8", app.items[0].toString())
    }

    @Test
    fun `short sulfuras name past sell-by is a normal item`() {
        val items = listOf(Item("Sulfuras", 0, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Sulfuras, -1, 8", app.items[0].toString())
    }

    @Test
    fun `backstage pass to another concert past sell-by is a normal item`() {
        val items = listOf(Item("Backstage passes to another concert", 0, 10))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("Backstage passes to another concert, -1, 8", app.items[0].toString())
    }
}
