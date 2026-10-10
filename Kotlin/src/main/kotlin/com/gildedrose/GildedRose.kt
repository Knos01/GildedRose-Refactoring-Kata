package com.gildedrose

private enum class Category { AGED_BRIE, BACKSTAGE_PASSES, LEGENDARY, NORMAL }

private fun categoryOf(item: Item): Category = when (item.name) {
    "Aged Brie" -> Category.AGED_BRIE
    "Backstage passes to a TAFKAL80ETC concert" -> Category.BACKSTAGE_PASSES
    "Sulfuras, Hand of Ragnaros" -> Category.LEGENDARY
    else -> Category.NORMAL
}

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
        if (item.quality < 50) {
            item.quality = item.quality + 1
        }
        item.sellIn = item.sellIn - 1
        if (item.sellIn < 0 && item.quality < 50) {
            item.quality = item.quality + 1
        }
    }
 
    private fun updateBackstagePasses(item: Item) {
        if (item.quality < 50) {
            item.quality = item.quality + 1
            if (item.sellIn < 11 && item.quality < 50) {
                item.quality = item.quality + 1
            }
            if (item.sellIn < 6 && item.quality < 50) {
                item.quality = item.quality + 1
            }
        }
        item.sellIn = item.sellIn - 1
        if (item.sellIn < 0) {
            item.quality = 0
        }
    }
 
    private fun updateNormalItem(item: Item) {
        if (item.quality > 0) {
            item.quality = item.quality - 1
        }
        item.sellIn = item.sellIn - 1
        if (item.sellIn < 0 && item.quality > 0) {
            item.quality = item.quality - 1
        }
    }
     
}

