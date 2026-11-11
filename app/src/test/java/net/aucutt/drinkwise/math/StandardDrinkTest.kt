package net.aucutt.drinkwise.math

import org.junit.Assert.assertEquals
import org.junit.Test

class StandardDrinkTest {

    @Test
    fun drinkWeight_standardBeer_returnsOne() {
        // 12 oz of 5.0% ABV beer = 1.0 Standard Drink
        val result = StandardDrink.drinkWeight(abv = 5.0f, volume = 12.0f)
        assertEquals(1.0f, result, 0.001f)
    }

    @Test
    fun drinkWeight_standardWine_returnsOne() {
        // 5 oz of 12.0% ABV wine = 1.0 Standard Drink
        val result = StandardDrink.drinkWeight(abv = 12.0f, volume = 5.0f)
        assertEquals(1.0f, result, 0.001f)
    }

    @Test
    fun drinkWeight_standardShot_returnsOne() {
        // 1.5 oz of 40.0% ABV hard alcohol = 1.0 Standard Drink
        val result = StandardDrink.drinkWeight(abv = 40.0f, volume = 1.5f)
        assertEquals(1.0f, result, 0.001f)
    }

    @Test
    fun drinkWeight_pintOfStrongIPA_returnsTwo() {
        // 16 oz of 7.5% ABV IPA = 2.0 Standard Drinks
        val result = StandardDrink.drinkWeight(abv = 7.5f, volume = 16.0f)
        assertEquals(2.0f, result, 0.001f)
    }

    @Test
    fun drinkWeight_zeroAbv_returnsZero() {
        // Non-alcoholic beverage = 0.0 Standard Drinks
        val result = StandardDrink.drinkWeight(abv = 0.0f, volume = 12.0f)
        assertEquals(0.0f, result, 0.001f)
    }

    @Test
    fun drinkWeight_zeroVolume_returnsZero() {
        val result = StandardDrink.drinkWeight(abv = 5.0f, volume = 0.0f)
        assertEquals(0.0f, result, 0.001f)
    }

    @Test
    fun drinkInferer_budWeiser() {
        val result = StandardDrink.drinkInferer( abv = 5f, volume = 12f )
        assertEquals(result.ordinal, StandardDrink.DrinkType.BEER.ordinal)
    }

    @Test
    fun drinkInferer_georgeTownBodhizafaAtAPub() {
        val result = StandardDrink.drinkInferer( abv = 6.9f, volume = 16f )
        assertEquals(result.ordinal, StandardDrink.DrinkType.BEER.ordinal)
    }

    @Test
    fun drinkInferer_standardShot() {
        val result = StandardDrink.drinkInferer( abv = 40f, volume = 1.5f )
        assertEquals(result.ordinal, StandardDrink.DrinkType.HARD_ALCOHOL.ordinal)
    }

    @Test
    fun drinkInferer_fireballWhiskey() {
        val result = StandardDrink.drinkInferer( abv = 33f, volume = 1.5f )
        assertEquals(result.ordinal, StandardDrink.DrinkType.HARD_ALCOHOL.ordinal)
    }

    @Test
    fun drinkInferer_chardonnay() {
        val result = StandardDrink.drinkInferer( abv = 12f, volume = 5f )
        assertEquals(result.ordinal, StandardDrink.DrinkType.WINE.ordinal)
    }
    @Test
    fun drinkInferer_glassOfMerlotForDarrell() {
        val result = StandardDrink.drinkInferer( abv = 15f, volume = 8f )
        assertEquals(result.ordinal, StandardDrink.DrinkType.WINE.ordinal)
    }

    @Test
    fun drinkInferer_glassOfUnknown() {
        val result = StandardDrink.drinkInferer( abv = 26f, volume = 16f )
        assertEquals(result.ordinal, StandardDrink.DrinkType.OTHER.ordinal)
    }
}
