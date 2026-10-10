package com.gildedrose

private enum class Category { AGED_BRIE, BACKSTAGE_PASSES, LEGENDARY, NORMAL }

private fun categoryOf(item: Item): Category = when (item.name) {
    "Aged Brie" -> Category.AGED_BRIE
    "Backstage passes to a TAFKAL80ETC concert" -> Category.BACKSTAGE_PASSES
    "Sulfuras, Hand of Ragnaros" -> Category.LEGENDARY
    else -> Category.NORMAL
}

private const val MIN_QUALITY = 0
private const val MAX_QUALITY = 50

class GildedRose(val items: List<Item>) {

    fun updateQuality() {
        for (item in items) {
            updateItem(item)
        }
    }
    
    private fun updateItem(item: Item) {
        when (categoryOf(item)) {
            Category.AGED_BRIE -> updateAgedBrie(item)
            Category.BACKSTAGE_PASSES -> updateBackstagePasses(item)
            Category.LEGENDARY -> {}
            Category.NORMAL -> updateNormalItem(item)
        }
    }
    
    private fun updateAgedBrie(item: Item) {
        item.sellIn = item.sellIn - 1
        val increase = if (item.sellIn < 0) 2 else 1
        repeat(increase) { increaseQuality(item) }
    }

    private fun updateNormalItem(item: Item) {
        item.sellIn = item.sellIn - 1
        val decrease = if (item.sellIn < 0) 2 else 1
        repeat(decrease) { decreaseQuality(item) }
    }
 
    private fun updateBackstagePasses(item: Item) {
        val increase = when {
            item.sellIn <= 5 -> 3
            item.sellIn <= 10 -> 2
            else -> 1
        }
        repeat(increase) { increaseQuality(item) }
        
        item.sellIn = item.sellIn - 1
        if (item.sellIn < 0) {
            item.quality = 0
        }
    }
    
    private fun increaseQuality(item: Item) {
        if (item.quality < MAX_QUALITY) {
            item.quality = item.quality + 1
        }
    }
 
    private fun decreaseQuality(item: Item) {
        if (item.quality > MIN_QUALITY) {
            item.quality = item.quality - 1
        }
    }
     
}

