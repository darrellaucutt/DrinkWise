package net.aucutt.drinkwise.math

object StandardDrink {

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
}
