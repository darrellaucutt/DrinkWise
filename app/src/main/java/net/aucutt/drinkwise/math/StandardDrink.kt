package net.aucutt.drinkwise.math

import net.aucutt.drinkwise.data.Drink

object StandardDrink {


    const val NIA_URL="https://www.niaaa.nih.gov/health-professionals-communities/core-resource-on-alcohol/basics-defining-how-much-alcohol-too-much#pub-toc4"
    enum class DrinkType(val displayText: String) {
        WINE("Wine"),
        BEER("Beer"),
        HARD_ALCOHOL("Hard Alcohol"),
        CIDER("Cider"),
        SELTZER("Seltzer"),
        OTHER("Other")
    }

    fun drinkWeight(abv: Float, volume : Float) = (abv  * volume) / 60.0f
    fun drinkInferer(abv: Float, volume : Float) : DrinkType {
        if (volume < 4f &&  abv > 30f) {
            return DrinkType.HARD_ALCOHOL
        }
        if (volume >= 8f && ((abv > 4.0f)  && (abv < 10f))) {
            return DrinkType.BEER
        }
        if( volume >= 4f && (abv in 12f..25f)) {
            return DrinkType.WINE
        }
        //TODO were I collecting metrics and the users gets here, I would like to collect the volume and abv
        // and then see what the user selected.
        return DrinkType.OTHER
    }

    fun computeTotalForWeek(drinks : List<Drink>) :Float {
        var total  = 0f
        for( drink in drinks ) {
            total += drink.drinkWeight
        }
        return total
    }
    //I think I want to allow them to select a time period.
    fun computeTotalForDay(drinks : List<Drink>) :Float {
        var total  = 0f
        for( drink in drinks ) {
            total += drink.drinkWeight
        }
        return total
    }

}
