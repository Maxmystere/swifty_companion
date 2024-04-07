package tech.maxdevcoveries.swiftycompanion.lib

import com.google.gson.annotations.SerializedName

data class FortyTwoImageVersion(
    @SerializedName("large")
    val large:String,
    @SerializedName("medium")
    val medium:String,
    @SerializedName("small")
    val small:String,
    @SerializedName("micro")
    val micro:String,
)

data class FortyTwoProfileImage(
    @SerializedName("link")
    val link:String,
    @SerializedName("versions")
    val versions:FortyTwoImageVersion,
)

data class CursusUser(
    @SerializedName("cursus_id")
    val cursusId:Int,
    @SerializedName("level")
    val level:Float,
)

data class FortyTwoUser(
    @SerializedName("id")
    val id:String,
    @SerializedName("email")
    val email:String,
    @SerializedName("displayname")
    val displayName:String,
    @SerializedName("image")
    val image:FortyTwoProfileImage,
    @SerializedName("pool_month")
    val poolMonth:String,
    @SerializedName("pool_year")
    val poolYear:String,
    @SerializedName("cursus_users")
    val cursusUsers:Array<CursusUser>,
)
{
    fun getActiveCursus():CursusUser?
    {
        return cursusUsers.find {
            it.cursusId == 21 // Current main cursus is of id 21
        }
    }
}