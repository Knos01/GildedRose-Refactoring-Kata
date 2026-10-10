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
            val category = categoryOf(item)
            if (category != Category.AGED_BRIE && category != Category.BACKSTAGE_PASSES) {
                if (item.quality > 0) {
                    if (category != Category.LEGENDARY) {
                        item.quality = item.quality - 1
                    }
                }
            } else {
                if (item.quality < 50) {
                    item.quality = item.quality + 1

                    if (category == Category.BACKSTAGE_PASSES) {
                        if (item.sellIn < 11) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1
                            }
                        }

                        if (item.sellIn < 6) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1
                            }
                        }
                    }
                }
            }

            if (category != Category.LEGENDARY) {
                item.sellIn = item.sellIn - 1
            }

            if (item.sellIn < 0) {
                if (category != Category.AGED_BRIE) {
                    if (category != Category.BACKSTAGE_PASSES) {
                        if (item.quality > 0) {
                            if (category != Category.LEGENDARY) {
                                item.quality = item.quality - 1
                            }
                        }
                    } else {
                        item.quality = item.quality - item.quality
                    }
                } else {
                    if (item.quality < 50) {
                        item.quality = item.quality + 1
                    }
                }
            }
        }
    }
    
}

