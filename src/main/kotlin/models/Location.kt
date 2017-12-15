package models

data class Location(val locationId: Int,
                    val locationName: LocationName,
                    val locationDetails: String)

enum class LocationName {
    NUS, NTU, SMU, SP, RP, NP, NYP, TP
}

