package emilien.tetu.taptogo.domain.model

enum class StateStation {
    OPEN,
    CLOSE,
    EMPTY,
    FULL
}

fun StateStation.toStringStatus() : String{
    return when(this){
        StateStation.OPEN -> "Station open"
        StateStation.CLOSE -> "Station close"
        StateStation.FULL -> "Full of bike"
        StateStation.EMPTY -> "No bikes available"
    }
}