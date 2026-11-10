package net.aucutt.drinkwise.math

object StandardDrink {

     //bit grandiose  for one function, wouldn't you say?
    fun drinks( abv: Float, volume : Float) = (abv  * volume) / 60.0f  //TODO business logic, I want test cases
}
